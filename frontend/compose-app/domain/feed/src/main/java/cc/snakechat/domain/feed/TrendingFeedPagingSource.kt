package cc.snakechat.domain.feed

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cc.snakechat.inject.ApplicationScope
import me.tatarka.inject.annotations.Inject

@Inject
@ApplicationScope
class TrendingFeedPagingSource(private val getTrendingFeed: GetTrendingFeed) : PagingSource<String, Post>() {
    private val visitedPostIds = mutableSetOf<String>()

    override fun getRefreshKey(state: PagingState<String, Post>): String? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor) ?: return null
        return page.nextKey
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
        val offset = params.key
        val req = TrendingFeedRequest(offset)
        val result = getTrendingFeed.execute(req)
        return LoadResult.Page(
            data =  result.posts.filter {
                if (!visitedPostIds.contains(it.id)) {
                    visitedPostIds.add(it.id)
                    true
                } else {
                    false
                }
            },
            prevKey = offset,
            nextKey = result.offset,
        )
    }
}