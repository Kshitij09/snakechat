package util

import (
	"fmt"
	"net/http"
)

type APIError struct {
	StatusCode int              `json:"statusCode"`
	Errors     []map[string]any `json:"errors"`
}

func (e *APIError) Error() string {
	return fmt.Sprintf("API error: %d", e.StatusCode)
}

var InternalServerError = &APIError{
	StatusCode: http.StatusInternalServerError,
	Errors:     []map[string]any{{"msg": "Internal Server Error"}},
}
