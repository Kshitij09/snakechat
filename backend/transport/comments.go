package transport

import (
	"database/sql"
	"errors"
	"github.com/Kshitij09/snakechat_server/domain"
	"github.com/Kshitij09/snakechat_server/sqlite"
	"github.com/Kshitij09/snakechat_server/transport/apierror"
	"github.com/Kshitij09/snakechat_server/transport/handlers"
	"github.com/Kshitij09/snakechat_server/transport/writer"
	"net/http"
)

type CommentsResponse struct {
	Total    int       `json:"total"`
	Comments []Comment `json:"comments"`
	Offset   *string   `json:"offset"`
}

type Comment struct {
	Id        string    `json:"id"`
	UpdatedAt int64     `json:"updated_at"`
	Text      string    `json:"text"`
	Commenter Commenter `json:"commenter"`
}

type Commenter struct {
	Id         string `json:"id"`
	Name       string `json:"name"`
	ProfileUrl string `json:"profile_url"`
}

func PostCommentsHandler(db *sql.DB) handlers.Handler {
	storage := sqlite.NewCommentsStorage(db)
	service := domain.NewCommentsService(storage)
	return func(w http.ResponseWriter, r *http.Request) error {
		postId := r.PathValue("id")
		commentsPage, err := service.PostComments(postId, nil)
		if err != nil {
			if errors.Is(err, domain.ErrInvalidOffset) {
				return apierror.SimpleAPIError(http.StatusBadRequest, "Invalid offset")
			}
			return err
		}
		resp := CommentsResponse{
			Total:    commentsPage.Total,
			Comments: toTransportPostComments(commentsPage.Items),
			Offset:   commentsPage.Offset,
		}
		return writer.SuccessJson(w, resp)
	}
}

func toTransportPostComments(comments []domain.Comment) []Comment {
	transportComments := make([]Comment, 0, len(comments))
	for _, comment := range comments {
		tc := Comment{
			Id:        comment.Id,
			UpdatedAt: comment.UpdatedAt,
			Text:      comment.Text,
			Commenter: Commenter{
				Id:         comment.Commenter.Id,
				Name:       comment.Commenter.Name,
				ProfileUrl: comment.Commenter.ProfileUrl,
			},
		}
		transportComments = append(transportComments, tc)
	}
	return transportComments
}
