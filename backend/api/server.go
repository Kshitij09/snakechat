package api

import (
	"github.com/Kshitij09/snakechat_server/data"
	"log"
	"net/http"
)

type Server struct {
	listenAddr string
	db         *data.Database
}

func NewServer(port string) *Server {
	listenAddr := ":" + port
	db, err := data.CreateDatabase()
	if err != nil {
		panic(err)
	}
	return &Server{
		listenAddr: listenAddr,
		db:         db,
	}
}

func (s *Server) Run() error {
	router := http.NewServeMux()
	hg := NewHandlerGroup()
	hg.RegisterMiddleware(Logging)
	router.HandleFunc("GET /health", hg.Make(s.handleGetHealth))
	if err := s.registerSecuredGroup(router); err != nil {
		return err
	}
	log.Println("snakechat server listening on " + s.listenAddr)
	return http.ListenAndServe(s.listenAddr, router)
}

func (s *Server) registerSecuredGroup(router *http.ServeMux) error {
	securedGroup := NewHandlerGroup()
	securedGroup.RegisterMiddleware(Logging)
	apiKeyMiddleware, err := ApiKeyValidator()
	if err != nil {
		return err
	}
	securedGroup.RegisterMiddleware(apiKeyMiddleware)
	router.HandleFunc("GET /v1/trending-tags", securedGroup.Make(s.handleGetTrendingTags))
	router.HandleFunc("POST /v1/trending-feed", securedGroup.Make(s.handleGetTrendingFeed))
	return nil
}

func (s *Server) handleGetHealth(w http.ResponseWriter, _ *http.Request) error {
	_, err := w.Write([]byte("OK"))
	return err
}
