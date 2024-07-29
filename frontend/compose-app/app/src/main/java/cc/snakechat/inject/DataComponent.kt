package cc.snakechat.inject

import cc.snakechat.BytemaskConfig
import cc.snakechat.data.feed.FeedApiComponent
import cc.snakechat.ktorclient.ApiKey
import cc.snakechat.ktorclient.HttpClientComponent
import me.tatarka.inject.annotations.Provides

interface DataComponent :
    HttpClientComponent,
    FeedApiComponent {
    @Provides
    @ApplicationScope
    fun provideApiKey(): @ApiKey String = BytemaskConfig.API_KEY
}