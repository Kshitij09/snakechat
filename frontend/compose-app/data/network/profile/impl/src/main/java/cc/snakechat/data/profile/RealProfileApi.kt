package cc.snakechat.data.profile

import cc.snakechat.data.network.AuthError
import cc.snakechat.data.network.ConnectionError
import cc.snakechat.data.network.NetworkError
import cc.snakechat.data.network.ServerError
import cc.snakechat.data.network.UnknownError
import cc.snakechat.data.network.UnknownHttpError
import cc.snakechat.data.network.UserNotFound
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.NullBody
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RealProfileApi(private val httpClient: HttpClient) : ProfileApi {
    override suspend fun getProfile(userId: String): Result<ProfileResponse, NetworkError> {
        return withContext(Dispatchers.IO) {
            try {
                val response = httpClient.post("/profile/$userId") {
                    setBody(NullBody)
                }
                when (response.status) {
                    HttpStatusCode.OK -> Ok(response.body<ProfileResponse>())
                    HttpStatusCode.NotFound -> Err(UserNotFound)
                    HttpStatusCode.Unauthorized -> Err(AuthError)
                    else -> {
                        if (response.status.value >= HttpStatusCode.InternalServerError.value) {
                            Err(ServerError)
                        } else {
                            Err(UnknownHttpError(response.status.value))
                        }
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException,
                    is ConnectTimeoutException,
                    is HttpRequestTimeoutException,
                    is IOException -> Err(ConnectionError)
                    else -> Err(UnknownError(e))
                }
            }
        }
    }
}
