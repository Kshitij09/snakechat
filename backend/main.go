package main

import "github.com/Kshitij09/snakechat_server/api"

func main() {
	server := api.NewServer("8080")
	server.Run()
}
