package cc.snakechat.data.post.like

import kotlinx.serialization.Serializable

@Serializable
class PostLikersResponse(
    val likers: List<Liker>? = null,
    val offset: String? = null,
    val total: Int? = null,
)

@Serializable
class PostLikersRequest(
    val offset: String?,
)
