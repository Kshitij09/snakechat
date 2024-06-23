package main

import (
	"github.com/Kshitij09/snakechat_server/transport"
)

func main() {
	server := transport.NewServer()
	err := server.Run("8080")
	if err != nil {
		panic(err)
	}
}
