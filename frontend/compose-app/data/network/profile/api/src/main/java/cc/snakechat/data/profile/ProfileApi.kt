package cc.snakechat.data.profile

import cc.snakechat.data.network.NetworkError
import com.github.michaelbull.result.Result

interface ProfileApi {
    suspend fun getProfile(userId: String): Result<ProfileResponse, NetworkError>
    suspend fun getUserFollowers(userId: String, offset: String? = null): Result<FollowsResponse, NetworkError>
    suspend fun getUserFollowings(userId: String, offset: String? = null): Result<FollowsResponse, NetworkError>
}

object UserNotFound : NetworkError
