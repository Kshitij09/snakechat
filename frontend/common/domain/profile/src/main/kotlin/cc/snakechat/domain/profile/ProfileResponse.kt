package cc.snakechat.domain.profile

class Profile(
    val followersCount: Long = 0,
    val followingCount: Long = 0,
    val id: String,
    val postThumbnails: List<PostThumbnail> = emptyList(),
    val postsCount: Long = 0,
    val profileUrl: String? = null,
    val status: String? = null,
    val username: String,
)

class PostThumbnail(
    val id: String,
    val mediaUrl: String,
)
