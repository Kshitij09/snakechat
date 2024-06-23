package middlewares

import (
	"errors"
	"github.com/Kshitij09/snakechat_server/transport/apierror"
	"github.com/Kshitij09/snakechat_server/transport/handlers"
	"log"
	"net/http"
	"time"
)

func HttpLogger(next handlers.Handler) handlers.Handler {
	return func(w http.ResponseWriter, r *http.Request) error {
		start := time.Now()
		wrappedWriter := &wrappedResponseWriter{
			ResponseWriter: w,
			statusCode:     http.StatusOK,
		}
		err := next(wrappedWriter, r)
		var (
			statusCode int
			apiErr     *apierror.APIError
		)
		if errors.As(err, &apiErr) {
			statusCode = apiErr.StatusCode
		} else {
			statusCode = wrappedWriter.statusCode
		}
		status := http.StatusText(statusCode)
		log.Printf("%s %s, status: %d %s, took: %s\n", r.Method, r.URL.Path, statusCode, status, time.Since(start))
		if err != nil {
			return err
		}
		return nil
	}
}

func (w *wrappedResponseWriter) WriteHeader(statusCode int) {
	w.ResponseWriter.WriteHeader(statusCode)
	w.statusCode = statusCode
}

type wrappedResponseWriter struct {
	http.ResponseWriter
	statusCode int
}
