package api

import (
	"log"
	"net/http"
	"time"
)

type wrappedResponseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (w *wrappedResponseWriter) WriteHeader(statusCode int) {
	w.ResponseWriter.WriteHeader(statusCode)
	w.statusCode = statusCode
}

func Logging(next Handler) Handler {
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
