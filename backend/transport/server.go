package transport

import (
	"crypto/tls"
	"errors"
	"github.com/Kshitij09/snakechat_server/sqlite"
	"github.com/Kshitij09/snakechat_server/transport/handlers"
	"github.com/Kshitij09/snakechat_server/transport/middlewares"
	"log"
	"net/http"
	"os"
	"strconv"
)

type Server struct{}

func NewServer() *Server {
	return &Server{}
}

func (s *Server) Run(port int) error {
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

	tlsConfig := readTlsConfig()
	log.Println("snakechat server started listening on " + listenAddr)
	if tlsConfig != nil {
		_, err := os.Stat(tlsConfig.certFile)
		if os.IsNotExist(err) {
			return errors.New("tls cert file not found:" + tlsConfig.certFile)
		}
		_, err = os.Stat(tlsConfig.keyFile)
		if os.IsNotExist(err) {
			return errors.New("tls key file not found:" + tlsConfig.keyFile)
		}
		server := &http.Server{
			Addr: ":443",
			TLSConfig: &tls.Config{
				MinVersion:       tls.VersionTLS12,
				CurvePreferences: []tls.CurveID{tls.CurveP521, tls.CurveP384, tls.CurveP256},
				CipherSuites: []uint16{
					tls.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
					tls.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
					tls.TLS_RSA_WITH_AES_256_GCM_SHA384,
					tls.TLS_RSA_WITH_AES_256_CBC_SHA,
				},
			},
			Handler: router,
		}
		return server.ListenAndServeTLS(tlsConfig.certFile, tlsConfig.keyFile)
	} else {
		return http.ListenAndServe(listenAddr, router)
	}
}

type tlsFileConfig struct {
	certFile, keyFile string
}

func readTlsConfig() *tlsFileConfig {
	certFile := os.Getenv("SSL_CERT_FILE")
	if certFile == "" {
		return nil
	}
	keyFile := os.Getenv("SSL_KEY_FILE")
	if keyFile == "" {
		return nil
	}
	return &tlsFileConfig{certFile: certFile, keyFile: keyFile}
}

func health(w http.ResponseWriter, _ *http.Request) error {
	_, err := w.Write([]byte("OK"))
	return err
}
