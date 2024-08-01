package cc.snakechat.data.post

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Liker(
    @SerialName("followers_count")
    val followersCount: Int? = null,
    val id: String? = null,
    val name: String? = null,
    @SerialName("profile_url")
    val profileUrl: String? = null,
)
