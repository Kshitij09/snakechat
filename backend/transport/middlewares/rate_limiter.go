package middlewares

import (
	"github.com/Kshitij09/snakechat_server/transport"
	"github.com/Kshitij09/snakechat_server/transport/apierror"
	"golang.org/x/time/rate"
	"net"
	"net/http"
	"sync"
	"time"
)

func RateLimiter(next transport.Handler) transport.Handler {
	newLimiter := func() *rate.Limiter { return rate.NewLimiter(rate.Every(time.Hour), 20) }
	type client struct {
		limiter  *rate.Limiter
		lastSeen time.Time
	}
	var (
		mu      sync.Mutex
		clients = make(map[string]*client)
	)

	go func() {
		for {
			time.Sleep(5 * time.Minute)
			mu.Lock()
			for ip, client := range clients {
				if time.Since(client.lastSeen) > time.Hour {
					delete(clients, ip)
				}
			}
			mu.Unlock()
		}
	}()

	return func(w http.ResponseWriter, r *http.Request) error {
		ip, _, err := net.SplitHostPort(r.RemoteAddr)
		if err != nil {
			return err
		}
		mu.Lock()
		if _, found := clients[ip]; !found {
			clients[ip] = &client{limiter: newLimiter()}
		}
		clients[ip].lastSeen = time.Now()
		if !clients[ip].limiter.Allow() {
			mu.Unlock()
			return apierror.SimpleAPIError(http.StatusTooManyRequests, "rate limit exceeded, try again later")
		}
		mu.Unlock()
		return next(w, r)
	}
}
