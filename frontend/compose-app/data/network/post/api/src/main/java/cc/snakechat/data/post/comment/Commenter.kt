package cc.snakechat.data.post.comment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Commenter(
    val id: String? = null,
    val name: String? = null,
    @SerialName("profile_url")
    val profileUrl: String? = null
)