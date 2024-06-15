package main

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

func NewAPIError(statusCode int, err error) *APIError {
	return &APIError{
		StatusCode: statusCode,
		Errors: []map[string]any{
			{"msg": err.Error()},
		},
	}
}

var internalServerError = &APIError{
	StatusCode: http.StatusInternalServerError,
	Errors:     []map[string]any{{"msg": "Internal Server Error"}},
}
