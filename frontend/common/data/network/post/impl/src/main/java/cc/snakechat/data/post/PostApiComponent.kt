package cc.snakechat.data.post

import io.ktor.client.HttpClient
import me.tatarka.inject.annotations.Provides

interface PostApiComponent {
    @Provides
    fun providePostApi(httpClient: HttpClient): PostApi = RealPostApi(httpClient)
}
