package api

import (
	"github.com/Kshitij09/snakechat_server/data/model"
	"net/http"
)

type TagsResponse struct {
	Tags []Tag `json:"tags"`
}

type Tag struct {
	Id        string `json:"id"`
	Title     string `json:"title"`
	CreatedAt int64  `json:"created_at"`
}

func (s *Server) handleGetTrendingTags(w http.ResponseWriter, _ *http.Request) error {
	dbTags, err := s.db.Tags.GetTrendingTags(5)
	if err != nil {
		return err
	}
	resp := TagsResponse{Tags: toApiTags(dbTags)}
	return WriteSuccessJson(w, resp)
}

func toApiTags(dbTags []model.Tag) []Tag {
	tags := make([]Tag, 0, len(dbTags))
	for _, dbTag := range dbTags {
		tag := Tag{Id: dbTag.Id, Title: dbTag.Title, CreatedAt: dbTag.CreatedAt}
		tags = append(tags, tag)
	}
	return tags
}
