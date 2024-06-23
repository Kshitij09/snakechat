package data

import (
	"database/sql"
	"errors"
	"fmt"
	"github.com/Kshitij09/snakechat_server/data/model"
	"strconv"
	"strings"
	"time"
)

type guestUserIdHandler struct{}

func (h guestUserIdHandler) GetUserId(seq int) string {
	return fmt.Sprintf("guest_%05d", seq)
}

func (h guestUserIdHandler) ExtractSequence(seq string) (int, error) {
	if seq == "" {
		return 0, nil
	}
	userId, _ := strings.CutPrefix(seq, "guest_")
	return strconv.Atoi(userId)
}

type userSqlContext struct {
	db *sql.DB
	guestUserIdHandler
}

var ErrInvalidDeviceId = errors.New("invalid device id")

func (ctx *userSqlContext) GetOrCreateUser(deviceId string) (*model.UserCredentials, error) {
	if deviceId == "" {
		return nil, ErrInvalidDeviceId
	}
	query := `
	SELECT user_id FROM user_devices
	WHERE device_id = ?
	ORDER BY updated_at DESC
	LIMIT 1
	`
	row := ctx.db.QueryRow(query, deviceId)
	var userId string
	err := row.Scan(&userId)
	if errors.Is(err, sql.ErrNoRows) {
		row = ctx.db.QueryRow("SELECT id FROM users WHERE is_guest = TRUE ORDER BY id DESC LIMIT 1")
		var lastGuestUserId string
		err := row.Scan(&lastGuestUserId)
		if !errors.Is(err, sql.ErrNoRows) && err != nil {
			return nil, wrapCreateGuestError("", err)
		}
		lastGuestUserIdSeq, err := ctx.guestUserIdHandler.ExtractSequence(lastGuestUserId)
		if err != nil {
			lastGuestUserIdSeq = 0
		}
		newGuestUserId := ctx.guestUserIdHandler.GetUserId(lastGuestUserIdSeq + 1)
		_, err = ctx.db.Exec("INSERT INTO users (id, name, created_at, is_guest) VALUES (?, ?, ?, ?)", newGuestUserId, newGuestUserId, time.Now().UnixMilli(), true)
		if err != nil {
			return nil, wrapCreateGuestError(newGuestUserId, err)
		}
		_, err = ctx.db.Exec("INSERT INTO user_devices (user_id, device_id, updated_at) VALUES (?, ?, ?)", newGuestUserId, deviceId, time.Now().UnixMilli())
		if err != nil {
			return nil, wrapCreateGuestError(newGuestUserId, err)
		}
		return &model.UserCredentials{UserId: newGuestUserId}, nil
	}
	if err != nil {
		return nil, err
	}
	return &model.UserCredentials{UserId: userId}, nil
}

func wrapCreateGuestError(newUserId string, error error) error {
	return fmt.Errorf("error creating guest user '%s': %w", newUserId, error)
}
