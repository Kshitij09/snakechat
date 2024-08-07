package cc.snakechat.data.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class FollowsResponse(
    val follows: List<Follow>? = null,
    val offset: String? = null,
    val total: Int? = null,
)

@Serializable
class FollowListRequest(val offset: String)

@Serializable
class Follow(
    val id: String? = null,
    val name: String? = null,
    @SerialName("profile_url")
    val profileUrl: String? = null,
    @SerialName("updated_at")
    val updatedAt: Long? = null,
)
