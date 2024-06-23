package api

import (
	"errors"
	"github.com/Kshitij09/snakechat_server/data"
	"github.com/Kshitij09/snakechat_server/util"
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
	securedGroup.RegisterMiddleware(RateLimiter)
	router.HandleFunc("GET /v1/trending-tags", securedGroup.Make(s.handleGetTrendingTags))
	router.HandleFunc("POST /v1/trending-feed", securedGroup.Make(s.handleGetTrendingFeed))
	router.HandleFunc("POST /v1/guestSignUp", securedGroup.Make(s.handleGuestSignUp))
	return nil
}

func (s *Server) handleGetHealth(w http.ResponseWriter, _ *http.Request) error {
	_, err := w.Write([]byte("OK"))
	return err
}

type GuestSignUpResponse struct {
	UserId string `json:"user_id"`
}

func (s *Server) handleGuestSignUp(w http.ResponseWriter, r *http.Request) error {
	deviceId := r.Header.Get(HeaderDeviceId)
	if deviceId == "" {
		return util.SimpleAPIError(http.StatusBadRequest, "header '"+HeaderDeviceId+"' missing")
	}
	credentials, err := s.db.User.GetOrCreateUser(deviceId)
	if errors.Is(err, data.ErrInvalidDeviceId) {
		return util.SimpleAPIError(http.StatusBadRequest, "invalid device id")
	}
	if err != nil {
		return err
	}
	response := GuestSignUpResponse{UserId: credentials.UserId}
	return WriteSuccessJson(w, response)
}
