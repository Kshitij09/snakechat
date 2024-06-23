package transport

import (
	"log"
	"net/http"
	"time"
)

func HttpLogger(next Handler) Handler {
	return func(w http.ResponseWriter, r *http.Request) error {
		start := time.Now()
		wrappedWriter := &wrappedResponseWriter{
			ResponseWriter: w,
			statusCode:     http.StatusOK,
		}
		err := next(wrappedWriter, r)
		status := http.StatusText(wrappedWriter.statusCode)
		log.Printf("%s %s, status: %d %s, took: %s\n", r.Method, r.URL.Path, wrappedWriter.statusCode, status, time.Since(start))
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
