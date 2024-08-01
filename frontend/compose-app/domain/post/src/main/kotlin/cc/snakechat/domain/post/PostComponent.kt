package cc.snakechat.domain.post

import cc.snakechat.data.post.PostApi
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.feed.ObservePostLikersPageData
import me.tatarka.inject.annotations.Provides

interface PostComponent {
    @Provides
    fun provideObservePostLikersPageData(api: PostApi): ObservePagingData<String, Liker> {
        val fetcher = PostLikersFetcher(api)
        return ObservePostLikersPageData(fetcher)
    }
}
