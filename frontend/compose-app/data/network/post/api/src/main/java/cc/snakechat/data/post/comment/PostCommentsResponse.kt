package cc.snakechat.data.post.comment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PostCommentsResponse(
    val comments: List<Comment>? = null,
    val offset: String? = null,
    val total: Int? = null
)

@Serializable
class PostCommentsRequest(
   val offset: String?
)

@Serializable
class Commenter(
    val id: String? = null,
    val name: String? = null,
    @SerialName("profile_url")
    val profileUrl: String? = null
)

@Serializable
class Comment(
    val commenter: Commenter? = null,
    val id: String? = null,
    val likes: Int? = null,
    val text: String? = null,
    @SerialName("updated_at")
    val updatedAt: Long? = null
)
