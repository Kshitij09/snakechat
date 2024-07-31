package transport

import (
	"database/sql"
	"errors"
	"fmt"
	"github.com/Kshitij09/snakechat_server/domain"
	"github.com/Kshitij09/snakechat_server/sqlite"
	"github.com/Kshitij09/snakechat_server/transport/apierror"
	"github.com/Kshitij09/snakechat_server/transport/handlers"
	"github.com/Kshitij09/snakechat_server/transport/writer"
	"net/http"
)

type UserProfileResponse struct {
	Id             string              `json:"id"`
	Name           string              `json:"username"`
	Status         *string             `json:"status,omitempty"`
	ProfileUrl     *string             `json:"profile_url,omitempty"`
	FollowersCount int64               `json:"followers_count"`
	FollowingCount int64               `json:"following_count"`
	PostsCount     int64               `json:"posts_count"`
	PostThumbnails []UserPostThumbnail `json:"post_thumbnails"`
}

type UserPostThumbnail struct {
	Id           string `json:"id"`
	ThumbnailUrl string `json:"media_url"`
}

func UserProfileHandler(db *sql.DB) handlers.Handler {
	storage := sqlite.NewUserStorage(db)
	service := domain.NewUserService(storage)
	return func(w http.ResponseWriter, r *http.Request) error {
		userId := r.PathValue("id")
		if userId == "" {
			return apierror.SimpleAPIError(http.StatusBadRequest, "user id is missing or not valid, found: "+userId)
		}
		profile, err := service.UserProfile(userId)
		if errors.Is(err, domain.ErrUserNotFound) {
			return apierror.SimpleAPIError(http.StatusNotFound, fmt.Sprintf("User '%s' not found", userId))
		}
		if err != nil {
			return err
		}
		userProfileResponse := toTransportProfile(*profile)
		return writer.SuccessJson(w, userProfileResponse)
	}
}

func toTransportProfile(profile domain.UserProfile) UserProfileResponse {
	thumbs := make([]UserPostThumbnail, 0, len(profile.PostThumbnails))
	for _, postThumbnail := range profile.PostThumbnails {
		thumb := UserPostThumbnail{Id: postThumbnail.PostId, ThumbnailUrl: postThumbnail.ThumbnailUrl}
		thumbs = append(thumbs, thumb)
	}
	return UserProfileResponse{
		Id:             profile.Id,
		Name:           profile.Name,
		Status:         profile.Status,
		ProfileUrl:     profile.ProfileUrl,
		FollowersCount: profile.FollowersCount,
		FollowingCount: profile.FollowingCount,
		PostsCount:     profile.PostsCount,
		PostThumbnails: thumbs,
	}
}
