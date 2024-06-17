package data

import (
	"bytes"
	"database/sql"
	"fmt"
	"github.com/Kshitij09/snakechat_server/data/model"
	"text/template"
)

type feedQueryRunner interface {
	getFeed(rankFilter *int, feedSize int) (*sql.Rows, error)
	scanFeedRow(resultRows *sql.Rows) (model.Post, error)
}

type feedQueryContext struct {
	db *sql.DB
}

func (ctx feedQueryContext) getFeed(rankFilter *int, feedSize int) (*sql.Rows, error) {
	query, err := buildQuery(rankFilter)
	if err != nil {
		return nil, fmt.Errorf("error parsing the feed query, %w", err)
	}
	if rankFilter != nil {
		return ctx.db.Query(query, rankFilter, feedSize)
	} else {
		return ctx.db.Query(query, feedSize)
	}
}

func buildQuery(rankFilter *int) (string, error) {
	var queryBuffer bytes.Buffer
	var templateErr error
	if rankFilter != nil {
		templateErr = trendingFeedQuery.Execute(&queryBuffer, rankFilter)
	} else {
		templateErr = trendingFeedQuery.Execute(&queryBuffer, false)
	}
	return queryBuffer.String(), templateErr
}

var trendingFeedQuery = template.Must(template.New("trending_feed").Parse(`
SELECT p.id, p.caption, p.media_url, p.created_at, p.rank,
SUM(CASE WHEN pe.type = 0 THEN 1 ELSE 0 END) AS likes,
SUM(CASE WHEN pe.type = 1 THEN 1 ELSE 0 END) AS shares,
SUM(CASE WHEN pe.type = 2 THEN 1 ELSE 0 END) AS saves,
SUM(CASE WHEN pe.type = 3 THEN 1 ELSE 0 END) AS downloads,
t.id as tag_id, u.id as user_id, u.name as user_name, u.profile_url as user_profile_url
FROM posts p 
INNER JOIN tags t ON p.tag_id = t.id
INNER JOIN users u ON p.user_id = u.id
INNER JOIN post_engagements pe ON p.id = pe.post_id
WHERE t.title = 'trending' AND p.deleted_at IS null
{{if . -}}AND p.rank < ?{{println}}{{end}}GROUP BY p.id
ORDER BY p.rank DESC
LIMIT ?
`))

func (ctx feedQueryContext) scanFeedRow(resultRows *sql.Rows) (model.Post, error) {
	var post model.Post
	err := resultRows.Scan(
		&post.Id,
		&post.Caption,
		&post.MediaUrl,
		&post.CreatedAt,
		&post.Rank,
		&post.Likes,
		&post.Shares,
		&post.Saves,
		&post.Downloads,
		&post.TagId,
		&post.User.Id,
		&post.User.Name,
		&post.User.ProfileUrl,
	)
	return post, err
}
