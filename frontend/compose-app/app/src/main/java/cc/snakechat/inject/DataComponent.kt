package cc.snakechat.inject

import cc.snakechat.BytemaskConfig
import cc.snakechat.data.feed.FeedApiComponent
import cc.snakechat.data.post.PostApiComponent
import cc.snakechat.data.profile.ProfileApiComponent
import cc.snakechat.domain.feed.FeedComponent
import cc.snakechat.domain.post.PostComponent
import cc.snakechat.ktorclient.ApiKey
import me.tatarka.inject.annotations.Provides

interface DataComponent :
    FeedApiComponent,
    FeedComponent,
    PostApiComponent,
    PostComponent,
    ProfileApiComponent {
    @Provides
    @ApplicationScope
    fun provideApiKey(): @ApiKey String = BytemaskConfig.API_KEY
}
