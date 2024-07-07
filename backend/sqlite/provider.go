package sqlite

import (
	"database/sql"
	"errors"
	_ "github.com/mattn/go-sqlite3"
	"os"
)

func New() (*sql.DB, error) {
	dbPath := os.Getenv("DB_PATH")
	_, err := os.Stat(dbPath)
	if os.IsNotExist(err) {
		return nil, errors.New("database path does not exist: " + dbPath)
	}
	db, err := sql.Open("sqlite3", os.Getenv("DB_PATH"))
	if err != nil {
		envWarnErr := errors.New("error opening the DB connection, make sure you have set `DB_PATH` environment variable")
		return nil, errors.Join(envWarnErr, err)
	}
	return db, nil
}
