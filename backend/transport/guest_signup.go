package transport

import (
	"database/sql"
	"errors"
	"github.com/Kshitij09/snakechat_server/snakechat"
	"github.com/Kshitij09/snakechat_server/sqlite"
	"github.com/Kshitij09/snakechat_server/transport/apierror"
	"github.com/Kshitij09/snakechat_server/transport/handlers"
	"github.com/Kshitij09/snakechat_server/transport/writer"
	"net/http"
)

const HeaderDeviceId = "X-Device-Id"

type GuestSignUpResponse struct {
	UserId string `json:"user_id"`
}

func GuestSignUpHandler(db *sql.DB) handlers.Handler {
	storage := sqlite.NewUserStorage(db)
	service := snakechat.NewUserCredentialsService(storage)
	return func(w http.ResponseWriter, r *http.Request) error {
		deviceId := r.Header.Get(HeaderDeviceId)
		if deviceId == "" {
			return apierror.SimpleAPIError(http.StatusBadRequest, "header '"+HeaderDeviceId+"' missing")
		}
		credentials, err := service.GetOrCreateUser(deviceId)
		if errors.Is(err, snakechat.ErrInvalidDeviceId) {
			return apierror.SimpleAPIError(http.StatusBadRequest, "invalid device id")
		}
		if err != nil {
			return err
		}
		response := GuestSignUpResponse{UserId: credentials.UserId}
		return writer.SuccessJson(w, response)
	}
}
