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

func (s *Server) Run() {
	router := http.NewServeMux()
	hg := NewHandlerGroup()
	router.HandleFunc("GET /health", hg.Make(s.handleGetHealth))
	router.HandleFunc("GET /v1/trending-tags", hg.Make(s.handleGetTrendingTags))
	router.HandleFunc("POST /v1/trending-feed", hg.Make(s.handleGetTrendingFeed))
	log.Println("snakechat server listening on " + s.listenAddr)
	_ = http.ListenAndServe(s.listenAddr, router)
}

func (s *Server) handleGetHealth(w http.ResponseWriter, _ *http.Request) error {
	_, err := w.Write([]byte("OK"))
	return err
}
