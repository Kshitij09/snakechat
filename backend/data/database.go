package data

import (
	"database/sql"
	"errors"
	"github.com/Kshitij09/snakechat_server/data/model"
	_ "github.com/mattn/go-sqlite3"
	"os"
)

type Database struct {
	User UserDao
}

type UserDao interface {
	GetOrCreateUser(deviceId string) (*model.UserCredentials, error)
}

func CreateDatabase() (*Database, error) {
	db, err := sql.Open("sqlite3", os.Getenv("DB_PATH"))
	if err != nil {
		envWarnErr := errors.New("error opening the DB connection, make sure you have set `DB_PATH` environment variable")
		return nil, errors.Join(envWarnErr, err)
	}
	userCtx := &userSqlContext{db: db, guestUserIdHandler: guestUserIdHandler{}}
	return &Database{User: userCtx}, nil
}
