package cc.snakechat.data.feed
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TrendingFeedResponse(
    var offset: String? = null,
    @SerialName("page_size")
    val pageSize: Int? = null,
    val posts: List<Post?>? = null
)

@Serializable
class Post(
    val caption: String? = null,
    val comments: Long? = null,
    @SerialName("created_at")
    val createdAt: Long? = null,
    val downloads: Long? = null,
    val id: String? = null,
    val likes: Long? = null,
    @SerialName("media_url")
    val mediaUrl: String? = null,
    val saves: Long? = null,
    val shares: Long? = null,
    @SerialName("tag_id")
    val tagId: String? = null,
    val user: User? = null,
    val views: Long? = null
)

@Serializable
class User(
    val id: String? = null,
    val name: String? = null,
    @SerialName("profile_url")
    val profileUrl: String? = null
)
