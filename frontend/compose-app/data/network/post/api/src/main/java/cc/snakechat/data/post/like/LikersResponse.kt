package cc.snakechat.data.post.like

import kotlinx.serialization.Serializable

@Serializable
class LikersResponse(
    val likers: List<Liker>? = null,
    val offset: String? = null,
    val total: Int? = null,
)

@Serializable
class LikersRequest(
    val offset: String?,
)
