package domain

import (
	"fmt"
	"github.com/Kshitij09/snakechat_server/domain/paging"
)

const LikersPageSize = 40

type Liker struct {
	Id             string
	Name           string
	ProfileUrl     string
	FollowersCount int64
	UpdatedAt      int64
}

func (l Liker) OffsetKey() int64 {
	return l.UpdatedAt
}

type LikersDao interface {
	PostLikers(postId string) ([]Liker, error)
	PostLikersUpdatedBefore(postId string, updateTimestamp int64) ([]Liker, error)
	CommentLikers(commentId string) ([]Liker, error)
	CommentLikersUpdatedBefore(commentId string, updateTimestamp int64) ([]Liker, error)
}

type LikersService struct {
	likers LikersDao
}

func NewLikersService(likers LikersDao) *LikersService {
	return &LikersService{likers: likers}
}

func (s *LikersService) PostLikers(postId string, offset *string) (*paging.Page[int64, Liker], error) {
	fetcher := paging.Fetcher[int64, Liker]{
		ById:          s.likers.PostLikers,
		ByIdAndOffset: s.likers.PostLikersUpdatedBefore,
		OffsetConv:    timestampConverter[Liker]{},
	}
	likers, err := fetcher.FetchPage(postId, offset)
	if err != nil {
		return nil, fmt.Errorf("PostLikers: %w", err)
	}
	return likers, nil
}

func (s *LikersService) CommentLikers(commentId string, offset *string) (*paging.Page[int64, Liker], error) {
	fetcher := paging.Fetcher[int64, Liker]{
		ById:          s.likers.CommentLikers,
		ByIdAndOffset: s.likers.CommentLikersUpdatedBefore,
		OffsetConv:    timestampConverter[Liker]{},
		PageSize:      LikersPageSize,
	}
	likers, err := fetcher.FetchPage(commentId, offset)
	if err != nil {
		return nil, fmt.Errorf("CommentLikers: %w", err)
	}
	return likers, nil
}
