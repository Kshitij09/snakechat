package api

import (
	"github.com/Kshitij09/snakechat_server/data/model"
	"net/http"
)

type TrendingFeedResponse struct {
	Posts  []FeedPost `json:"posts"`
	Offset string     `json:"offset"`
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

func (s *Server) handleGetTrendingFeed(w http.ResponseWriter, _ *http.Request) error {
	feed, err := s.db.Feed.GetFirstTrendingFeed()
	if err != nil {
		return err
	}
	resp := TrendingFeedResponse{Posts: toApiFeedPosts(feed.Posts), Offset: feed.Offset}
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
