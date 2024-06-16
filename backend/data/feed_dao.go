package data

import (
	"database/sql"
	"fmt"
	"github.com/Kshitij09/snakechat_server/data/model"
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
	rows, err := ctx.db.Query(trendingFeedQuery, offset)
	defer rows.Close()
	if err != nil {
		return nil, fmt.Errorf("error getting trending feed: %w", err)
	}
	var posts []model.Post
	for rows.Next() {
		var post model.Post
		if err := rows.Scan(&post.Id, &post.Caption, &post.MediaUrl, &post.CreatedAt, &post.Likes, &post.Shares, &post.Saves, &post.Downloads); err != nil {
			return nil, fmt.Errorf("error parsing trending feed row: %w", err)
		}
		posts = append(posts, post)
	}
	return &model.Feed{Posts: posts}, nil
}

const trendingFeedQuery = `
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
SELECT id, caption, media_url, created_at, likes, shares, saves, downloads FROM post_counters
LIMIT 10
`
