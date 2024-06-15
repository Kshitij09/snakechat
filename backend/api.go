package main

import (
	"github.com/gorilla/mux"
	"log"
	"net/http"
)

type APIServer struct {
	listenAddr string
}

func NewAPIServer(port string) *APIServer {
	var listenAddr = ":" + port
	return &APIServer{
		listenAddr: listenAddr,
	}
}

const httpGet = "GET"

func (s *APIServer) Run() {
	router := mux.NewRouter()
	router.HandleFunc("/health", Make(s.handleGetHealth)).Methods(httpGet)
	log.Println("snakechat server listening on " + s.listenAddr)
	_ = http.ListenAndServe(s.listenAddr, router)
}

func (s *APIServer) handleGetHealth(w http.ResponseWriter, _ *http.Request) error {
	_, err := w.Write([]byte("OK"))
	return err
}
