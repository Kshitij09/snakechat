package cc.snakechat.data.post

import android.content.Context
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides

interface PostApiComponent {
    @Provides
    fun providePostApi(context: Context, json: Json): PostApi = FakePostApi(context, json)
}
