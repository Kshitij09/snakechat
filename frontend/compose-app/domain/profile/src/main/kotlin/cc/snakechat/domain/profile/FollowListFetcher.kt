package cc.snakechat.domain.profile

import androidx.paging.PagingSource.LoadResult
import cc.snakechat.data.profile.ProfileApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Inject
internal class FollowListFetcher(private val api: ProfileApi) {
    private val visitedItems = mutableSetOf<String>()

    suspend fun fetch(request: FollowListRequest): LoadResult<String, Follow> {
        return withContext(Dispatchers.IO) {
            val result = if (request.listType == ListType.Followers) {
                api.getUserFollowers(request.userId, request.offset)
            } else {
                api.getUserFollowings(request.userId, request.offset)
            }
            if (result.isOk) {
                val resp = result.value
                val upstreamItems = resp.follows?.let { uniqueItems(it) }
                if (upstreamItems.isNullOrEmpty()) {
                    return@withContext LoadResult.Error(NoSuchElementException("No more items"))
                }
                val nextKey = if (upstreamItems.isNotEmpty()) resp.offset else null
                LoadResult.Page(
                    nextKey = nextKey,
                    data = upstreamItems,
                    prevKey = request.offset,
                )
            } else {
                LoadResult.Error(Throwable("Something went wrong"))
            }
        }
    }

    private fun uniqueItems(items: List<cc.snakechat.data.profile.Follow>): List<Follow> = items.asSequence()
        .filterNot { visitedItems.contains(it.id) }
        .onEach { p -> p.id?.let { visitedItems.add(it) } }
        .mapNotNull { it.toDomain() }
        .toList()

    private fun cc.snakechat.data.profile.Follow.toDomain(): Follow? {
        val id = id ?: return null
        val name = name ?: return null
        val updatedAt = updatedAt?.let {
            Instant.ofEpochMilli(it)
        }.let {
            LocalDateTime.ofInstant(it, ZoneId.systemDefault())
        } ?: return null

        return Follow(
            id = id,
            name = name,
            profileUrl = profileUrl,
            updatedAt = updatedAt,
        )
    }
}
