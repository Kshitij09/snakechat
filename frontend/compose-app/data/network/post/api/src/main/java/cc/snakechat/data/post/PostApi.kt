package cc.snakechat.data.post

interface PostApi {
    suspend fun getPostLikers(postId: String): PostLikersResponse
}