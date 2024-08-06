package cc.snakechat.domain.post.like

import androidx.paging.PagingSource.LoadResult
import cc.snakechat.data.post.PostApi
import cc.snakechat.domain.model.liker.ContentId
import cc.snakechat.domain.model.liker.PostId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
internal class LikersFetcher(private val api: PostApi) {
    private val visitedItems = mutableSetOf<String>()

    suspend fun fetch(contentId: ContentId, offset: String? = null): LoadResult<String, Liker> {
        return withContext(Dispatchers.IO) {
            // TODO: Better error handling
            val resp = if (contentId is PostId) {
                api.getPostLikers(contentId.id, offset)
            } else {
                api.getCommentLikers(contentId.id, offset)
            }
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
