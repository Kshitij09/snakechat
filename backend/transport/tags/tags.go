package tags

import (
	"database/sql"
	"github.com/Kshitij09/snakechat_server/snakechat"
	"github.com/Kshitij09/snakechat_server/sqlite"
	"github.com/Kshitij09/snakechat_server/transport"
	"github.com/Kshitij09/snakechat_server/transport/writer"
	"net/http"
)

type Response struct {
	Tags []Tag `json:"tags"`
}

type Tag struct {
	Id        string `json:"id"`
	Title     string `json:"title"`
	CreatedAt int64  `json:"created_at"`
}

type API struct {
	tagService snakechat.TagService
}

func TrendingTagsHandler(db *sql.DB) transport.Handler {
	storage := sqlite.NewTagsStorage(db)
	service := snakechat.NewTagService(storage)
	return func(w http.ResponseWriter, _ *http.Request) error {
		tags, err := service.Trending(5)
		if err != nil {
			return err
		}
		resp := Response{Tags: toTransport(tags)}
		return writer.SuccessJson(w, resp)
	}
}

func toTransport(dbTags []snakechat.Tag) []Tag {
	tags := make([]Tag, 0, len(dbTags))
	for _, dbTag := range dbTags {
		tag := Tag{Id: dbTag.Id, Title: dbTag.Title, CreatedAt: dbTag.CreatedAt}
		tags = append(tags, tag)
	}
	return tags
}
