package model

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
	Rank      int64
	TagId     string
	User      PostUserMeta
}

type PostUserMeta struct {
	Id         string
	Name       string
	ProfileUrl string
}

type UserCredentials struct {
	UserId string
}
