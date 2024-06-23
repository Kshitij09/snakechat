package middlewares

import "github.com/Kshitij09/snakechat_server/transport/handlers"

type Middleware func(next handlers.Handler) handlers.Handler

func Append(first Middleware, next Middleware) Middleware {
	return func(h handlers.Handler) handlers.Handler {
		handler := next(h)
		handler = first(handler)
		return handler
	}
}
