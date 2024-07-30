package cc.snakechat.domain.feed

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface ObservePagingData<in Request, Data : Any> {
    fun observe(request: Request): Flow<PagingData<Data>>
}

internal class ObserveTrendingFeedPageData(
    private val feedFetcher: TrendingFeedFetcher,
) : ObservePagingData<Unit, Post> {
    override fun observe(request: Unit): Flow<PagingData<Post>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 2,
            initialLoadSize = 10,
        ),
        pagingSourceFactory = trendingFeedPagingSourceFactory(feedFetcher),
    ).flow
}
