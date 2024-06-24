package domain

import (
	"errors"
	"fmt"
	"strconv"
	"strings"
)

type UserCredentials struct {
	UserId string
}

var ErrInvalidDeviceId = errors.New("invalid device id")
var ErrUserNotFound = errors.New("user not found")

type UserCredentialsService struct {
	users UserDao
}

func NewUserCredentialsService(users UserDao) *UserCredentialsService {
	return &UserCredentialsService{users: users}
}

func (s UserCredentialsService) GetOrCreateUser(deviceId string) (*UserCredentials, error) {
	if deviceId == "" {
		return nil, ErrInvalidDeviceId
	}
	userId, err := s.users.UserIdByDevice(deviceId)
	if errors.Is(err, ErrUserNotFound) {
		lastGuestUserId, err := s.users.LastGuestUserId()
		if !errors.Is(err, ErrUserNotFound) && err != nil {
			return nil, wrapCreateGuestError("", err)
		}
		lastGuestUserIdSeq := guestSequenceOrZero(lastGuestUserId)
		newGuestUserId := guestUserIdFromSequence(lastGuestUserIdSeq + 1)
		err = s.users.CreateGuest(newGuestUserId)
		if err != nil {
			return nil, wrapCreateGuestError(newGuestUserId, err)
		}
		err = s.users.AddDeviceMapping(newGuestUserId, deviceId)
		if err != nil {
			return nil, wrapCreateGuestError(newGuestUserId, err)
		}
		return &UserCredentials{UserId: newGuestUserId}, nil
	}
	return &UserCredentials{UserId: *userId}, nil
}

func wrapCreateGuestError(newUserId string, error error) error {
	return fmt.Errorf("error creating guest user '%s': %w", newUserId, error)
}

func guestSequenceOrZero(userId *string) int64 {
	if userId == nil {
		return 0
	}
	seqStr, _ := strings.CutPrefix(*userId, "guest_")
	if seqStr == "" {
		return 0
	}
	seq, err := strconv.ParseInt(seqStr, 10, 64)
	if err != nil {
		return 0
	}
	return seq
}

func guestUserIdFromSequence(seq int64) string {
	return fmt.Sprintf("guest_%05d", seq)
}
