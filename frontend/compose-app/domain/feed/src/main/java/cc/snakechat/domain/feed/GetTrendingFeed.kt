package cc.snakechat.domain.feed

import cc.snakechat.data.feed.FeedApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Inject
class GetTrendingFeed(private val api: FeedApi) {
    suspend fun execute(request: TrendingFeedRequest? = null): TrendingFeed {
        return withContext(Dispatchers.IO) {
            val req = cc.snakechat.data.feed.TrendingFeedRequest(request?.offset)
            api.getTrendingFeed(req).toDomain()
        }
    }

    private fun cc.snakechat.data.feed.TrendingFeedResponse.toDomain(): TrendingFeed {
        return TrendingFeed(
            offset = offset,
            posts = posts?.mapNotNull { it?.toDomain() } ?: emptyList(),
        )
    }

    private fun cc.snakechat.data.feed.Post.toDomain(): Post? {
        val user = user?.toDomain() ?: return null
        val createdAt = createdAt
        return Post(
            caption = caption,
            comments = comments ?: 0,
            createdAt = if (createdAt != null && createdAt != 0L) localDateTimeOf(createdAt) else null,
            downloads = downloads ?: 0,
            id = id,
            likes = likes ?: 0,
            mediaUrl = mediaUrl,
            saves = saves ?: 0,
            shares = shares ?: 0,
            tagId = tagId,
            user = user,
            views = views ?: 0,
        )
    }

    private fun cc.snakechat.data.feed.User.toDomain(): User? {
        val id = id ?: return null
        val name = name ?: return null
        return User(
            id = id,
            name = name,
            profileUrl = profileUrl,
        )
    }

    private fun localDateTimeOf(epochMilli: Long): LocalDateTime {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(epochMilli),
            ZoneId.systemDefault()
        )
    }
}