package domain

import (
	"fmt"
	"github.com/Kshitij09/snakechat_server/domain/paging"
)

const FollowPageSize = 20

type FollowUser struct {
	Id         string
	Name       string
	ProfileUrl *string
	UpdatedAt  int64
}

func (f FollowUser) OffsetKey() int64 {
	return f.UpdatedAt
}

type FollowDao interface {
	Followers(userId string) ([]FollowUser, error)
	FollowersUpdatedBefore(userId string, updateTimestamp int64) ([]FollowUser, error)
	Following(userId string) ([]FollowUser, error)
	FollowingUpdatedBefore(userId string, updateTimestamp int64) ([]FollowUser, error)
}

type FollowService struct {
	followers FollowDao
}

func NewFollowService(followers FollowDao) *FollowService {
	return &FollowService{followers: followers}
}

func (s *FollowService) Followers(userId string, offset *string) (*paging.Page[int64, FollowUser], error) {
	fetcher := paging.Fetcher[int64, FollowUser]{
		ById:          s.followers.Followers,
		ByIdAndOffset: s.followers.FollowersUpdatedBefore,
		OffsetConv:    timestampConverter[FollowUser]{},
		PageSize:      FollowPageSize,
	}
	followers, err := fetcher.FetchPage(userId, offset)
	if err != nil {
		return nil, fmt.Errorf("followers: %w", err)
	}
	return followers, nil
}

func (s *FollowService) Following(userId string, offset *string) (*paging.Page[int64, FollowUser], error) {
	fetcher := paging.Fetcher[int64, FollowUser]{
		ById:          s.followers.Following,
		ByIdAndOffset: s.followers.FollowingUpdatedBefore,
		OffsetConv:    timestampConverter[FollowUser]{},
		PageSize:      FollowPageSize,
	}
	followers, err := fetcher.FetchPage(userId, offset)
	if err != nil {
		return nil, fmt.Errorf("following: %w", err)
	}
	return followers, nil
}
