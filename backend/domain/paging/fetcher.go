package paging

import (
	"github.com/Kshitij09/snakechat_server/domain/offsetconv"
)

type Page[K any, T offsetconv.KeyGetter[K]] struct {
	Total  int
	Items  []T
	Offset *string
}

type Fetcher[K any, T offsetconv.KeyGetter[K]] struct {
	ById          fetcherById[K, T]
	ByIdAndOffset fetcherByIdAndOffset[K, T]
	OffsetConv    offsetconv.Converter[K, T]
	PageSize      int
}

type fetcherById[K any, T offsetconv.KeyGetter[K]] func(id string) ([]T, error)
type fetcherByIdAndOffset[K any, T offsetconv.KeyGetter[K]] func(id string, offset K) ([]T, error)

func (fetcher Fetcher[K, T]) FetchPage(id string, offset *string) (*Page[K, T], error) {
	items, err := fetcher.Fetch(id, offset)
	if err != nil {
		return nil, err
	}
	var nextOffset *string
	if len(items) == fetcher.PageSize {
		nextOffset = fetcher.OffsetConv.NextOffsetOrNil(items)
	}
	return &Page[K, T]{Total: len(items), Items: items, Offset: nextOffset}, nil
}

func (fetcher Fetcher[K, T]) Fetch(id string, offset *string) ([]T, error) {
	if offset == nil || len(*offset) == 0 {
		return fetcher.ById(id)
	} else {
		parsedOffset, err := fetcher.OffsetConv.Parse(*offset)
		if err != nil {
			return nil, err
		}
		return fetcher.ByIdAndOffset(id, *parsedOffset)
	}
}
