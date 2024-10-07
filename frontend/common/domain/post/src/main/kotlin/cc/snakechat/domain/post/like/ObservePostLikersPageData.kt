package cc.snakechat.domain.post.like

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.common.SimplePagingSource
import cc.snakechat.domain.model.common.ContentId
import kotlinx.coroutines.flow.Flow

internal class ObservePostLikersPageData(
    private val fetcher: LikersFetcher,
) : ObservePagingData<ContentId, Liker> {
    override fun observe(request: ContentId): Flow<PagingData<Liker>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 2,
            initialLoadSize = 10,
        ),
        pagingSourceFactory = postLikersPagingSourceFactory(contentId = request, fetcher),
    ).flow
}

internal fun postLikersPagingSourceFactory(
    contentId: ContentId,
    fetcher: LikersFetcher,
): () -> PagingSource<String, Liker> = {
    SimplePagingSource(
        fetcher = { fetcher.fetch(it.contentId, it.offset) },
        requestBuilder = { LikersRequest(contentId, it.key) },
    )
}
