package cc.snakechat.domain.feed

import cc.snakechat.data.feed.FeedApi
import me.tatarka.inject.annotations.Provides

interface FeedComponent {
    @Provides
    fun provideObserveTrendingFeedPageData(api: FeedApi): ObservePagingData<Unit, Post> {
        val fetcher = TrendingFeedFetcher(api)
        return ObserveTrendingFeedPageData(fetcher)
    }
}