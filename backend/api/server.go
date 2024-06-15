package api

import (
	"github.com/Kshitij09/snakechat_server/data"
	"github.com/gorilla/mux"
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

const httpGet = "GET"

func (s *Server) Run() {
	router := mux.NewRouter()
	router.HandleFunc("/health", Make(s.handleGetHealth)).Methods(httpGet)
	router.HandleFunc("/v1/trending-tags", Make(s.handleGetTrendingTags)).Methods(httpGet)
	log.Println("snakechat server listening on " + s.listenAddr)
	_ = http.ListenAndServe(s.listenAddr, router)
}

func (s *Server) handleGetHealth(w http.ResponseWriter, _ *http.Request) error {
	_, err := w.Write([]byte("OK"))
	return err
}
