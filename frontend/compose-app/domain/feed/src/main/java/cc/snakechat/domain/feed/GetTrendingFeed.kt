package cc.snakechat.domain.feed

import androidx.paging.PagingSource
import cc.snakechat.data.feed.FeedApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Inject
internal class TrendingFeedFetcher(private val api: FeedApi) {
    private val visitedPostIds = mutableSetOf<String>()

    suspend fun fetch(request: TrendingFeedRequest? = null): PagingSource.LoadResult<String, Post> {

        return withContext(Dispatchers.IO) {
            val currentOffset = request?.offset
            val req = cc.snakechat.data.feed.TrendingFeedRequest(currentOffset)
            // TODO: Better error handling
            val resp = api.getTrendingFeed(req)
            val upstreamPosts = resp.posts
            if (upstreamPosts.isNullOrEmpty()) return@withContext PagingSource.LoadResult.Error(NoSuchElementException("No more posts"))
            val posts = uniquePosts(upstreamPosts)
            val nextKey = if (posts.isNotEmpty()) resp.offset else null
            PagingSource.LoadResult.Page(
                nextKey = nextKey,
                data = posts,
                prevKey = request?.offset,
            )
        }
    }

    private fun uniquePosts(posts: List<cc.snakechat.data.feed.Post?>): List<Post> {
        return posts.asSequence()
            .filterNotNull()
            .filterNot { visitedPostIds.contains(it.id) }
            .onEach { p -> p.id?.let { visitedPostIds.add(it) } }
            .mapNotNull { it.toDomain() }
            .toList()
    }

    private fun cc.snakechat.data.feed.Post.toDomain(): Post? {
        val user = user?.toDomain() ?: return null
        val createdAt = createdAt
        val id = id ?: return null
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