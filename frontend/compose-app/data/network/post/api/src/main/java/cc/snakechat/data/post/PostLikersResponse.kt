package cc.snakechat.data.post


import kotlinx.serialization.Serializable

@Serializable
class PostLikersResponse(
    val likers: List<Liker>? = null,
    val offset: String? = null,
    val total: Int? = null
)