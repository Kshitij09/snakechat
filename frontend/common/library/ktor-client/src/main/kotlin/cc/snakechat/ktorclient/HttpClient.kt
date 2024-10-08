package cc.snakechat.ktorclient

import cc.snakechat.inject.ApplicationScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides

interface HttpClientComponent {
    @Provides
    @ApplicationScope
    fun provideHttpClient(@ApiKey apiKey: String, json: Json): HttpClient = buildHttpClient(apiKey, json)
}

internal fun buildHttpClient(apiKey: String, json: Json): HttpClient = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(json)
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
    install(DefaultRequest) {
        header("X-API-KEY", apiKey)
        url("https://apis.snakechat.cc/")
        contentType(ContentType.Application.Json)
    }
}
