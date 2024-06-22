package api

import (
	"errors"
	"github.com/Kshitij09/snakechat_server/util"
	"net/http"
	"os"
)

const apikeyHeader = "X-Api-Key"

func ApiKeyValidator() (Middleware, error) {
	serverApiKey := os.Getenv("API_KEY")
	if serverApiKey == "" {
		return nil, errors.New("environment variable 'API_KEY' is not set")
	}
	return apiKeyMiddleware(serverApiKey), nil
}

func apiKeyMiddleware(serverApiKey string) Middleware {
	return func(next Handler) Handler {
		return func(w http.ResponseWriter, r *http.Request) error {
			apiKey := r.Header.Get(apikeyHeader)
			if apiKey == "" {
				return util.SimpleAPIError(http.StatusUnauthorized, "api key is required, make sure to include 'X-Api-Key' header with a valid API key")
			}
			if apiKey != serverApiKey {
				return util.SimpleAPIError(http.StatusForbidden, "api key is invalid")
			}
			return next(w, r)
		}
	}
}
