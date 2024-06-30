package offsetconv

type Converter[K any, T KeyGetter[K]] interface {
	Parse(offset string) (*K, error)
	NextOffsetOrNil(items []T) *string
}

type KeyGetter[T any] interface {
	OffsetKey() T
}
