package cc.snakechat.data.post

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.content.NullBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
internal class RealPostApi(private val httpClient: HttpClient) : PostApi {
    override suspend fun getPostLikers(postId: String, offset: String?): PostLikersResponse = withContext(Dispatchers.IO) {
        httpClient.post("/v1/posts/$postId/likers") {
            if (offset != null) {
                setBody(PostLikersRequest(offset))
            } else {
                setBody(NullBody)
            }
        }.body()
    }
}
