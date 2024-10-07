package cc.snakechat.domain.common

import androidx.paging.PagingSource
import androidx.paging.PagingState

class SimplePagingSource<Req, Key : Any, Value : Any>(
    private val fetcher: suspend (Req) -> LoadResult<Key, Value>,
    private val requestBuilder: (LoadParams<Key>) -> Req,
) : PagingSource<Key, Value>() {

    override fun getRefreshKey(state: PagingState<Key, Value>): Key? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor) ?: return null
        return page.nextKey
    }

    override suspend fun load(params: LoadParams<Key>): LoadResult<Key, Value> {
        val request = requestBuilder(params)
        return fetcher(request)
    }
}
