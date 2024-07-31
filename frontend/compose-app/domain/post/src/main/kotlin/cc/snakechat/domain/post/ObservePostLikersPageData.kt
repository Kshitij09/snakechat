package cc.snakechat.domain.feed

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.common.SimplePagingSource
import cc.snakechat.domain.post.Liker
import cc.snakechat.domain.post.PostLikersFetcher
import cc.snakechat.domain.post.PostLikersRequest
import kotlinx.coroutines.flow.Flow

internal class ObservePostLikersPageData(
    private val fetcher: PostLikersFetcher,
) : ObservePagingData<String, Liker> {
    override fun observe(request: String): Flow<PagingData<Liker>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 2,
            initialLoadSize = 10,
        ),
        pagingSourceFactory = postLikersPagingSourceFactory(postId = request, fetcher),
    ).flow
}

internal fun postLikersPagingSourceFactory(
    postId: String,
    fetcher: PostLikersFetcher,
): () -> PagingSource<String, Liker> = {
    SimplePagingSource(
        fetcher = { fetcher.fetch(it.postId, it.offset) },
        requestBuilder = { PostLikersRequest(postId, it.key) }
    )
}
