package cc.snakechat.domain.post

import androidx.paging.PagingSource.LoadResult
import cc.snakechat.data.post.PostApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
internal class PostLikersFetcher(private val api: PostApi) {
    private val visitedItems = mutableSetOf<String>()

    suspend fun fetch(postId: String, offset: String? = null): LoadResult<String, Liker> {
        return withContext(Dispatchers.IO) {
            // TODO: Better error handling
            val resp = api.getPostLikers(postId, offset)
            val upstreamItems = resp.likers
            if (upstreamItems.isNullOrEmpty()) {
                return@withContext LoadResult.Error(NoSuchElementException("No more items"))
            }
            val posts = uniqueItems(upstreamItems)
            val nextKey = if (posts.isNotEmpty()) resp.offset else null
            LoadResult.Page(
                nextKey = nextKey,
                data = posts,
                prevKey = offset,
            )
        }
    }

    private fun uniqueItems(items: List<cc.snakechat.data.post.like.Liker?>): List<Liker> = items.asSequence()
        .filterNotNull()
        .filterNot { visitedItems.contains(it.id) }
        .onEach { p -> p.id?.let { visitedItems.add(it) } }
        .mapNotNull { it.toDomain() }
        .toList()

    private fun cc.snakechat.data.post.like.Liker.toDomain(): Liker? {
        val id = id ?: return null
        val name = name ?: return null
        return Liker(
            followersCount = followersCount ?: 0,
            id = id,
            name = name,
            profileUrl = profileUrl,
        )
    }
}
