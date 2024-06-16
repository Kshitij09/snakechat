package api

import (
	"encoding/json"
	"github.com/Kshitij09/snakechat_server/data/model"
	"github.com/Kshitij09/snakechat_server/util"
	"net/http"
	"reflect"
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
	Id        string
	Caption   string
	MediaUrl  string
	CreatedAt int64
	Likes     int64
	Shares    int64
	Saves     int64
	Downloads int64
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
		if reflect.ValueOf(req.Offset).IsZero() {
			feed, dbErr = s.db.Feed.GetFirstTrendingFeed()
		} else {
			feed, dbErr = s.db.Feed.GetTrendingFeed(req.Offset)
		}
	}
	if dbErr != nil {
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
		feedPost := FeedPost{
			Id:        post.Id,
			Caption:   post.Caption,
			MediaUrl:  post.MediaUrl,
			CreatedAt: post.Likes,
			Likes:     post.Likes,
			Shares:    post.Shares,
			Saves:     post.Saves,
			Downloads: post.Downloads,
		}
		feedPosts = append(feedPosts, feedPost)
	}
	return feedPosts
}
