package snakechat

type UserDao interface {
	LastGuestUserId() (*string, error)
	CreateGuest(userId string) error
	AddDeviceMapping(userId string, deviceId string) error
	UserIdByDevice(deviceId string) (*string, error)
}
