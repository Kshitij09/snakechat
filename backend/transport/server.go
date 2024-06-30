package transport

import (
	"github.com/Kshitij09/snakechat_server/sqlite"
	"github.com/Kshitij09/snakechat_server/transport/handlers"
	"github.com/Kshitij09/snakechat_server/transport/middlewares"
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

	baseMiddleware := middlewares.HttpLogger
	router.HandleFunc("GET /health", handlers.NewHttpHandler(baseMiddleware(health)))

	apiKeyMiddleware, err := middlewares.ApiKeyValidator()
	if err != nil {
		return err
	}

	securedMiddleware := middlewares.Append(baseMiddleware, apiKeyMiddleware)

	trendingTags := TrendingTagsHandler(db)
	trendingTags = securedMiddleware(trendingTags)
	router.HandleFunc("POST /v1/trending-tags", handlers.NewHttpHandler(trendingTags))

	trendingFeed := TrendingFeedHandler(db)
	trendingFeed = securedMiddleware(trendingFeed)
	router.HandleFunc("POST /v1/trending-feed", handlers.NewHttpHandler(trendingFeed))

	guestSignUp := GuestSignUpHandler(db)
	guestSignUp = securedMiddleware(guestSignUp)
	router.HandleFunc("POST /v1/guestSignUp", handlers.NewHttpHandler(guestSignUp))

	userProfile := UserProfileHandler(db)
	userProfile = securedMiddleware(userProfile)
	router.HandleFunc("POST /v1/user/{id}", handlers.NewHttpHandler(userProfile))

	postLikers := PostLikersHandler(db)
	postLikers = securedMiddleware(postLikers)
	router.HandleFunc("POST /v1/posts/{id}/likers", handlers.NewHttpHandler(postLikers))

	commentLikers := CommentLikersHandler(db)
	commentLikers = securedMiddleware(commentLikers)
	router.HandleFunc("POST /v1/comments/{id}/likers", handlers.NewHttpHandler(commentLikers))

	log.Println("domain server listening on " + listenAddr)
	return http.ListenAndServe(listenAddr, router)
}

func health(w http.ResponseWriter, _ *http.Request) error {
	_, err := w.Write([]byte("OK"))
	return err
}
