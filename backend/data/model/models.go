package model

type Tag struct {
	Id        string
	Title     string
	CreatedAt int64
}

type Feed struct {
	Posts  []Post
	Offset string
}

type Post struct {
	Id        string
	Caption   string
	MediaUrl  string
	CreatedAt int64
	Likes     int64
	Shares    int64
	Saves     int64
	Downloads int64
	Total     int64
}

type PostUserMeta struct {
	Id         string
	Name       string
	ProfileUrl string
}
