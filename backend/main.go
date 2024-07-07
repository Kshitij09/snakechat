package main

import (
	"flag"
	"github.com/Kshitij09/snakechat_server/transport"
)

func main() {
	portUsage := "port to listen on"
	defaultPort := 8080
	port := flag.Int("port", defaultPort, portUsage)
	flag.IntVar(port, "p", defaultPort, portUsage)
	flag.Parse()
	server := transport.NewServer()
	err := server.Run(*port)
	if err != nil {
		panic(err)
	}
}
