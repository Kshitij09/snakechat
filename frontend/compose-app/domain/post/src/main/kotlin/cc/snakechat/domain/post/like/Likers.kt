package cc.snakechat.domain.post.like

class PostLikersRequest(
    val postId: String,
    val offset: String? = null,
)

class Liker(
    val followersCount: Int = 0,
    val id: String,
    val name: String,
    val profileUrl: String? = null,
)

class User(
    val id: String,
    val name: String,
    val profileUrl: String? = null,
)
