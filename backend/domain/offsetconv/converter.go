package offsetconv

import "errors"

type Converter[K any, T KeyGetter[K]] interface {
	Parse(offset string) (*K, error)
	NextOffsetOrNil(items []T) *string
}

type KeyGetter[T any] interface {
	OffsetKey() T
}

var ErrInvalidOffset = errors.New("invalid offset")
