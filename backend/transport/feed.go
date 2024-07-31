package transport

import (
	"database/sql"
	"encoding/json"
	"errors"
	"github.com/Kshitij09/snakechat_server/domain"
	"github.com/Kshitij09/snakechat_server/sqlite"
	"github.com/Kshitij09/snakechat_server/transport/apierror"
	"github.com/Kshitij09/snakechat_server/transport/handlers"
	"github.com/Kshitij09/snakechat_server/transport/writer"
	"net/http"
)

type TrendingFeedRequest struct {
	Offset string `json:"offset"`
}

type TrendingFeedResponse struct {
	PageSize int     `json:"page_size"`
	Posts    []Post  `json:"posts"`
	Offset   *string `json:"offset,omitempty"`
}

type Post struct {
	Id        string       `json:"id"`
	TagId     string       `json:"tag_id"`
	Caption   *string      `json:"caption"`
	MediaUrl  string       `json:"media_url"`
	CreatedAt int64        `json:"created_at"`
	Comments  int64        `json:"comments"`
	Likes     int64        `json:"likes"`
	Shares    int64        `json:"shares"`
	Views     int64        `json:"views"`
	Saves     int64        `json:"saves"`
	Downloads int64        `json:"downloads"`
	User      FeedUserMeta `json:"user"`
}

type FeedUserMeta struct {
	Id         string  `json:"id"`
	Name       string  `json:"name"`
	ProfileUrl *string `json:"profile_url"`
}

func TrendingFeedHandler(db *sql.DB) handlers.Handler {
	storage := sqlite.NewPostStorage(db)
	service := domain.NewFeedService(storage)
	return func(w http.ResponseWriter, r *http.Request) error {
		feed, err := fetchFeed(service, r)
		if err != nil {
			if errors.Is(err, domain.ErrInvalidFeedOffset) {
				return invalidOffsetError()
			}
			return err
		}
		resp := TrendingFeedResponse{PageSize: len(feed.Posts), Posts: toTransportPosts(feed.Posts), Offset: feed.Offset}
		return writer.SuccessJson(w, resp)
	}
}

func fetchFeed(service domain.FeedService, r *http.Request) (*domain.Feed, error) {
	var (
		feed  *domain.Feed
		dbErr error
	)
	if r.Body == http.NoBody {
		feed, dbErr = service.TrendingFeed()
	} else {
		req := TrendingFeedRequest{}
		if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
			return nil, apierror.SimpleAPIError(http.StatusBadRequest, "Invalid request body")
		}
		if req.Offset == "" {
			feed, dbErr = service.TrendingFeed()
		} else {
			feed, dbErr = service.TrendingFeedByOffset(req.Offset)
		}
	}
	return feed, dbErr
}

func toTransportPosts(posts []domain.Post) []Post {
	feedPosts := make([]Post, 0, len(posts))
	for _, post := range posts {
		user := FeedUserMeta{
			Id:         post.User.Id,
			Name:       post.User.Name,
			ProfileUrl: post.User.ProfileUrl,
		}
		feedPost := Post{
			Id:        post.Id,
			TagId:     post.TagId,
			Caption:   post.Caption,
			MediaUrl:  post.MediaUrl,
			CreatedAt: post.Likes,
			Comments:  post.Comments,
			Likes:     post.Likes,
			Views:     post.Views,
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
	return &apierror.APIError{
		StatusCode: http.StatusBadRequest,
		Errors: []map[string]any{
			{"offset": "Invalid Offset"},
		},
	}
}
