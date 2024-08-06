package cc.snakechat.domain.post

import cc.snakechat.data.post.PostApi
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.model.liker.ContentId
import cc.snakechat.domain.post.comment.Comment
import cc.snakechat.domain.post.comment.ObservePostCommentsPageData
import cc.snakechat.domain.post.comment.PostCommentsFetcher
import cc.snakechat.domain.post.like.Liker
import cc.snakechat.domain.post.like.LikersFetcher
import cc.snakechat.domain.post.like.ObservePostLikersPageData
import me.tatarka.inject.annotations.Provides

interface PostComponent {
    @Provides
    fun provideObservePostLikersPageData(api: PostApi): ObservePagingData<ContentId, Liker> {
        val fetcher = LikersFetcher(api)
        return ObservePostLikersPageData(fetcher)
    }

    @Provides
    fun provideObservePostCommentsPageData(api: PostApi): ObservePagingData<String, Comment> {
        val fetcher = PostCommentsFetcher(api)
        return ObservePostCommentsPageData(fetcher)
    }
}
