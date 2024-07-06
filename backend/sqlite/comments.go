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
		SELECT c.id, c.updated_at, c.text, u.id, u.name, u.profile_url, count(cl.user_id) as likes
		FROM comments c
		INNER JOIN users u ON c.user_id = u.id
		INNER JOIN comment_likes cl ON c.id = cl.id
		WHERE c.post_id = ?
		GROUP BY c.id
		ORDER BY likes desc, c.updated_at desc
		LIMIT ?
	`
	comments, err := ctx.queryPostComments(query, postId, domain.CommentsPageSize)
	if err != nil {
		return nil, fmt.Errorf("sqlite.PostComments: %w", err)
	}
	return comments, nil
}

func (ctx CommentsStorage) PostCommentsByOffset(postId string, offset domain.CommentsOffset) ([]domain.Comment, error) {
	query := `
		SELECT c.id, c.updated_at, c.text, u.id, u.name, u.profile_url, count(cl.user_id) as likes
		FROM comments c
		INNER JOIN users u ON c.user_id = u.id
		INNER JOIN comment_likes cl ON c.id = cl.id
		WHERE c.post_id = :post_id
		GROUP BY c.id
		HAVING (likes = :likes and c.updated_at < :updated_at) or likes < :likes
		ORDER BY likes desc, c.updated_at desc
		LIMIT :limit
	`
	comments, err := ctx.queryPostComments(
		query,
		sql.Named("post_id", postId),
		sql.Named("likes", offset.Likes),
		sql.Named("updated_at", offset.UpdateTimestamp),
		sql.Named("limit", domain.CommentsPageSize),
	)
	if err != nil {
		return nil, fmt.Errorf("sqlite.PostCommentsByOffset: %w", err)
	}
	return comments, nil
}

func (ctx CommentsStorage) CommentReplies(commentId string) ([]domain.Comment, error) {
	query := `
		SELECT c.id, c.updated_at, c.text, u.id, u.name, u.profile_url, count(cl.user_id) as likes
		FROM comments c
		INNER JOIN users u ON c.user_id = u.id
		INNER JOIN comment_likes cl ON c.id = cl.id
		WHERE c.comment_id = ?
		GROUP BY c.id
		ORDER BY likes desc, c.updated_at desc
		LIMIT ?
	`
	comments, err := ctx.queryPostComments(query, commentId, domain.CommentsPageSize)
	if err != nil {
		return nil, fmt.Errorf("sqlite.CommentReplies: %w", err)
	}
	return comments, nil
}

func (ctx CommentsStorage) CommentRepliesByOffset(commentId string, offset domain.CommentsOffset) ([]domain.Comment, error) {
	query := `
		SELECT c.id, c.updated_at, c.text, u.id, u.name, u.profile_url, count(cl.user_id) as likes
		FROM comments c
		INNER JOIN users u ON c.user_id = u.id
		INNER JOIN comment_likes cl ON c.id = cl.id
		WHERE c.comment_id = :comment_id
		GROUP BY c.id
		HAVING (likes = :likes and c.updated_at < :updated_at) or likes < :likes
		ORDER BY likes desc, c.updated_at desc
		LIMIT :limit
	`
	comments, err := ctx.queryPostComments(
		query,
		sql.Named("comment_id", commentId),
		sql.Named("updated_at", offset.UpdateTimestamp),
		sql.Named("likes", offset.Likes),
		sql.Named("limit", domain.CommentsPageSize),
	)
	if err != nil {
		return nil, fmt.Errorf("sqlite.CommentRepliesByOffset: %w", err)
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
			&comment.Likes,
		)
		if err != nil {
			return nil, err
		}
		comments = append(comments, comment)
	}
	return comments, nil
}
