package cc.snakechat.domain.post.comment

import androidx.paging.PagingSource.LoadResult
import cc.snakechat.data.post.PostApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Inject
internal class PostCommentsFetcher(private val api: PostApi) {
    private val visitedItems = mutableSetOf<String>()

    suspend fun fetch(postId: String, offset: String? = null): LoadResult<String, Comment> {
        return withContext(Dispatchers.IO) {
            // TODO: Better error handling
            val resp = api.getPostComments(postId, offset)
            val upstreamItems = resp.comments
            if (upstreamItems.isNullOrEmpty()) {
                return@withContext LoadResult.Error(NoSuchElementException("No more items"))
            }
            val items = uniqueItems(upstreamItems)
            val nextKey = if (items.isNotEmpty()) resp.offset else null
            LoadResult.Page(
                nextKey = nextKey,
                data = items,
                prevKey = offset,
            )
        }
    }

    private fun uniqueItems(items: List<cc.snakechat.data.post.comment.Comment>): List<Comment> = items.asSequence()
        .filterNot { visitedItems.contains(it.id) }
        .onEach { p -> p.id?.let { visitedItems.add(it) } }
        .mapNotNull { it.toDomain() }
        .toList()

    private fun cc.snakechat.data.post.comment.Comment.toDomain(): Comment? {
        val id = id ?: return null
        val commenter = commenter?.toDomain() ?: return null
        val text = text ?: return null
        val updatedAt = updatedAt?.let {
            Instant.ofEpochMilli(it)
        }.let {
            LocalDateTime.ofInstant(it, ZoneId.systemDefault())
        } ?: return null

        return Comment(
            id = id,
            commenter = commenter,
            text = text,
            updatedAt = updatedAt,
            likes = likes ?: 0,
        )
    }

    private fun cc.snakechat.data.post.comment.Commenter.toDomain(): Commenter? {
        val id = id ?: return null
        val name = name ?: return null
        return Commenter(
            id = id,
            name = name,
            profileUrl = profileUrl,
        )
    }
}
