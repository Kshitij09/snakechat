package sqlite

import (
	"database/sql"
	"fmt"
	"github.com/Kshitij09/snakechat_server/domain"
)

type CommentsStorage struct {
	db *sql.DB
}

func NewCommentsStorage(db *sql.DB) *CommentsStorage {
	return &CommentsStorage{db: db}
}

func (ctx CommentsStorage) PostComments(postId string) ([]domain.Comment, error) {
	query := `
		SELECT c.id, c.updated_at, c.text, u.id, u.name, u.profile_url 
		FROM comments c
		INNER JOIN users u ON c.user_id = u.id
		WHERE post_id = ?
		LIMIT ?
	`
	comments, err := ctx.queryPostComments(query, postId, domain.CommentsPageSize)
	if err != nil {
		return nil, fmt.Errorf("sqlite.PostComments: %w", err)
	}
	return comments, nil
}

func (ctx CommentsStorage) PostCommentsUpdatedBefore(postId string, updateTimestamp int64) ([]domain.Comment, error) {
	query := `
		SELECT c.id, c.updated_at, c.text, u.id, u.name, u.profile_url 
		FROM comments c
		INNER JOIN users u ON c.user_id = u.id
		WHERE post_id = ?
		AND updated_at < ?
		LIMIT ?
	`
	comments, err := ctx.queryPostComments(query, postId, updateTimestamp, domain.CommentsPageSize)
	if err != nil {
		return nil, fmt.Errorf("sqlite.PostCommentsUpdatedBefore: %w", err)
	}
	return comments, nil
}

func (ctx CommentsStorage) queryPostComments(query string, args ...any) ([]domain.Comment, error) {
	rs, err := ctx.db.Query(query, args...)
	if err != nil {
		return nil, err
	}
	defer rs.Close()
	comments := make([]domain.Comment, 0)
	for rs.Next() {
		var comment domain.Comment
		err := rs.Scan(
			&comment.Id,
			&comment.UpdatedAt,
			&comment.Text,
			&comment.Commenter.Id,
			&comment.Commenter.Name,
			&comment.Commenter.ProfileUrl,
		)
		if err != nil {
			return nil, err
		}
		comments = append(comments, comment)
	}
	return comments, nil
}
