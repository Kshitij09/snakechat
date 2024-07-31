package sqlite

import (
	"database/sql"
	"errors"
	"fmt"
	"github.com/Kshitij09/snakechat_server/domain"
	"time"
)

type UserStorage struct {
	db *sql.DB
}

func NewUserStorage(db *sql.DB) UserStorage {
	return UserStorage{db: db}
}

func (ctx UserStorage) UserIdByDevice(deviceId string) (*string, error) {
	query := `
	SELECT user_id FROM user_devices
	WHERE device_id = ?
	ORDER BY updated_at DESC
	LIMIT 1
	`
	var userId string
	row := ctx.db.QueryRow(query, deviceId)
	err := row.Scan(&userId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, domain.ErrUserNotFound
		}
		return nil, err
	}
	return &userId, nil
}

func (ctx UserStorage) LastGuestUserId() (*string, error) {
	row := ctx.db.QueryRow("SELECT id FROM users WHERE is_guest = TRUE ORDER BY id DESC LIMIT 1")
	var userId string
	err := row.Scan(&userId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, domain.ErrUserNotFound
		}
		return nil, err
	}
	return &userId, nil
}

func (ctx UserStorage) CreateGuest(userId string) error {
	query := "INSERT INTO users (id, name, created_at, is_guest) VALUES (?, ?, ?, ?)"
	_, err := ctx.db.Exec(query, userId, userId, time.Now().UnixMilli(), true)
	return err
}

func (ctx UserStorage) AddDeviceMapping(userId string, deviceId string) error {
	query := "INSERT INTO user_devices (user_id, device_id, updated_at) VALUES (?, ?, ?)"
	_, err := ctx.db.Exec(query, userId, deviceId, time.Now().UnixMilli())
	return err
}

func (ctx UserStorage) UserById(userId string) (*domain.User, error) {
	query := `
		SELECT id, name, status, profile_url, 
		IFNULL(followers_count, 0),
		IFNULL(following_count, 0),
		IFNULL(posts_count, 0)
		FROM users WHERE id = ? 
		LIMIT 1
	`
	row := ctx.db.QueryRow(query, userId)
	var user domain.User
	err := row.Scan(
		&user.Id,
		&user.Name,
		&user.Status,
		&user.ProfileUrl,
		&user.FollowersCount,
		&user.FollowingCount,
		&user.PostsCount,
	)
	if errors.Is(err, sql.ErrNoRows) {
		return nil, domain.ErrUserNotFound
	}
	return &user, err
}

func (ctx UserStorage) UserPostThumbnails(userId string) ([]domain.UserPostThumbnail, error) {
	query := "SELECT id, media_url FROM posts WHERE user_id = ?"
	rs, err := ctx.db.Query(query, userId)
	if err != nil {
		return nil, fmt.Errorf("UserPostThumbnails: %w", err)
	}
	defer rs.Close()
	return scanUserPostThumbnails(rs)
}

func scanUserPostThumbnails(rs *sql.Rows) ([]domain.UserPostThumbnail, error) {
	thumbs := make([]domain.UserPostThumbnail, 0)
	for rs.Next() {
		var thumbnail domain.UserPostThumbnail
		err := rs.Scan(&thumbnail.PostId, &thumbnail.ThumbnailUrl)
		if err != nil {
			return nil, fmt.Errorf("scanUserPostThumbnails: %w", err)
		}
		thumbs = append(thumbs, thumbnail)
	}
	return thumbs, nil
}
