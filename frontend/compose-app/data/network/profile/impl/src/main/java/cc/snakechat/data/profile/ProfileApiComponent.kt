package cc.snakechat.data.profile

import io.ktor.client.HttpClient
import me.tatarka.inject.annotations.Provides

interface ProfileApiComponent {
    @Provides
    fun provideProfileApi(httpClient: HttpClient): ProfileApi = RealProfileApi(httpClient)
}
