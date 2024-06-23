package transport

import (
	"github.com/Kshitij09/snakechat_server/sqlite"
	"github.com/Kshitij09/snakechat_server/transport/tags"
	"log"
	"net/http"
)

type Server struct{}

func NewServer() *Server {
	return &Server{}
}

func (s *Server) Run(port string) error {
	listenAddr := ":" + port
	router := http.NewServeMux()

	db, err := sqlite.New()
	if err != nil {
		return err
	}

	baseHg := NewHandlerGroup()
	baseHg.RegisterMiddlewares(HttpLogger)
	router.HandleFunc("GET /health", baseHg.Make(health))

	securedHg := baseHg.Clone()

	apiKeyMiddleware, err := ApiKeyValidator()
	if err != nil {
		return err
	}
	securedHg.RegisterMiddlewares(apiKeyMiddleware)

	tagsHandler := tags.NewHandler(db)
	router.HandleFunc("GET /v1/trending-tags", securedHg.Make(tagsHandler.Trending))
	log.Println("snakechat server listening on " + listenAddr)
	return http.ListenAndServe(listenAddr, router)
}

func health(w http.ResponseWriter, _ *http.Request) error {
	_, err := w.Write([]byte("OK"))
	return err
}
