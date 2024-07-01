package domain

import (
	"encoding/base64"
	"github.com/Kshitij09/snakechat_server/domain/offsetconv"
	"log"
	"strconv"
)

type timestampConverter[T offsetconv.KeyGetter[int64]] struct {
}

func (c timestampConverter[T]) Parse(offset string) (*int64, error) {
	decodedBytes, err := base64.StdEncoding.DecodeString(offset)
	if err != nil {
		log.Printf("error decoding offset: %s\n", err)
		return nil, offsetconv.ErrInvalidOffset
	}
	decoded := string(decodedBytes)
	offsetInt, err := strconv.ParseInt(decoded, 10, 64)
	if err != nil {
		log.Printf("error parsing offset: %s\n", err)
		return nil, offsetconv.ErrInvalidOffset
	}
	return &offsetInt, nil
}

func (c timestampConverter[T]) NextOffsetOrNil(items []T) *string {
	if len(items) == 0 {
		return nil
	}
	last := items[len(items)-1]
	updateTimestamp := strconv.FormatInt(last.OffsetKey(), 10)
	encoded := base64.StdEncoding.EncodeToString([]byte(updateTimestamp))
	return &encoded
}
