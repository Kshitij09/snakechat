package transport

import (
	"errors"
	"github.com/Kshitij09/snakechat_server/transport/writer"
	"github.com/Kshitij09/snakechat_server/util"
	"log"
	"net/http"
)

type Handler func(w http.ResponseWriter, r *http.Request) error

type Middleware func(next Handler) Handler

type HandlerGroup struct {
	middlewares []Middleware
}

func NewHandlerGroup() *HandlerGroup {
	return &HandlerGroup{}
}

func (hg *HandlerGroup) Clone() *HandlerGroup {
	return &HandlerGroup{middlewares: hg.middlewares}
}

func (hg *HandlerGroup) RegisterMiddlewares(middlewares ...Middleware) {
	hg.middlewares = append(hg.middlewares, middlewares...)
}

func (hg *HandlerGroup) Make(handler Handler, middlewares ...Middleware) http.HandlerFunc {
	for _, groupMiddleware := range hg.middlewares {
		handler = groupMiddleware(handler)
	}
	for _, ext := range middlewares {
		handler = ext(handler)
	}
	return func(w http.ResponseWriter, r *http.Request) {
		if err := handler(w, r); err != nil {
			var apiErr *util.APIError
			var writeErr error
			if errors.As(err, &apiErr) {
				writeErr = writer.ErrorJson(w, apiErr)
			} else {
				log.Println(err)
				writeErr = writer.ErrorJson(w, util.InternalServerError)
			}
			if writeErr != nil {
				log.Fatal(writeErr)
			}
		}
	}
}
