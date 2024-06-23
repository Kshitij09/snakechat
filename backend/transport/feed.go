package transport

import (
	"database/sql"
	"encoding/json"
	"errors"
	"github.com/Kshitij09/snakechat_server/snakechat"
	"github.com/Kshitij09/snakechat_server/sqlite"
	"github.com/Kshitij09/snakechat_server/transport/handlers"
	"github.com/Kshitij09/snakechat_server/transport/writer"
	"github.com/Kshitij09/snakechat_server/util"
	"net/http"
)

type TrendingFeedRequest struct {
	Offset string `json:"offset"`
}

type TrendingFeedResponse struct {
	PageSize int    `json:"page_size"`
	Posts    []Post `json:"posts"`
	Offset   string `json:"offset,omitempty"`
}

type Post struct {
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

func TrendingFeedHandler(db *sql.DB) handlers.Handler {
	storage := sqlite.NewPostStorage(db)
	service := snakechat.NewFeedService(storage)
	return func(w http.ResponseWriter, r *http.Request) error {
		feed, err := fetchFeed(service, r)
		if err != nil {
			if errors.Is(err, snakechat.ErrInvalidFeedOffset) {
				return invalidOffsetError()
			}
			return err
		}
		resp := TrendingFeedResponse{PageSize: len(feed.Posts), Posts: toTransportPosts(feed.Posts), Offset: feed.Offset}
		return writer.SuccessJson(w, resp)
	}
}

func fetchFeed(service snakechat.FeedService, r *http.Request) (*snakechat.Feed, error) {
	var (
		feed  *snakechat.Feed
		dbErr error
	)
	if r.Body == http.NoBody {
		feed, dbErr = service.TrendingFeed()
	} else {
		req := TrendingFeedRequest{}
		if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
			return nil, util.SimpleAPIError(http.StatusBadRequest, "Invalid request body")
		}
		if req.Offset == "" {
			feed, dbErr = service.TrendingFeed()
		} else {
			feed, dbErr = service.TrendingFeedByOffset(req.Offset)
		}
	}
	return feed, dbErr
}

func toTransportPosts(posts []snakechat.Post) []Post {
	feedPosts := make([]Post, 0, len(posts))
	for _, post := range posts {
		user := FeedUserMeta{
			Id:         post.User.Id,
			Name:       post.User.Name,
			ProfileUrl: post.User.ProfileUrl,
		}
		feedPost := Post{
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
