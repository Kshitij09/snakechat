package sqlite

import (
	"database/sql"
	"fmt"
	"github.com/Kshitij09/snakechat_server/domain"
)

type FollowsStorage struct {
	db *sql.DB
}

func NewFollowsStorage(db *sql.DB) *FollowsStorage {
	return &FollowsStorage{db}
}

func (ctx FollowsStorage) Followers(userId string) ([]domain.FollowUser, error) {
	query := `
		SELECT  u.id, u.name, u.profile_url, f.updated_at
		FROM followers f INNER JOIN users u ON f.follower_id = u.id
		WHERE user_id = ?
		ORDER BY updated_at DESC
		LIMIT ?
	`
	likers, err := ctx.queryFollows(query, userId, domain.FollowPageSize)
	if err != nil {
		return nil, fmt.Errorf("FollowersStorage: %w", err)
	}
	return likers, nil
}

func (ctx FollowsStorage) FollowersUpdatedBefore(userId string, updateTimestamp int64) ([]domain.FollowUser, error) {
	query := `
		SELECT  u.id, u.name, u.profile_url, f.updated_at
		FROM followers f INNER JOIN users u ON f.follower_id = u.id
		WHERE user_id = ?
		AND updated_at < ?
		ORDER BY updated_at DESC
		LIMIT ?
	`
	likers, err := ctx.queryFollows(query, userId, updateTimestamp, domain.FollowPageSize)
	if err != nil {
		return nil, fmt.Errorf("FollowersStorage: %w", err)
	}
	return likers, nil
}

func (ctx FollowsStorage) Following(userId string) ([]domain.FollowUser, error) {
	query := `
		SELECT  u.id, u.name, u.profile_url, f.updated_at
		FROM followers f INNER JOIN users u ON f.user_id = u.id
		WHERE follower_id = ?
		ORDER BY updated_at DESC
		LIMIT ?
	`
	likers, err := ctx.queryFollows(query, userId, domain.FollowPageSize)
	if err != nil {
		return nil, fmt.Errorf("FollowersStorage: %w", err)
	}
	return likers, nil
}

func (ctx FollowsStorage) FollowingUpdatedBefore(userId string, updateTimestamp int64) ([]domain.FollowUser, error) {
	query := `
		SELECT  u.id, u.name, u.profile_url, f.updated_at
		FROM followers f INNER JOIN users u ON f.user_id = u.id
		WHERE follower_id = ?
		AND updated_at < ?
		ORDER BY updated_at DESC
		LIMIT ?
	`
	likers, err := ctx.queryFollows(query, userId, updateTimestamp, domain.FollowPageSize)
	if err != nil {
		return nil, fmt.Errorf("FollowersStorage: %w", err)
	}
	return likers, nil
}

func (ctx FollowsStorage) queryFollows(query string, args ...any) ([]domain.FollowUser, error) {
	rs, err := ctx.db.Query(query, args...)
	if err != nil {
		return nil, err
	}
	defer rs.Close()
	return scanFollowUser(rs)
}

func scanFollowUser(rows *sql.Rows) ([]domain.FollowUser, error) {
	follows := make([]domain.FollowUser, 0)
	for rows.Next() {
		var user domain.FollowUser
		err := rows.Scan(
			&user.Id,
			&user.Name,
			&user.ProfileUrl,
			&user.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}
		follows = append(follows, user)
	}
	return follows, nil
}
