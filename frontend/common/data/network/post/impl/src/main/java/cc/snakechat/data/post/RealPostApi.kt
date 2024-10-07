package cc.snakechat.data.post

import cc.snakechat.data.post.comment.PostCommentsRequest
import cc.snakechat.data.post.comment.PostCommentsResponse
import cc.snakechat.data.post.like.LikersRequest
import cc.snakechat.data.post.like.LikersResponse
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
    override suspend fun getPostLikers(postId: String, offset: String?): LikersResponse = withContext(Dispatchers.IO) {
        httpClient.post("/v1/posts/$postId/likers") {
            if (offset != null) {
                setBody(LikersRequest(offset))
            } else {
                setBody(NullBody)
            }
        }.body()
    }

    override suspend fun getCommentLikers(commentId: String, offset: String?): LikersResponse = withContext(Dispatchers.IO) {
        httpClient.post("/v1/comments/$commentId/likers") {
            if (offset != null) {
                setBody(LikersRequest(offset))
            } else {
                setBody(NullBody)
            }
        }.body()
    }

    override suspend fun getPostComments(postId: String, offset: String?): PostCommentsResponse = withContext(Dispatchers.IO) {
        httpClient.post("/v1/posts/$postId/comments") {
            if (offset != null) {
                setBody(PostCommentsRequest(offset))
            } else {
                setBody(NullBody)
            }
        }.body()
    }
}
