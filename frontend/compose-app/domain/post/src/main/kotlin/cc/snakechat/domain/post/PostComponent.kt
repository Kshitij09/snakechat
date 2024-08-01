package cc.snakechat.domain.post

import cc.snakechat.data.post.PostApi
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.post.comment.Comment
import cc.snakechat.domain.post.comment.ObservePostCommentsPageData
import cc.snakechat.domain.post.comment.PostCommentsFetcher
import cc.snakechat.domain.post.like.Liker
import cc.snakechat.domain.post.like.ObservePostLikersPageData
import cc.snakechat.domain.post.like.PostLikersFetcher
import me.tatarka.inject.annotations.Provides

interface PostComponent {
    @Provides
    fun provideObservePostLikersPageData(api: PostApi): ObservePagingData<String, Liker> {
        val fetcher = PostLikersFetcher(api)
        return ObservePostLikersPageData(fetcher)
    }

    @Provides
    fun provideObservePostCommentsPageData(api: PostApi): ObservePagingData<String, Comment> {
        val fetcher = PostCommentsFetcher(api)
        return ObservePostCommentsPageData(fetcher)
    }
}
