package data

import (
	"bytes"
	"database/sql"
	"encoding/base64"
	"fmt"
	"github.com/Kshitij09/snakechat_server/data/model"
	"reflect"
	"strconv"
	"strings"
	"text/template"
)

type feedSqlContext struct {
	db *sql.DB
}

func (ctx *feedSqlContext) GetTrendingFeed(offset string) (*model.Feed, error) {
	return ctx.getTrendingFeed(sql.NullString{String: offset, Valid: true})
}

func (ctx *feedSqlContext) GetFirstTrendingFeed() (*model.Feed, error) {
	return ctx.getTrendingFeed(sql.NullString{})
}

func (ctx *feedSqlContext) getTrendingFeed(offset sql.NullString) (*model.Feed, error) {
	var offsetParams feedOffsetParams
	if offset.Valid {
		offsetStr, err := base64.StdEncoding.DecodeString(offset.String)
		invalidOffsetErr := fmt.Errorf("invalid offset")
		if err != nil {
			return nil, invalidOffsetErr
		}
		offsetParts := strings.Split(string(offsetStr), "_")
		if len(offsetParts) != 2 {
			return nil, invalidOffsetErr
		}
		total, err := strconv.ParseInt(offsetParts[0], 10, 64)
		if err != nil {
			return nil, invalidOffsetErr
		}
		createdAt, err := strconv.ParseInt(offsetParts[1], 10, 64)
		if err != nil {
			return nil, invalidOffsetErr
		}
		offsetParams = feedOffsetParams{Total: total, CreatedAt: createdAt}
	}
	var queryBuffer bytes.Buffer
	isValidOffset := !reflect.ValueOf(offsetParams).IsZero()
	if err := trendingFeedQuery.Execute(&queryBuffer, isValidOffset); err != nil {
		return nil, fmt.Errorf("error parsing the feed query, %w", err)
	}
	query := queryBuffer.String()
	var resultRows *sql.Rows
	var queryErr error
	if isValidOffset {
		resultRows, queryErr = ctx.db.Query(query, offsetParams.Total, offsetParams.CreatedAt, feedSize)
	} else {
		resultRows, queryErr = ctx.db.Query(query, feedSize)
	}
	defer resultRows.Close()
	if queryErr != nil {
		return nil, fmt.Errorf("error getting trending feed: %w", queryErr)
	}
	posts := make([]model.Post, 0, feedSize)
	for resultRows.Next() {
		var post model.Post
		if err := resultRows.Scan(&post.Id, &post.Caption, &post.MediaUrl, &post.CreatedAt, &post.Likes, &post.Shares, &post.Saves, &post.Downloads, &post.Total); err != nil {
			return nil, fmt.Errorf("error parsing trending feed row: %w", err)
		}
		posts = append(posts, post)
	}
	var nextOffset string
	if len(posts) > 0 {
		lastPost := posts[len(posts)-1]
		nextOffset = fmt.Sprintf("%d_%d", lastPost.Total, lastPost.CreatedAt)
		nextOffset = base64.StdEncoding.EncodeToString([]byte(nextOffset))
	}
	return &model.Feed{Posts: posts, Offset: nextOffset}, nil
}

const feedSize = 10

var trendingFeedQuery = template.Must(template.New("trending_feed").Parse(`
WITH post_counters AS (
	SELECT p.id, p.caption, p.media_url, p.created_at,
	count(*) as total,
	SUM(CASE WHEN pe.type = 0 THEN 1 ELSE 0 END) AS likes,
	SUM(CASE WHEN pe.type = 1 THEN 1 ELSE 0 END) AS shares,
	SUM(CASE WHEN pe.type = 2 THEN 1 ELSE 0 END) AS saves,
	SUM(CASE WHEN pe.type = 3 THEN 1 ELSE 0 END) AS downloads
	FROM posts p 
	INNER JOIN tags t ON p.tag_id = t.id 
	INNER JOIN post_engagements pe ON p.id = pe.post_id
	WHERE t.title = 'trending' AND p.deleted_at IS null
	GROUP BY p.id
	ORDER BY total desc, p.created_at DESC
)
SELECT id, caption, media_url, created_at, likes, shares, saves, downloads, total FROM post_counters
{{if . -}}WHERE total <= ? AND created_at < ?{{println}}{{end}}LIMIT ?
`))

type feedOffsetParams struct {
	Total     int64
	CreatedAt int64
}
