package cc.snakechat.domain.post.comment

import java.time.LocalDateTime

class PostCommentsRequest(
    val postId: String,
    val offset: String? = null
)

class Commenter(
    val id: String,
    val name: String,
    val profileUrl: String? = null
)

class Comment(
    val commenter: Commenter,
    val id: String,
    val likes: Long = 0,
    val text: String,
    val updatedAt: LocalDateTime
)
