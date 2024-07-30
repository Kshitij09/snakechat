package cc.snakechat.inject

import cc.snakechat.BytemaskConfig
import cc.snakechat.data.feed.FeedApiComponent
import cc.snakechat.domain.feed.FeedComponent
import cc.snakechat.ktorclient.ApiKey
import me.tatarka.inject.annotations.Provides

interface DataComponent :
    FeedApiComponent,
    FeedComponent {
    @Provides
    @ApplicationScope
    fun provideApiKey(): @ApiKey String = BytemaskConfig.API_KEY
}
