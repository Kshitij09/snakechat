package cc.snakechat.domain.profile

import cc.snakechat.data.network.ConnectionError
import cc.snakechat.data.profile.ProfileApi
import cc.snakechat.data.profile.ProfileResponse
import cc.snakechat.domain.common.DomainError
import cc.snakechat.domain.common.Failure
import cc.snakechat.domain.common.NoInternet
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class GetUserProfile(private val api: ProfileApi) {
    suspend fun execute(userId: String): Result<Profile, DomainError> = withContext(Dispatchers.IO) {
        val result = api.getProfile(userId)
        if (result.isOk) {
            val profile = result.value.toDomain()
            if (profile != null) Ok(profile) else Err(Failure)
        } else {
            when (result.error) {
                is cc.snakechat.data.profile.UserNotFound -> Err(UserNotFound)
                is ConnectionError -> Err(NoInternet)
                else -> Err(Failure)
            }
        }
    }

    private fun ProfileResponse.toDomain(): Profile? {
        val id = id ?: return null
        val username = username ?: return null
        return Profile(
            followersCount = followersCount ?: 0,
            followingCount = followingCount ?: 0,
            id = id,
            postsCount = postsCount ?: 0,
            postThumbnails = postThumbnails?.mapNotNull { it.toDomain() } ?: emptyList(),
            profileUrl = profileUrl,
            status = status,
            username = username,
        )
    }

    private fun cc.snakechat.data.profile.PostThumbnail.toDomain(): PostThumbnail? {
        val id = id ?: return null
        val url = mediaUrl ?: return null
        return PostThumbnail(
            id = id,
            mediaUrl = url,
        )
    }
}

object UserNotFound : DomainError
