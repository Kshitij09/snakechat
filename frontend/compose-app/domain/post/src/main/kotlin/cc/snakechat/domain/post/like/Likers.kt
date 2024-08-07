package cc.snakechat.domain.post.like

import cc.snakechat.domain.model.common.ContentId

class LikersRequest(
    val contentId: ContentId,
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
