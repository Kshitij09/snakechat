package cc.snakechat.data.post.comment
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