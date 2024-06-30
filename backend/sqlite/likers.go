package sqlite

import (
	"database/sql"
	"fmt"
	"github.com/Kshitij09/snakechat_server/domain"
)

const likeEngagementType = 0

type LikersStorage struct {
	db *sql.DB
}

func NewLikersStorage(db *sql.DB) *LikersStorage {
	return &LikersStorage{db}
}

func (ctx LikersStorage) PostLikers(postId string) ([]domain.Liker, error) {
	query := `
		SELECT u.id, u.name, u.profile_url, u.followers_count, pe.updated_at
		FROM post_engagements pe
		INNER JOIN users u ON u.id = pe.user_id
		WHERE pe.type = ?
		AND pe.post_id = ?
		ORDER BY pe.updated_at DESC
		LIMIT ?
	`
	likers, err := ctx.queryLikers(query, likeEngagementType, postId, domain.LikersPageSize)
	if err != nil {
		return nil, fmt.Errorf("PostLikers: %w", err)
	}
	return likers, nil
}

func (ctx LikersStorage) PostLikersUpdatedBefore(postId string, updateTimestamp int64) ([]domain.Liker, error) {
	query := `
		SELECT u.id, u.name, u.profile_url, u.followers_count, pe.updated_at
		FROM post_engagements pe
		INNER JOIN users u ON u.id = pe.user_id
		WHERE pe.type = ?
		AND pe.post_id = ?
		AND pe.updated_at < ?
		ORDER BY pe.updated_at DESC
		LIMIT ?
	`
	likers, err := ctx.queryLikers(query, likeEngagementType, postId, updateTimestamp, domain.LikersPageSize)
	if err != nil {
		return nil, fmt.Errorf("PostLikers: %w", err)
	}
	return likers, nil
}

func (ctx LikersStorage) CommentLikers(commentId string) ([]domain.Liker, error) {
	query := `
		SELECT u.id, u.name, u.profile_url, u.followers_count, cl.updated_at
		FROM comment_likes cl
		INNER JOIN users u ON u.id = cl.user_id
		WHERE cl.id = ?
		ORDER BY cl.updated_at DESC
		LIMIT ?
	`
	likers, err := ctx.queryLikers(query, commentId, domain.LikersPageSize)
	if err != nil {
		return nil, fmt.Errorf("CommentLikers: %w", err)
	}
	return likers, nil
}

func (ctx LikersStorage) CommentLikersUpdatedBefore(commentId string, updateTimestamp int64) ([]domain.Liker, error) {
	query := `
		SELECT u.id, u.name, u.profile_url, u.followers_count, cl.updated_at
		FROM comment_likes cl
		INNER JOIN users u ON u.id = cl.user_id
		WHERE cl.id = ? 
		AND cl.updated_at < ?
		ORDER BY cl.updated_at DESC
		LIMIT ?
	`
	likers, err := ctx.queryLikers(query, commentId, updateTimestamp, domain.LikersPageSize)
	if err != nil {
		return nil, fmt.Errorf("CommentLikers: %w", err)
	}
	return likers, nil
}

func (ctx LikersStorage) queryLikers(query string, args ...any) ([]domain.Liker, error) {
	rs, err := ctx.db.Query(query, args...)
	if err != nil {
		return nil, err
	}
	defer rs.Close()
	return scanLikers(rs)
}

func scanLikers(rows *sql.Rows) ([]domain.Liker, error) {
	likers := make([]domain.Liker, 0)
	for rows.Next() {
		var liker domain.Liker
		err := rows.Scan(
			&liker.Id,
			&liker.Name,
			&liker.ProfileUrl,
			&liker.FollowersCount,
			&liker.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}
		likers = append(likers, liker)
	}
	return likers, nil
}
