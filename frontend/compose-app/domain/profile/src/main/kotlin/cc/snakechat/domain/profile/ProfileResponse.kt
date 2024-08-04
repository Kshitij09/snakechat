package cc.snakechat.domain.profile

class Profile(
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val id: String,
    val postThumbnails: List<PostThumbnail> = emptyList(),
    val postsCount: Int = 0,
    val profileUrl: String? = null,
    val status: String? = null,
    val username: String
)

class PostThumbnail(
    val id: String,
    val mediaUrl: String
)
