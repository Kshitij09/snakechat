package cc.snakechat.data.feed
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TrendingFeedResponse(
    val offset: String? = null,
    @SerialName("page_size")
    val pageSize: Int? = null,
    val posts: List<Post?>? = null
)

@Serializable
class Post(
    val caption: String? = null,
    val comments: Int? = null,
    @SerialName("created_at")
    val createdAt: Int? = null,
    val downloads: Int? = null,
    val id: String? = null,
    val likes: Int? = null,
    @SerialName("media_url")
    val mediaUrl: String? = null,
    val saves: Int? = null,
    val shares: Int? = null,
    @SerialName("tag_id")
    val tagId: String? = null,
    val user: User? = null,
    val views: Int? = null
)

@Serializable
class User(
    val id: String? = null,
    val name: String? = null,
    @SerialName("profile_url")
    val profileUrl: String? = null
)
