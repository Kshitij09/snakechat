package cc.snakechat.data.feed

import android.content.Context
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import me.tatarka.inject.annotations.Inject

@Inject
class FakeFeedApi(
    private val context: Context,
    private val json: Json,
) : FeedApi {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getTrendingFeed(request: TrendingFeedRequest?): TrendingFeedResponse {
        val reqOffset = request?.offset
        val offset = if (reqOffset == null) "1" else (reqOffset.toInt() + 1).toString()
        println("Loading new page with offset: $offset")
        val assetName = if (request?.offset == null) "feed/trending_feed_1.json" else "feed/trending_feed_2.json"
         return context.assets.open(assetName)
             .use { json.decodeFromStream<TrendingFeedResponse>(it) }
             // Paging library breaks if we return the same offset
             // It is safe to make assumptions from implementation details
             // while writing fake code
             .also { it.offset = offset }
    }
}