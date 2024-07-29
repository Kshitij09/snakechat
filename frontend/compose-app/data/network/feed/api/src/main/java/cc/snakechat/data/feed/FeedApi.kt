package cc.snakechat.data.feed

import kotlinx.serialization.Serializable

interface FeedApi {
    suspend fun getTrendingFeed(request: TrendingFeedRequest? = null): TrendingFeedResponse
}

@Serializable
class TrendingFeedRequest(val offset: String? = null)