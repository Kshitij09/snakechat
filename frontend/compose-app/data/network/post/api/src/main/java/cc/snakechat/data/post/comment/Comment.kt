package cc.snakechat.data.post.comment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Comment(
    val commenter: Commenter? = null,
    val id: String? = null,
    val likes: Int? = null,
    val text: String? = null,
    @SerialName("updated_at")
    val updatedAt: Long? = null
)