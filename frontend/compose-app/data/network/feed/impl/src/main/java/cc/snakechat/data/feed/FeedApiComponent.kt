package cc.snakechat.data.feed

import cc.snakechat.inject.ApplicationScope
import io.ktor.client.HttpClient
import me.tatarka.inject.annotations.Provides

interface FeedApiComponent {
    @Provides
    @ApplicationScope
    fun provideFeedApi(httpClient: HttpClient): FeedApi = RealFeedApi(httpClient)
}