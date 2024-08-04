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
}
