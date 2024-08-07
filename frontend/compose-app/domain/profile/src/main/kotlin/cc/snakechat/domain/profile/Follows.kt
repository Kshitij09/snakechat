package cc.snakechat.domain.profile

import cc.snakechat.domain.model.common.FollowListType
import java.time.LocalDateTime

class FollowsResponse(
    val follows: List<Follow> = emptyList(),
    val offset: String? = null,
    val total: Int = 0,
)

class FollowListRequest(
    val listType: FollowListType,
    val userId: String,
    val offset: String?
)

class Follow(
    val id: String,
    val name: String,
    val updatedAt: LocalDateTime,
    val profileUrl: String? = null,
)

