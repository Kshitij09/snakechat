package main

import (
	"flag"
	"github.com/Kshitij09/snakechat_server/transport"
)

func main() {
	portUsage := "port to listen on"
	defaultPort := 8080
	port := flag.Int("port", defaultPort, portUsage)
	enableSsl := flag.Bool("ssl", false, "enable SSL")
	flag.IntVar(port, "p", defaultPort, portUsage)
	flag.Parse()
	server := transport.NewServer()
	err := server.Run(*port, *enableSsl)
	if err != nil {
		panic(err)
	}
}
