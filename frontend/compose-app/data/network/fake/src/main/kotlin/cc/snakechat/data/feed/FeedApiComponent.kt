package cc.snakechat.data.feed

import android.content.Context
import cc.snakechat.inject.ApplicationScope
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides

interface FeedApiComponent {
    @Provides
    @ApplicationScope
    fun provideFeedApi(context: Context, json: Json): FeedApi = FakeFeedApi(context, json)
}