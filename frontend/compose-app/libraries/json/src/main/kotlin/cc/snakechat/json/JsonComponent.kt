package cc.snakechat.json

import cc.snakechat.inject.ApplicationScope
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides

interface JsonComponent {

    @Provides
    @ApplicationScope
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
}
