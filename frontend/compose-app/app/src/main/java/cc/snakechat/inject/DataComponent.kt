package cc.snakechat.inject

import cc.snakechat.BytemaskConfig
import cc.snakechat.ktorclient.ApiKey
import cc.snakechat.ktorclient.HttpClientComponent
import me.tatarka.inject.annotations.Provides

interface DataComponent : HttpClientComponent {
    @Provides
    @ApplicationScope
    fun provideApiKey(): @ApiKey String = BytemaskConfig.API_KEY
}