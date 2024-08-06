package transport

import (
	"database/sql"
	"encoding/json"
	"errors"
	"github.com/Kshitij09/snakechat_server/domain"
	"github.com/Kshitij09/snakechat_server/domain/offsetconv"
	"github.com/Kshitij09/snakechat_server/domain/paging"
	"github.com/Kshitij09/snakechat_server/sqlite"
	"github.com/Kshitij09/snakechat_server/transport/apierror"
	"github.com/Kshitij09/snakechat_server/transport/handlers"
	"github.com/Kshitij09/snakechat_server/transport/writer"
	"net/http"
)

type FollowUserResponse struct {
	Total   int          `json:"total"`
	Follows []FollowUser `json:"follows"`
	Offset  *string      `json:"offset,omitempty"`
}

type FollowListRequest struct {
	Offset string `json:"offset"`
}

type FollowUser struct {
	Id         string  `json:"id"`
	Name       string  `json:"name"`
	ProfileUrl *string `json:"profile_url,omitempty"`
	UpdatedAt  int64   `json:"updated_at"`
}

func FollowersHandler(db *sql.DB) handlers.Handler {
	storage := sqlite.NewFollowsStorage(db)
	service := domain.NewFollowService(storage)
	return genericFollowersHandler(service.Followers)
}

func FollowingHandler(db *sql.DB) handlers.Handler {
	storage := sqlite.NewFollowsStorage(db)
	service := domain.NewFollowService(storage)
	return genericFollowersHandler(service.Following)
}

type followsGetter func(id string, offset *string) (*paging.Page[int64, domain.FollowUser], error)

func genericFollowersHandler(getter followsGetter) handlers.Handler {
	return func(w http.ResponseWriter, r *http.Request) error {
		id := r.PathValue("id")
		if id == "" {
			return apierror.SimpleAPIError(http.StatusBadRequest, "id is missing in the path")
		}

		var (
			followsPage *paging.Page[int64, domain.FollowUser]
			err         error
		)
		if r.Body == http.NoBody {
			followsPage, err = getter(id, nil)
		} else {
			req := FollowListRequest{}
			decodeErr := json.NewDecoder(r.Body).Decode(&req)
			if decodeErr != nil {
				return apierror.SimpleAPIError(http.StatusBadRequest, "Invalid request body")
			}
			followsPage, err = getter(id, &req.Offset)
		}
		if err != nil {
			if errors.Is(err, offsetconv.ErrInvalidOffset) {
				return apierror.SimpleAPIError(http.StatusBadRequest, "Invalid offset")
			}
			return err
		}
		resp := FollowUserResponse{
			Total:   len(followsPage.Items),
			Follows: toTransportFollows(followsPage.Items),
			Offset:  followsPage.Offset,
		}
		return writer.SuccessJson(w, resp)
	}
}

func toTransportFollows(follows []domain.FollowUser) []FollowUser {
	followsTransport := make([]FollowUser, 0, len(follows))
	for _, user := range follows {
		u := FollowUser{
			Id:         user.Id,
			Name:       user.Name,
			ProfileUrl: user.ProfileUrl,
			UpdatedAt:  user.UpdatedAt,
		}
		followsTransport = append(followsTransport, u)
	}
	return followsTransport
}
