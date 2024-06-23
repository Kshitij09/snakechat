package data

import (
	"database/sql"
	"errors"
	"github.com/Kshitij09/snakechat_server/data/model"
	_ "github.com/mattn/go-sqlite3"
	"os"
)

type Database struct {
	Feed FeedDao
	User UserDao
}

type FeedDao interface {
	GetTrendingFeed(offset string) (*model.Feed, error)
	GetFirstTrendingFeed() (*model.Feed, error)
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
	queryContext := feedQueryContext{db: db}
	feedCtx := &feedSqlContext{db: db, queryRunner: queryContext}
	userCtx := &userSqlContext{db: db, guestUserIdHandler: guestUserIdHandler{}}
	return &Database{Feed: feedCtx, User: userCtx}, nil
}
