package cc.snakechat.data.post

import cc.snakechat.data.post.comment.PostCommentsResponse
import cc.snakechat.data.post.like.PostLikersResponse

interface PostApi {
    suspend fun getPostLikers(postId: String, offset: String? = null): PostLikersResponse
    suspend fun getPostComments(postId: String, offset: String? = null): PostCommentsResponse
}
