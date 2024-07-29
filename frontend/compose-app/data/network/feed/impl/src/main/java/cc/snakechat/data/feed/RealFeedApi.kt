package cc.snakechat.data.feed

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class RealFeedApi(private val httpClient: HttpClient) : FeedApi {
    override suspend fun getTrendingFeed(request: TrendingFeedRequest?): TrendingFeedResponse {
        return withContext(Dispatchers.IO) {
            httpClient.post("/v1/trending-feed") {
                setBody(request)
            }.body()
        }
    }
}