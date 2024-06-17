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
	var feed *model.Feed
	var dbErr error
	if r.Body == http.NoBody {
		feed, dbErr = s.db.Feed.GetFirstTrendingFeed()
	} else {
		req := TrendingFeedRequest{}
		if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
			return err
		}
		if req.Offset == "" {
			feed, dbErr = s.db.Feed.GetFirstTrendingFeed()
		} else {
			feed, dbErr = s.db.Feed.GetTrendingFeed(req.Offset)
		}
	}
	if dbErr != nil {
		if errors.Is(dbErr, data.ErrInvalidFeedOffset) {
			return &util.APIError{
				StatusCode: http.StatusBadRequest,
				Errors: []map[string]any{
					{"offset": "Invalid Offset"},
				},
			}
		}
		return dbErr
	}
	if feed == nil {
		return util.SimpleAPIError(http.StatusNotFound, "Feed not found for given parameters")
	}
	resp := TrendingFeedResponse{PageSize: len(feed.Posts), Posts: toApiFeedPosts(feed.Posts), Offset: feed.Offset}
	return WriteSuccessJson(w, resp)
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
