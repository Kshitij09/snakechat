package cc.snakechat.ktorclient

import cc.snakechat.inject.ApplicationScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides

interface HttpClientComponent {
    @Provides
    @ApplicationScope
    fun provideHttpClient(@ApiKey apiKey: String): HttpClient = buildHttpClient(apiKey)
}

internal fun buildHttpClient(apiKey: String): HttpClient {
    return HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                },
            )
        }
        install(Logging) {
            level = LogLevel.ALL
        }
        install(DefaultRequest) {
            header("X-API-KEY", apiKey)
        }
    }
}
