package cc.snakechat.data.post

import android.content.Context
import cc.snakechat.data.post.comment.PostCommentsResponse
import cc.snakechat.data.post.like.LikersResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import me.tatarka.inject.annotations.Inject

@Inject
class FakePostApi(
    private val context: Context,
    private val json: Json,
) : PostApi {
    override suspend fun getPostLikers(postId: String, offset: String?): LikersResponse {
        val assetName = if (offset == null) "likers/post_likers_1.json" else "likers/post_likers_2.json"
        return context.assets.open(assetName)
            .use { json.decodeFromStream<LikersResponse>(it) }
    }

    override suspend fun getCommentLikers(commentId: String, offset: String?): LikersResponse {
        val assetName = if (offset == null) "likers/comment_likers_1.json" else "likers/comment_likers_2.json"
        return context.assets.open(assetName)
            .use { json.decodeFromStream<LikersResponse>(it) }
    }

    override suspend fun getPostComments(postId: String, offset: String?): PostCommentsResponse {
        val assetName = if (offset == null) "likers/post_comments_1.json" else "likers/post_comments_2.json"
        return context.assets.open(assetName)
            .use { json.decodeFromStream<PostCommentsResponse>(it) }
    }
}
