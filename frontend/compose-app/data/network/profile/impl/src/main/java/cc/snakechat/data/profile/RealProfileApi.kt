package cc.snakechat.data.profile

import cc.snakechat.data.network.HttpErrorMapper
import cc.snakechat.data.network.NetworkError
import cc.snakechat.data.network.apiCall
import com.github.michaelbull.result.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.content.NullBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class RealProfileApi(private val httpClient: HttpClient) : ProfileApi {
    override suspend fun getProfile(userId: String): Result<ProfileResponse, NetworkError> = withContext(Dispatchers.IO) {
        apiCall<ProfileResponse>(
            httpErrorMapper = userNotFoundMapper(),
        ) {
            httpClient.post("/v1/user/$userId") {
                setBody(NullBody)
            }
        }
    }

    override suspend fun getUserFollowers(
        userId: String,
        offset: String?,
    ): Result<FollowsResponse, NetworkError> = apiCall<FollowsResponse> {
        httpClient.post("/v1/users/$userId/followers") {
            if (offset != null) {
                setBody(FollowListRequest(offset))
            } else {
                setBody(NullBody)
            }
        }
    }

    override suspend fun getUserFollowings(
        userId: String,
        offset: String?,
    ): Result<FollowsResponse, NetworkError> = apiCall<FollowsResponse> {
        httpClient.post("/v1/users/$userId/following") {
            if (offset != null) {
                setBody(FollowListRequest(offset))
            } else {
                setBody(NullBody)
            }
        }
    }

    private fun userNotFoundMapper(): HttpErrorMapper = HttpErrorMapper {
        if (it == NotFound) UserNotFound else null
    }
}
