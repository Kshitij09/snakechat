package cc.snakechat.data.network

interface NetworkError

object AuthError: NetworkError
object ConnectionError : NetworkError
class UnknownHttpError(val status: Int) : NetworkError
object ServerError : NetworkError
class UnknownError(val e: Exception? = null) :
    NetworkError
