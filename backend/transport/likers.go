package transport

import (
	"database/sql"
	"encoding/json"
	"errors"
	"github.com/Kshitij09/snakechat_server/domain"
	"github.com/Kshitij09/snakechat_server/domain/paging"
	"github.com/Kshitij09/snakechat_server/sqlite"
	"github.com/Kshitij09/snakechat_server/transport/apierror"
	"github.com/Kshitij09/snakechat_server/transport/handlers"
	"github.com/Kshitij09/snakechat_server/transport/writer"
	"net/http"
)

type LikersResponse struct {
	Total  int     `json:"total"`
	Likers []Liker `json:"likers"`
	Offset *string `json:"offset,omitempty"`
}

type LikersRequest struct {
	Offset string `json:"offset"`
}

type Liker struct {
	Id             string `json:"id"`
	Name           string `json:"name"`
	ProfileUrl     string `json:"profile_url"`
	FollowersCount int64  `json:"followers_count"`
}

func PostLikersHandler(db *sql.DB) handlers.Handler {
	storage := sqlite.NewLikersStorage(db)
	service := domain.NewLikersService(storage)
	return genericLikersHandler(service.PostLikers)
}

func CommentLikersHandler(db *sql.DB) handlers.Handler {
	storage := sqlite.NewLikersStorage(db)
	service := domain.NewLikersService(storage)
	return genericLikersHandler(service.CommentLikers)
}

type likersGetter func(id string, offset *string) (*paging.Page[int64, domain.Liker], error)

func genericLikersHandler(likersGetter likersGetter) handlers.Handler {
	return func(w http.ResponseWriter, r *http.Request) error {
		id := r.PathValue("id")
		if id == "" {
			return apierror.SimpleAPIError(http.StatusBadRequest, "id is missing in the path")
		}

		var (
			likersPage *paging.Page[int64, domain.Liker]
			err        error
		)
		if r.Body == http.NoBody {
			likersPage, err = likersGetter(id, nil)
		} else {
			req := LikersRequest{}
			decodeErr := json.NewDecoder(r.Body).Decode(&req)
			if decodeErr != nil {
				return apierror.SimpleAPIError(http.StatusBadRequest, "Invalid request body")
			}
			likersPage, err = likersGetter(id, &req.Offset)
		}
		if err != nil {
			if errors.Is(err, domain.ErrInvalidOffset) {
				return apierror.SimpleAPIError(http.StatusBadRequest, "Invalid offset")
			}
			return err
		}
		resp := LikersResponse{
			Total:  len(likersPage.Items),
			Likers: toTransportLikers(likersPage.Items),
			Offset: likersPage.Offset,
		}
		return writer.SuccessJson(w, resp)
	}
}

func toTransportLikers(likers []domain.Liker) []Liker {
	likersTransport := make([]Liker, 0, len(likers))
	for _, liker := range likers {
		lt := Liker{
			Id:             liker.Id,
			Name:           liker.Name,
			ProfileUrl:     liker.ProfileUrl,
			FollowersCount: liker.FollowersCount,
		}
		likersTransport = append(likersTransport, lt)
	}
	return likersTransport
}
