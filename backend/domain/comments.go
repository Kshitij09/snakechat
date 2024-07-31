package domain

import (
	"encoding/base64"
	"fmt"
	"github.com/Kshitij09/snakechat_server/domain/offsetconv"
	"github.com/Kshitij09/snakechat_server/domain/paging"
	"log"
	"strconv"
	"strings"
)

const CommentsPageSize = 20

type Comment struct {
	Id        string
	UserId    string
	UpdatedAt int64
	Text      string
	Likes     int64
	Commenter Commenter
}

type Commenter struct {
	Id         string
	Name       string
	ProfileUrl *string
}

func (c Comment) OffsetKey() CommentsOffset {
	return CommentsOffset{
		Likes:           c.Likes,
		UpdateTimestamp: c.UpdatedAt,
	}
}

type CommentsOffset struct {
	Likes           int64
	UpdateTimestamp int64
}

type CommentsDao interface {
	PostComments(postId string) ([]Comment, error)
	PostCommentsByOffset(postId string, offset CommentsOffset) ([]Comment, error)
	CommentReplies(commentId string) ([]Comment, error)
	CommentRepliesByOffset(commentId string, offset CommentsOffset) ([]Comment, error)
}

type CommentsService struct {
	comments CommentsDao
}

func NewCommentsService(comments CommentsDao) *CommentsService {
	return &CommentsService{comments: comments}
}

func (s *CommentsService) PostComments(postId string, offset *string) (*paging.Page[CommentsOffset, Comment], error) {
	fetcher := paging.Fetcher[CommentsOffset, Comment]{
		ById:          s.comments.PostComments,
		ByIdAndOffset: s.comments.PostCommentsByOffset,
		OffsetConv:    commentsOffsetConverter{},
		PageSize:      CommentsPageSize,
	}
	comments, err := fetcher.FetchPage(postId, offset)
	if err != nil {
		return nil, fmt.Errorf("PostComments: %w", err)
	}
	return comments, nil
}

func (s *CommentsService) CommentReplies(commentId string, offset *string) (*paging.Page[CommentsOffset, Comment], error) {
	fetcher := paging.Fetcher[CommentsOffset, Comment]{
		ById:          s.comments.CommentReplies,
		ByIdAndOffset: s.comments.CommentRepliesByOffset,
		OffsetConv:    commentsOffsetConverter{},
		PageSize:      CommentsPageSize,
	}
	comments, err := fetcher.FetchPage(commentId, offset)
	if err != nil {
		return nil, fmt.Errorf("PostComments: %w", err)
	}
	return comments, nil
}

type commentsOffsetConverter struct {
}

func (_ commentsOffsetConverter) Parse(offset string) (*CommentsOffset, error) {
	decodedBytes, err := base64.StdEncoding.DecodeString(offset)
	if err != nil {
		log.Printf("error decoding offset: %s\n", err)
		return nil, offsetconv.ErrInvalidOffset
	}
	decoded := string(decodedBytes)
	parts := strings.Split(decoded, "_")
	if len(parts) != 2 {
		log.Printf("offset parts invalid: %s\n", parts)
		return nil, offsetconv.ErrInvalidOffset
	}
	likes, err := strconv.ParseInt(parts[0], 10, 64)
	if err != nil {
		log.Printf("error parsing the likes from offset: %s\n", parts)
		return nil, offsetconv.ErrInvalidOffset
	}
	updateTimestamp, err := strconv.ParseInt(parts[1], 10, 64)
	if err != nil {
		log.Printf("error parsing the updateTimestamp from offset: %s\n", parts)
		return nil, offsetconv.ErrInvalidOffset
	}
	commentOffset := CommentsOffset{Likes: likes, UpdateTimestamp: updateTimestamp}
	return &commentOffset, nil
}

func (_ commentsOffsetConverter) NextOffsetOrNil(items []Comment) *string {
	if len(items) == 0 {
		return nil
	}
	last := items[len(items)-1]
	offset := last.OffsetKey()
	offsetStr := fmt.Sprintf("%d_%d", offset.Likes, offset.UpdateTimestamp)
	encoded := base64.StdEncoding.EncodeToString([]byte(offsetStr))
	return &encoded
}
