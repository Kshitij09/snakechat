package domain

type User struct {
	Id             string
	Name           string
	Status         string
	ProfileUrl     string
	FollowersCount int64
	FollowingCount int64
	PostsCount     int64
}

type UserProfile struct {
	User
	PostThumbnails []UserPostThumbnail
}

type UserPostThumbnail struct {
	PostId       string
	ThumbnailUrl string
}

type UserDao interface {
	LastGuestUserId() (*string, error)
	CreateGuest(userId string) error
	AddDeviceMapping(userId string, deviceId string) error
	UserIdByDevice(deviceId string) (*string, error)
	GetUserById(userId string) (*User, error)
	GetUserPostThumbnails(userId string) ([]UserPostThumbnail, error)
}

type UserService struct {
	users UserDao
}

func NewUserService(users UserDao) *UserService {
	return &UserService{users: users}
}

func (s *UserService) GetUserProfile(userId string) (*UserProfile, error) {
	user, err := s.users.GetUserById(userId)
	if err != nil {
		return nil, err
	}
	thumbnails, err := s.users.GetUserPostThumbnails(userId)
	if err != nil {
		return nil, err
	}
	profile := UserProfile{
		User:           *user,
		PostThumbnails: thumbnails,
	}
	return &profile, nil
}
