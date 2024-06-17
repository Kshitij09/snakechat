package data

import (
	"bytes"
	"database/sql"
	"encoding/base64"
	"fmt"
	"github.com/Kshitij09/snakechat_server/data/model"
	"strconv"
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
	var decodedOffset string
	if offset.Valid {
		offsetBytes, err := base64.StdEncoding.DecodeString(offset.String)
		if err != nil {
			return nil, fmt.Errorf("invalid offset: %w", err)
		}
		decodedOffset = string(offsetBytes)
	}
	var queryBuffer bytes.Buffer
	hasValidOffset := decodedOffset != ""
	if err := trendingFeedQuery.Execute(&queryBuffer, hasValidOffset); err != nil {
		return nil, fmt.Errorf("error parsing the feed query, %w", err)
	}
	query := queryBuffer.String()
	var resultRows *sql.Rows
	var queryErr error
	if hasValidOffset {
		resultRows, queryErr = ctx.db.Query(query, decodedOffset, feedSize)
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
		if err := resultRows.Scan(&post.Id, &post.Caption, &post.MediaUrl, &post.CreatedAt, &post.Rank, &post.Likes, &post.Shares, &post.Saves, &post.Downloads); err != nil {
			return nil, fmt.Errorf("error parsing trending feed row: %w", err)
		}
		posts = append(posts, post)
	}
	var nextOffset string
	if len(posts) > 0 {
		lastPost := posts[len(posts)-1]
		nextOffset = strconv.FormatInt(lastPost.Rank, 10)
		nextOffset = base64.StdEncoding.EncodeToString([]byte(nextOffset))
	}
	return &model.Feed{Posts: posts, Offset: nextOffset}, nil
}

const feedSize = 10

var trendingFeedQuery = template.Must(template.New("trending_feed").Parse(`
	SELECT p.id, p.caption, p.media_url, p.created_at, p.rank,
	SUM(CASE WHEN pe.type = 0 THEN 1 ELSE 0 END) AS likes,
	SUM(CASE WHEN pe.type = 1 THEN 1 ELSE 0 END) AS shares,
	SUM(CASE WHEN pe.type = 2 THEN 1 ELSE 0 END) AS saves,
	SUM(CASE WHEN pe.type = 3 THEN 1 ELSE 0 END) AS downloads
	FROM posts p 
	INNER JOIN tags t ON p.tag_id = t.id 
	INNER JOIN post_engagements pe ON p.id = pe.post_id
	WHERE t.title = 'trending' AND p.deleted_at IS null
	{{if . -}}AND p.rank < ?{{println}}{{end}}GROUP BY p.id
	ORDER BY p.rank DESC
	LIMIT ?
`))
