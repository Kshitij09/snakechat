package cc.snakechat.data.profile

import android.content.Context
import cc.snakechat.data.network.NetworkError
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

class FakeProfileApi(private val context: Context, private val json: Json) : ProfileApi {
    override suspend fun getProfile(userId: String): Result<ProfileResponse, NetworkError> = context.assets.open("profile/profile_1.json").use {
        val response = json.decodeFromStream<ProfileResponse>(it)
        Ok(response)
    }

    override suspend fun getUserFollowings(userId: String, offset: String?): Result<FollowsResponse, NetworkError> = genericFollowsResponse(offset)

    override suspend fun getUserFollowers(userId: String, offset: String?): Result<FollowsResponse, NetworkError> = genericFollowsResponse(offset)

    private fun genericFollowsResponse(offset: String?): Result<FollowsResponse, NetworkError> {
        val assetName = if (offset == null) "follows/follows_1.json" else "follows/follows_2.json"
        return context.assets.open(assetName)
            .use { json.decodeFromStream<FollowsResponse>(it) }
            .let { Ok(it) }
    }
}
