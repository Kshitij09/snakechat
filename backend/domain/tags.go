package domain

type Tag struct {
	Id        string
	Title     string
	CreatedAt int64
}

type TagsDao interface {
	Trending(count int) ([]Tag, error)
}

type TagService struct {
	tags TagsDao
}

func NewTagService(tags TagsDao) TagService {
	return TagService{tags: tags}
}

func (s *TagService) Trending(count int) ([]Tag, error) {
	return s.tags.Trending(count)
}
