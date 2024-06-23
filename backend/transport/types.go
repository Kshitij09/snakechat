package transport

import (
	"errors"
	"github.com/Kshitij09/snakechat_server/transport/apierror"
	"github.com/Kshitij09/snakechat_server/transport/writer"
	"log"
	"net/http"
)

type Handler func(w http.ResponseWriter, r *http.Request) error

type Middleware func(next Handler) Handler

func Append(first Middleware, next Middleware) Middleware {
	return func(h Handler) Handler {
		handler := next(h)
		handler = first(handler)
		return handler
	}
}

func NewHttpHandler(handler Handler) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if err := handler(w, r); err != nil {
			var apiErr *apierror.APIError
			var writeErr error
			if errors.As(err, &apiErr) {
				writeErr = writer.ErrorJson(w, apiErr)
			} else {
				log.Println(err)
				writeErr = writer.ErrorJson(w, apierror.InternalServerError)
			}
			if writeErr != nil {
				log.Fatal(writeErr)
			}
		}
	}
}
