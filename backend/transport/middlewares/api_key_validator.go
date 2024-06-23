package middlewares

import (
	"errors"
	"github.com/Kshitij09/snakechat_server/transport/apierror"
	"github.com/Kshitij09/snakechat_server/transport/handlers"
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
	return func(next handlers.Handler) handlers.Handler {
		return func(w http.ResponseWriter, r *http.Request) error {
			apiKey := r.Header.Get(apikeyHeader)
			if apiKey == "" {
				return apierror.SimpleAPIError(http.StatusUnauthorized, "api key is required, make sure to include 'X-Api-Key' header with a valid API key")
			}
			if apiKey != serverApiKey {
				return apierror.SimpleAPIError(http.StatusForbidden, "api key is invalid")
			}
			return next(w, r)
		}
	}
}
