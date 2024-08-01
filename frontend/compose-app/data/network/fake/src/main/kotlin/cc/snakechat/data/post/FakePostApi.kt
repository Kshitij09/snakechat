package cc.snakechat.data.post

import android.content.Context
import cc.snakechat.data.post.comment.PostCommentsResponse
import cc.snakechat.data.post.like.PostLikersResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import me.tatarka.inject.annotations.Inject

@Inject
class FakePostApi(
    private val context: Context,
    private val json: Json,
) : PostApi {
    override suspend fun getPostLikers(postId: String, offset: String?): PostLikersResponse {
        val assetName = if (offset == null) "likers/post_likers_1.json" else "likers/post_likers_2.json"
        return context.assets.open(assetName)
            .use { json.decodeFromStream<PostLikersResponse>(it) }
    }

    override suspend fun getPostComments(postId: String, offset: String?): PostCommentsResponse {
        val assetName = if (offset == null) "likers/post_comments_1.json" else "likers/post_comments_2.json"
        return context.assets.open(assetName)
            .use { json.decodeFromStream<PostCommentsResponse>(it) }
    }
}
