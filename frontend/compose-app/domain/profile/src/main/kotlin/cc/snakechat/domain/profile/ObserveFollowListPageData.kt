package cc.snakechat.domain.profile

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.common.SimplePagingSource
import cc.snakechat.domain.model.common.FollowListType
import kotlinx.coroutines.flow.Flow

internal class ObserveFollowingListPageData(
    private val fetcher: FollowListFetcher,
) : ObservePagingData<String, Follow> {
    override fun observe(request: String): Flow<PagingData<Follow>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 2,
            initialLoadSize = 10,
        ),
        pagingSourceFactory = followListPagingSourceFactory(listType = FollowListType.Following, postId = request, fetcher),
    ).flow
}

internal class ObserveFollowerListPageData(
    private val fetcher: FollowListFetcher,
) : ObservePagingData<String, Follow> {
    override fun observe(request: String): Flow<PagingData<Follow>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 2,
            initialLoadSize = 10,
        ),
        pagingSourceFactory = followListPagingSourceFactory(listType = FollowListType.Followers, postId = request, fetcher),
    ).flow
}

internal fun followListPagingSourceFactory(
    listType: FollowListType,
    postId: String,
    fetcher: FollowListFetcher,
): () -> PagingSource<String, Follow> = {
    SimplePagingSource(
        fetcher = { fetcher.fetch(it) },
        requestBuilder = { FollowListRequest(listType, postId, it.key) },
    )
}
