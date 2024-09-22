package transport

import (
	"github.com/Kshitij09/snakechat_server/sqlite"
	"github.com/Kshitij09/snakechat_server/transport/handlers"
	"github.com/Kshitij09/snakechat_server/transport/middlewares"
	"golang.org/x/crypto/acme/autocert"
	"log"
	"net/http"
	"strconv"
)

type Server struct{}

func NewServer() *Server {
	return &Server{}
}

func (s *Server) Run(port int, enableSsl bool) error {
	listenAddr := ":" + strconv.Itoa(port)
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

	followers := FollowersHandler(db)
	followers = securedMiddleware(followers)
	router.HandleFunc("POST /v1/users/{id}/followers", handlers.NewHttpHandler(followers))

	following := FollowingHandler(db)
	following = securedMiddleware(following)
	router.HandleFunc("POST /v1/users/{id}/following", handlers.NewHttpHandler(following))

	postLikers := PostLikersHandler(db)
	postLikers = securedMiddleware(postLikers)
	router.HandleFunc("POST /v1/posts/{id}/likers", handlers.NewHttpHandler(postLikers))

	postComments := PostCommentsHandler(db)
	postComments = securedMiddleware(postComments)
	router.HandleFunc("POST /v1/posts/{id}/comments", handlers.NewHttpHandler(postComments))

	commentLikers := CommentLikersHandler(db)
	commentLikers = securedMiddleware(commentLikers)
	router.HandleFunc("POST /v1/comments/{id}/likers", handlers.NewHttpHandler(commentLikers))

	commentReplies := CommentRepliesHandler(db)
	commentReplies = securedMiddleware(commentReplies)
	router.HandleFunc("POST /v1/comments/{id}/replies", handlers.NewHttpHandler(commentReplies))

	if enableSsl {
		certManager := autocert.Manager{
			Prompt:     autocert.AcceptTOS,
			HostPolicy: autocert.HostWhitelist("apis.sharechat.cc", "apis.staging.snakechat.cc"),
			Cache:      autocert.DirCache("certs"),
		}
		server := &http.Server{
			Addr:      ":443",
			TLSConfig: certManager.TLSConfig(),
			Handler:   router,
		}
		server.RegisterOnShutdown(func() {
			db.Close()
		})
		log.Println("snakechat server started listening on :443")
		go http.ListenAndServe(listenAddr, certManager.HTTPHandler(nil))
		return server.ListenAndServeTLS("", "")
	} else {
		server := &http.Server{
			Addr:    listenAddr,
			Handler: router,
		}

		server.RegisterOnShutdown(func() {
			db.Close()
		})
		log.Println("snakechat server started listening on " + listenAddr)
		return server.ListenAndServe()
	}
}

func health(w http.ResponseWriter, _ *http.Request) error {
	_, err := w.Write([]byte("OK"))
	return err
}
