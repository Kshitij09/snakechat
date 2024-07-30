package cc.snakechat.domain.feed

import java.time.LocalDateTime

class TrendingFeedRequest(
    val offset: String? = null
)

class Post(
    val caption: String? = null,
    val comments: Long = 0,
    val createdAt: LocalDateTime? = null,
    val downloads: Long = 0,
    val id: String,
    val likes: Long = 0,
    val mediaUrl: String? = null,
    val saves: Long = 0,
    val shares: Long = 0,
    val tagId: String? = null,
    val user: User,
    val views: Long = 0,
)

class User(
    val id: String,
    val name: String,
    val profileUrl: String? = null
)
