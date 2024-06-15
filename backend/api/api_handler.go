package api

import (
	"encoding/json"
	"errors"
	"github.com/Kshitij09/snakechat_server/util"
	"log"
	"net/http"
)

type Handler func(w http.ResponseWriter, r *http.Request) error

func Make(h Handler) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if err := h(w, r); err != nil {
			var apiErr *util.APIError
			var writeErr error
			if errors.As(err, &apiErr) {
				writeErr = WriteJsonError(w, apiErr)
			} else {
				log.Println(err)
				writeErr = WriteJsonError(w, util.InternalServerError)
			}
			if writeErr != nil {
				log.Fatal(writeErr)
			}
		}
	}
}

func WriteJsonError(w http.ResponseWriter, apiErr *util.APIError) error {
	return writeJson(w, apiErr.StatusCode, apiErr)
}

func WriteSuccessJson(w http.ResponseWriter, data any) error {
	return writeJson(w, http.StatusOK, data)
}

func writeJson(w http.ResponseWriter, statusCode int, data any) error {
	if statusCode != http.StatusOK {
		w.WriteHeader(statusCode)
	}
	w.Header().Set("Content-Type", "application/json")
	return json.NewEncoder(w).Encode(data)
}
