package cc.snakechat.data.profile

interface ProfileApi {
    fun getProfile(userId: String): ProfileResponse
}