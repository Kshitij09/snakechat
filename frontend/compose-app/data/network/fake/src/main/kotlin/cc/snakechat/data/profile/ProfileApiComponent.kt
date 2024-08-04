package cc.snakechat.data.profile

import android.content.Context
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides

interface ProfileApiComponent {
    @Provides
    fun provideProfileApi(context: Context, json: Json): ProfileApi = FakeProfileApi(context, json)
}