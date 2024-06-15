package data

import (
	"database/sql"
	"errors"
	"github.com/Kshitij09/snakechat_server/data/model"
	_ "github.com/mattn/go-sqlite3"
	"os"
)

type Database struct {
	Tags TagsDao
}

type TagsDao interface {
	GetTrendingTags(count int8) ([]model.Tag, error)
}

func CreateDatabase() (*Database, error) {
	db, err := sql.Open("sqlite3", os.Getenv("DB_PATH"))
	if err != nil {
		envWarnErr := errors.New("error opening the DB connection, make sure you have set `DB_PATH` environment variable")
		return nil, errors.Join(envWarnErr, err)
	}
	tagsCtx := &tagsSqlContext{db: db}
	return &Database{Tags: tagsCtx}, nil
}
