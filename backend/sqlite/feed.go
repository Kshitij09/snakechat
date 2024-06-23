package sqlite

import (
	"bytes"
	"database/sql"
	"fmt"
	"github.com/Kshitij09/snakechat_server/snakechat"
	"text/template"
)

type PostStorage struct {
	db *sql.DB
}

func NewPostStorage(db *sql.DB) *PostStorage {
	return &PostStorage{db: db}
}

func (ctx *PostStorage) TrendingPostsBelowRank(rank int) ([]snakechat.Post, error) {
	return ctx.trendingPostsBelowRank(&rank)
}

func (ctx *PostStorage) TrendingPosts() ([]snakechat.Post, error) {
	return ctx.trendingPostsBelowRank(nil)
}

func (ctx *PostStorage) trendingPostsBelowRank(rank *int) ([]snakechat.Post, error) {
	resultRows, queryErr := ctx.getPostBelowRank(rank, snakechat.FeedPageSize)
	defer resultRows.Close()
	if queryErr != nil {
		return nil, fmt.Errorf("error getting trending posts: %w", queryErr)
	}
	return scanPosts(resultRows)
}

func (ctx *PostStorage) getPostBelowRank(rank *int, feedSize int) (*sql.Rows, error) {
	query, err := buildQuery(rank)
	if err != nil {
		return nil, fmt.Errorf("error parsing the feed query, %w", err)
	}
	if rank != nil {
		return ctx.db.Query(query, rank, feedSize)
	} else {
		return ctx.db.Query(query, feedSize)
	}
}

func buildQuery(rank *int) (string, error) {
	var queryBuffer bytes.Buffer
	var templateErr error
	if rank != nil {
		templateErr = trendingFeedQuery.Execute(&queryBuffer, rank)
	} else {
		templateErr = trendingFeedQuery.Execute(&queryBuffer, false)
	}
	return queryBuffer.String(), templateErr
}

func scanPosts(rows *sql.Rows) ([]snakechat.Post, error) {
	posts := make([]snakechat.Post, 0, snakechat.FeedPageSize)
	for rows.Next() {
		var post snakechat.Post
		post, err := scanPost(rows)
		if err != nil {
			return nil, fmt.Errorf("error parsing trending posts row: %w", err)
		}
		posts = append(posts, post)
	}
	return posts, nil
}
func scanPost(resultRows *sql.Rows) (snakechat.Post, error) {
	var post snakechat.Post
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
