package cc.snakechat.domain.feed

import androidx.paging.PagingSource
import cc.snakechat.domain.common.SimplePagingSource


internal fun trendingFeedPagingSourceFactory(
    feedFetcher: TrendingFeedFetcher,
): () -> PagingSource<String, Post> = {
    SimplePagingSource(
        fetcher = { feedFetcher.fetch(it) },
        requestBuilder = { TrendingFeedRequest(it.key) },
    )
}
