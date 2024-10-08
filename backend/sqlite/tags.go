package sqlite

import (
	"database/sql"
	"fmt"
	"github.com/Kshitij09/snakechat_server/domain"
)

type TagsStorage struct {
	db *sql.DB
}

func NewTagsStorage(db *sql.DB) TagsStorage {
	return TagsStorage{db: db}
}

func (ctx TagsStorage) Trending(count int) ([]domain.Tag, error) {
	query := `
	SELECT t.id, t.title, t.created_at 
	FROM tags t INNER JOIN posts p ON t.id = p.tag_id
	WHERE t.title <> 'trending' and t.deleted_at IS NULL
	GROUP BY t.id 
	ORDER BY COUNT(p.id) DESC
	`
	rows, err := ctx.db.Query(query, count)
	if err != nil {
		return nil, fmt.Errorf("error fetching the trending tags: %s", err)
	}
	defer rows.Close()
	tags := make([]domain.Tag, 0, count)
	appends := 0
	for rows.Next() {
		var tag domain.Tag
		appends++
		if err := rows.Scan(&tag.Id, &tag.Title, &tag.CreatedAt); err != nil {
			return nil, fmt.Errorf("error parsing a tag row: %s", err)
		}
		tags = append(tags, tag)
	}
	return tags, nil
}
