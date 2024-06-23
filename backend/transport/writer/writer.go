package writer

import (
	"encoding/json"
	"github.com/Kshitij09/snakechat_server/transport/apierror"
	"net/http"
)

func ErrorJson(w http.ResponseWriter, apiErr *apierror.APIError) error {
	return writeJson(w, apiErr.StatusCode, apiErr)
}

func SuccessJson(w http.ResponseWriter, data any) error {
	return writeJson(w, http.StatusOK, data)
}

func writeJson(w http.ResponseWriter, statusCode int, data any) error {
	if statusCode != http.StatusOK {
		w.WriteHeader(statusCode)
	}
	w.Header().Set("Content-Type", "application/json")
	return json.NewEncoder(w).Encode(data)
}
