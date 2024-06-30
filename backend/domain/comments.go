package domain

import (
	"fmt"
	"github.com/Kshitij09/snakechat_server/domain/paging"
)

const CommentsPageSize = 20

type Comment struct {
	Id        string
	UserId    string
	UpdatedAt int64
	Text      string
	Commenter Commenter
}

type Commenter struct {
	Id         string
	Name       string
	ProfileUrl string
}

func (c Comment) OffsetKey() int64 {
	return c.UpdatedAt
}

type CommentsDao interface {
	PostComments(postId string) ([]Comment, error)
	PostCommentsUpdatedBefore(postId string, updateTimestamp int64) ([]Comment, error)
	CommentReplies(commentId string) ([]Comment, error)
	CommentRepliesUpdatedBefore(commentId string, updateTimestamp int64) ([]Comment, error)
}

type CommentsService struct {
	comments CommentsDao
}

func NewCommentsService(comments CommentsDao) *CommentsService {
	return &CommentsService{comments: comments}
}

func (s *CommentsService) PostComments(postId string, offset *string) (*paging.Page[int64, Comment], error) {
	fetcher := paging.Fetcher[int64, Comment]{
		ById:          s.comments.PostComments,
		ByIdAndOffset: s.comments.PostCommentsUpdatedBefore,
		OffsetConv:    timestampConverter[Comment]{},
		PageSize:      CommentsPageSize,
	}
	comments, err := fetcher.FetchPage(postId, offset)
	if err != nil {
		return nil, fmt.Errorf("PostComments: %w", err)
	}
	return comments, nil
}

func (s *CommentsService) CommentReplies(commentId string, offset *string) (*paging.Page[int64, Comment], error) {
	fetcher := paging.Fetcher[int64, Comment]{
		ById:          s.comments.CommentReplies,
		ByIdAndOffset: s.comments.CommentRepliesUpdatedBefore,
		OffsetConv:    timestampConverter[Comment]{},
		PageSize:      CommentsPageSize,
	}
	comments, err := fetcher.FetchPage(commentId, offset)
	if err != nil {
		return nil, fmt.Errorf("PostComments: %w", err)
	}
	return comments, nil
}
