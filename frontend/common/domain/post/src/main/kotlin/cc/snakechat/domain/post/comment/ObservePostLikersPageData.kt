package cc.snakechat.domain.post.comment

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.common.SimplePagingSource
import kotlinx.coroutines.flow.Flow

internal class ObservePostCommentsPageData(
    private val fetcher: PostCommentsFetcher,
) : ObservePagingData<String, Comment> {
    override fun observe(request: String): Flow<PagingData<Comment>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 2,
            initialLoadSize = 10,
        ),
        pagingSourceFactory = postCommentsPagingSourceFactory(postId = request, fetcher),
    ).flow
}

internal fun postCommentsPagingSourceFactory(
    postId: String,
    fetcher: PostCommentsFetcher,
): () -> PagingSource<String, Comment> = {
    SimplePagingSource(
        fetcher = { fetcher.fetch(it.postId, it.offset) },
        requestBuilder = { PostCommentsRequest(postId, it.key) },
    )
}
