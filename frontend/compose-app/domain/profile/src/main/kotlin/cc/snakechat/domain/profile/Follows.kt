package cc.snakechat.domain.profile

import java.time.LocalDateTime

class FollowsResponse(
    val follows: List<Follow> = emptyList(),
    val offset: String? = null,
    val total: Int = 0,
)

class FollowListRequest(
    val listType: ListType,
    val userId: String,
    val offset: String?
)

class Follow(
    val id: String,
    val name: String,
    val updatedAt: LocalDateTime,
    val profileUrl: String? = null,
)

enum class ListType {
    Followers,
    Following
}