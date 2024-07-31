package cc.snakechat.domain.feed

import androidx.paging.PagingSource
import cc.snakechat.domain.common.SimplePagingSource


internal fun trendingFeedPagingSourceFactory(
    feedFetcher: TrendingFeedFetcher,
): () -> PagingSource<String, Post> = {
    SimplePagingSource(
        feedFetcher = { feedFetcher.fetch(it) },
        requestMapper = { TrendingFeedRequest(it.key) },
    )
}
