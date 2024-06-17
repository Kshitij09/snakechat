package data

import (
	"bytes"
	"database/sql"
	"encoding/base64"
	"errors"
	"fmt"
	"github.com/Kshitij09/snakechat_server/data/model"
	"log"
	"strconv"
)

const feedSize = 10

type feedSqlContext struct {
	db          *sql.DB
	queryRunner feedQueryRunner
}

func (ctx *feedSqlContext) GetTrendingFeed(offset string) (*model.Feed, error) {
	return ctx.getTrendingFeed(&offset)
}

func (ctx *feedSqlContext) GetFirstTrendingFeed() (*model.Feed, error) {
	return ctx.getTrendingFeed(nil)
}

var ErrInvalidFeedOffset = errors.New("invalid offset")

func (ctx *feedSqlContext) getTrendingFeed(offset *string) (*model.Feed, error) {
	var decodedOffset string
	if offset != nil && len(*offset) > 0 {
		offsetBytes, err := base64.StdEncoding.DecodeString(*offset)
		if err != nil {
			log.Printf("error decoding the offset: %s", err)
			return nil, ErrInvalidFeedOffset
		}
		decodedOffset = string(offsetBytes)
	}
	var (
		queryBuffer bytes.Buffer
		rank        *int
	)
	if len(decodedOffset) > 0 {
		var parseRankErr error
		r, err := strconv.Atoi(decodedOffset)
		if err != nil {
			return nil, ErrInvalidFeedOffset
		}
		rank = &r
		if err := trendingFeedQuery.Execute(&queryBuffer, parseRankErr != nil); err != nil {
			return nil, fmt.Errorf("error parsing the feed query, %w", err)
		}
	} else {
		if err := trendingFeedQuery.Execute(&queryBuffer, false); err != nil {
			return nil, fmt.Errorf("error parsing the feed query, %w", err)
		}
	}
	resultRows, queryErr := ctx.queryRunner.getFeed(rank, feedSize)
	defer resultRows.Close()
	if queryErr != nil {
		return nil, fmt.Errorf("error getting trending feed: %w", queryErr)
	}
	posts := make([]model.Post, 0, feedSize)
	for resultRows.Next() {
		var post model.Post
		post, err := ctx.queryRunner.scanFeedRow(resultRows)
		if err != nil {
			return nil, fmt.Errorf("error parsing trending feed row: %w", err)
		}
		posts = append(posts, post)
	}
	var nextOffset string
	if len(posts) > 0 {
		lastPost := posts[len(posts)-1]
		nextOffset = strconv.FormatInt(lastPost.Rank, 10)
		nextOffset = base64.StdEncoding.EncodeToString([]byte(nextOffset))
	}
	return &model.Feed{Posts: posts, Offset: nextOffset}, nil
}
