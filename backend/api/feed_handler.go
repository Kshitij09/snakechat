package api

import (
	"encoding/json"
	"errors"
	"github.com/Kshitij09/snakechat_server/data"
	"github.com/Kshitij09/snakechat_server/data/model"
	"github.com/Kshitij09/snakechat_server/util"
	"net/http"
)

type TrendingFeedRequest struct {
	Offset string `json:"offset"`
}

type TrendingFeedResponse struct {
	PageSize int        `json:"page_size"`
	Posts    []FeedPost `json:"posts"`
	Offset   string     `json:"offset,omitempty"`
}

type FeedPost struct {
	Id        string       `json:"id"`
	Caption   string       `json:"caption"`
	MediaUrl  string       `json:"media_url"`
	CreatedAt int64        `json:"created_at"`
	Likes     int64        `json:"likes"`
	Shares    int64        `json:"shares"`
	Saves     int64        `json:"saves"`
	Downloads int64        `json:"downloads"`
	User      FeedUserMeta `json:"user"`
}

type FeedUserMeta struct {
	Id         string `json:"id"`
	Name       string `json:"name"`
	ProfileUrl string `json:"profile_url"`
}

func (s *Server) handleGetTrendingFeed(w http.ResponseWriter, r *http.Request) error {
	feed, dbErr := s.fetchFeedFromDatabase(r)
	if dbErr != nil {
		if errors.Is(dbErr, data.ErrInvalidFeedOffset) {
			return invalidOffsetError()
		}
		return dbErr
	}
	resp := TrendingFeedResponse{PageSize: len(feed.Posts), Posts: toApiFeedPosts(feed.Posts), Offset: feed.Offset}
	return WriteSuccessJson(w, resp)
}

func (s *Server) fetchFeedFromDatabase(r *http.Request) (*model.Feed, error) {
	var (
		feed  *model.Feed
		dbErr error
	)
	if r.Body == http.NoBody {
		feed, dbErr = s.db.Feed.GetFirstTrendingFeed()
	} else {
		req := TrendingFeedRequest{}
		if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
			return nil, util.SimpleAPIError(http.StatusBadRequest, "Invalid request body")
		}
		if req.Offset == "" {
			feed, dbErr = s.db.Feed.GetFirstTrendingFeed()
		} else {
			feed, dbErr = s.db.Feed.GetTrendingFeed(req.Offset)
		}
	}
	return feed, dbErr
}

func toApiFeedPosts(posts []model.Post) []FeedPost {
	feedPosts := make([]FeedPost, 0, len(posts))
	for _, post := range posts {
		user := FeedUserMeta{
			Id:         post.User.Id,
			Name:       post.User.Name,
			ProfileUrl: post.User.ProfileUrl,
		}
		feedPost := FeedPost{
			Id:        post.Id,
			Caption:   post.Caption,
			MediaUrl:  post.MediaUrl,
			CreatedAt: post.Likes,
			Likes:     post.Likes,
			Shares:    post.Shares,
			Saves:     post.Saves,
			Downloads: post.Downloads,
			User:      user,
		}
		feedPosts = append(feedPosts, feedPost)
	}
	return feedPosts
}

func invalidOffsetError() error {
	return &util.APIError{
		StatusCode: http.StatusBadRequest,
		Errors: []map[string]any{
			{"offset": "Invalid Offset"},
		},
	}
}
