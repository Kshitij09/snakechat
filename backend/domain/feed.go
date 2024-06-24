package domain

import (
	"encoding/base64"
	"errors"
	"log"
	"strconv"
)

const FeedPageSize = 10

type Feed struct {
	Posts  []Post
	Offset string
}

type Post struct {
	Id        string
	Caption   string
	MediaUrl  string
	CreatedAt int64
	Likes     int64
	Shares    int64
	Saves     int64
	Downloads int64
	Rank      int64
	TagId     string
	User      PostUserMeta
}

type PostUserMeta struct {
	Id         string
	Name       string
	ProfileUrl string
}

type PostDao interface {
	TrendingPostsBelowRank(rank int) ([]Post, error)
	TrendingPosts() ([]Post, error)
}

type FeedService struct {
	posts PostDao
}

func NewFeedService(posts PostDao) FeedService {
	return FeedService{posts: posts}
}

func (s FeedService) TrendingFeedByOffset(offset string) (*Feed, error) {
	rankFilter, err := parseRankFilter(&offset)
	if err != nil {
		return nil, err
	}
	posts, err := s.posts.TrendingPostsBelowRank(*rankFilter)
	if err != nil {
		return nil, err
	}
	nextOffset := computeOffset(posts)
	return &Feed{Posts: posts, Offset: nextOffset}, nil
}

func (s FeedService) TrendingFeed() (*Feed, error) {
	posts, err := s.posts.TrendingPosts()
	if err != nil {
		return nil, err
	}
	nextOffset := computeOffset(posts)
	return &Feed{Posts: posts, Offset: nextOffset}, nil
}

var ErrInvalidFeedOffset = errors.New("invalid offset")

func parseRankFilter(offset *string) (*int, error) {
	var decodedOffset string
	if offset != nil && len(*offset) > 0 {
		offsetBytes, err := base64.StdEncoding.DecodeString(*offset)
		if err != nil {
			log.Printf("error decoding the offset: %s", err)
			return nil, ErrInvalidFeedOffset
		}
		decodedOffset = string(offsetBytes)
	}
	var rank *int
	if len(decodedOffset) > 0 {
		r, err := strconv.Atoi(decodedOffset)
		if err != nil {
			return nil, ErrInvalidFeedOffset
		}
		rank = &r
	}
	return rank, nil
}

func computeOffset(posts []Post) string {
	var nextOffset string
	if len(posts) > 0 {
		lastPost := posts[len(posts)-1]
		nextOffset = strconv.FormatInt(lastPost.Rank, 10)
		nextOffset = base64.StdEncoding.EncodeToString([]byte(nextOffset))
	}
	return nextOffset
}
