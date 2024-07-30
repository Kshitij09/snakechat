package cc.snakechat.domain.feed

import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.tatarka.inject.annotations.Inject

@Inject
internal class TrendingFeedPagingSource(
    private val feedFetcher: suspend (TrendingFeedRequest) -> LoadResult<String, Post>,
) : PagingSource<String, Post>() {

    override fun getRefreshKey(state: PagingState<String, Post>): String? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor) ?: return null
        return page.nextKey
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
        val offset = params.key
        val req = TrendingFeedRequest(offset)
        return feedFetcher(req)
    }
}

internal fun trendingFeedPagingSourceFactory(
    feedFetcher: TrendingFeedFetcher,
): () -> PagingSource<String, Post> = { TrendingFeedPagingSource { feedFetcher.fetch(it) } }
