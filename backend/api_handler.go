package main

import (
	"encoding/json"
	"errors"
	"log"
	"net/http"
)

type APIHandler func(w http.ResponseWriter, r *http.Request) error

func Make(h APIHandler) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if err := h(w, r); err != nil {
			var apiErr *APIError
			var writeErr error
			if errors.As(err, &apiErr) {
				writeErr = writeJson(w, apiErr)
			} else {
				writeErr = writeJson(w, internalServerError)
			}
			if writeErr != nil {
				log.Fatal(writeErr)
			}
		}
	}
}

func writeJson(w http.ResponseWriter, apiErr *APIError) error {
	w.WriteHeader(apiErr.StatusCode)
	w.Header().Set("Content-Type", "application/json")
	return json.NewEncoder(w).Encode(apiErr)
}
