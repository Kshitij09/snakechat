package sqlite

import (
	"bytes"
	"database/sql"
	"fmt"
	"github.com/Kshitij09/snakechat_server/domain"
	"text/template"
)

type PostStorage struct {
	db *sql.DB
}

func NewPostStorage(db *sql.DB) *PostStorage {
	return &PostStorage{db: db}
}

func (ctx *PostStorage) TrendingPostsBelowRank(rank float64) ([]domain.Post, error) {
	return ctx.trendingPostsBelowRank(&rank)
}

func (ctx *PostStorage) TrendingPosts() ([]domain.Post, error) {
	return ctx.trendingPostsBelowRank(nil)
}

func (ctx *PostStorage) trendingPostsBelowRank(rank *float64) ([]domain.Post, error) {
	resultRows, queryErr := ctx.getPostBelowRank(rank, domain.FeedPageSize)
	if queryErr != nil {
		return nil, fmt.Errorf("error getting trending posts: %w", queryErr)
	}
	defer resultRows.Close()
	return scanPosts(resultRows)
}

func (ctx *PostStorage) getPostBelowRank(rank *float64, feedSize int) (*sql.Rows, error) {
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

func buildQuery(rank *float64) (string, error) {
	var queryBuffer bytes.Buffer
	var templateErr error
	if rank != nil {
		templateErr = trendingFeedQuery.Execute(&queryBuffer, rank)
	} else {
		templateErr = trendingFeedQuery.Execute(&queryBuffer, false)
	}
	return queryBuffer.String(), templateErr
}

func scanPosts(rows *sql.Rows) ([]domain.Post, error) {
	posts := make([]domain.Post, 0, domain.FeedPageSize)
	for rows.Next() {
		var post domain.Post
		post, err := scanPost(rows)
		if err != nil {
			return nil, fmt.Errorf("error parsing trending posts row: %w", err)
		}
		posts = append(posts, post)
	}
	return posts, nil
}
func scanPost(resultRows *sql.Rows) (domain.Post, error) {
	var post domain.Post
	err := resultRows.Scan(
		&post.Id,
		&post.Caption,
		&post.MediaUrl,
		&post.CreatedAt,
		&post.Rank,
		&post.Comments,
		&post.Likes,
		&post.Views,
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
IFNULL(p.comments_count, 0),
IFNULL(p.likes_count, 0),
IFNULL(p.views_count, 0),
IFNULL(p.shares_count, 0),
IFNULL(p.saves_count, 0),
IFNULL(p.downloads_count, 0),
t.id as tag_id, u.id as user_id, u.name as user_name, u.profile_url as user_profile_url
FROM posts p 
INNER JOIN tags t ON p.tag_id = t.id
INNER JOIN users u ON p.user_id = u.id
WHERE t.title = 'trending' AND p.deleted_at IS null
AND p.rank IS NOT NULL
{{if . -}}AND p.rank < ?{{println}}{{end}}ORDER BY p.rank DESC
LIMIT ?
`))
