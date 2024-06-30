package domain

import (
	"encoding/base64"
	"errors"
	"fmt"
	"log"
	"strconv"
)

const LikersPageSize = 40

var ErrInvalidLikersOffset = errors.New("invalid offset")

type LikersPage struct {
	Offset *string
	Likers []Liker
}

type Liker struct {
	Id             string
	Name           string
	ProfileUrl     string
	FollowersCount int64
	UpdatedAt      int64
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

func (s *LikersService) PostLikers(postId string, offset *string) (*LikersPage, error) {
	fetcher := likersFetcher{
		byId:          s.likers.PostLikers,
		byIdAndOffset: s.likers.PostLikersUpdatedBefore,
	}
	likers, err := fetcher.fetchPage(postId, offset)
	if err != nil {
		return nil, fmt.Errorf("PostLikers: %w", err)
	}
	return likers, nil
}

func (s *LikersService) CommentLikers(commentId string, offset *string) (*LikersPage, error) {
	fetcher := likersFetcher{
		byId:          s.likers.CommentLikers,
		byIdAndOffset: s.likers.CommentLikersUpdatedBefore,
	}
	likers, err := fetcher.fetchPage(commentId, offset)
	if err != nil {
		return nil, fmt.Errorf("CommentLikers: %w", err)
	}
	return likers, nil
}

type likersFetcher struct {
	byId          likersById
	byIdAndOffset likersByIdAndOffset
}

type likersById func(id string) ([]Liker, error)
type likersByIdAndOffset func(id string, offset int64) ([]Liker, error)

func (fetcher likersFetcher) fetchPage(id string, offset *string) (*LikersPage, error) {
	likers, err := fetcher.fetch(id, offset)
	if err != nil {
		return nil, err
	}
	var nextOffset *string
	if len(likers) == LikersPageSize {
		nextOffset = nextOffsetOrNil(likers)
	}
	return &LikersPage{Likers: likers, Offset: nextOffset}, nil
}

func (fetcher likersFetcher) fetch(id string, offset *string) ([]Liker, error) {
	if offset == nil || len(*offset) == 0 {
		return fetcher.byId(id)
	} else {
		offsetInt, err := parseOffset(*offset)
		if err != nil {
			return nil, err
		}
		return fetcher.byIdAndOffset(id, *offsetInt)
	}
}

func parseOffset(offset string) (*int64, error) {
	decodedBytes, err := base64.StdEncoding.DecodeString(offset)
	if err != nil {
		log.Printf("error decoding offset: %s\n", err)
		return nil, ErrInvalidLikersOffset
	}
	decoded := string(decodedBytes)
	offsetInt, err := strconv.ParseInt(decoded, 10, 64)
	if err != nil {
		log.Printf("error parsing offset: %s\n", err)
		return nil, ErrInvalidLikersOffset
	}
	return &offsetInt, nil
}

func nextOffsetOrNil(likers []Liker) *string {
	if len(likers) == 0 {
		return nil
	}
	last := likers[len(likers)-1]
	updateTimestamp := strconv.FormatInt(last.UpdatedAt, 10)
	encoded := base64.StdEncoding.EncodeToString([]byte(updateTimestamp))
	return &encoded
}
