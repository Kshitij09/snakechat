package cc.snakechat.data.profile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ProfileResponse(
    @SerialName("followers_count")
    val followersCount: Int? = null,
    @SerialName("following_count")
    val followingCount: Int? = null,
    val id: String? = null,
    @SerialName("post_thumbnails")
    val postThumbnails: List<PostThumbnail>? = null,
    @SerialName("posts_count")
    val postsCount: Int? = null,
    @SerialName("profile_url")
    val profileUrl: String? = null,
    val status: String? = null,
    val username: String? = null
)

@Serializable
class PostThumbnail(
    val id: String? = null,
    @SerialName("media_url")
    val mediaUrl: String? = null
)
