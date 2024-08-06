package cc.snakechat.data.post

import cc.snakechat.data.post.comment.PostCommentsResponse
import cc.snakechat.data.post.like.LikersResponse

interface PostApi {
    suspend fun getPostLikers(postId: String, offset: String? = null): LikersResponse
    suspend fun getCommentLikers(commentId: String, offset: String? = null): LikersResponse
    suspend fun getPostComments(postId: String, offset: String? = null): PostCommentsResponse
}
