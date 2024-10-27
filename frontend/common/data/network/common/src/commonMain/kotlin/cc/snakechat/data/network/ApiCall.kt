package cc.snakechat.data.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.utils.io.errors.IOException

suspend inline fun <reified T> apiCall(
    httpErrorMapper: HttpErrorMapper? = NullHttpErrorMapper,
    unknownExceptionMapper: UnknownExceptionMapper? = NullUnknownExceptionMapper,
    call: () -> HttpResponse,
): Result<T, NetworkError> {
    return try {
        val response = call()
        if (response.status == OK) {
            return Ok(response.body<T>())
        }
        val httpError = httpErrorMapper?.map(response.status)
        if (httpError != null) {
            return Err(httpError)
        }
        when {
            response.status == Unauthorized -> Err(AuthError)
            response.status.value >= InternalServerError.value -> Err(ServerError)
            else -> Err(UnknownHttpError(response.status.value))
        }
    } catch (e: Exception) {
        when (e) {
            is SocketTimeoutException,
            is ConnectTimeoutException,
            is HttpRequestTimeoutException,
            is IOException,
            -> Err(ConnectionError)
            else -> {
                val unknownError = unknownExceptionMapper?.map(e)
                if (unknownError != null) {
                    Err(unknownError)
                }
                Err(UnknownError(e))
            }
        }
    }
}

fun interface HttpErrorMapper {
    fun map(status: HttpStatusCode): NetworkError?
}

fun interface UnknownExceptionMapper {
    fun map(e: Exception): NetworkError?
}

val NullHttpErrorMapper = HttpErrorMapper { null }
val NullUnknownExceptionMapper = UnknownExceptionMapper { null }
