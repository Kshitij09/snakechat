package cc.snakechat.imageloading

import android.content.Context
import cc.snakechat.inject.ApplicationScope
import coil3.ImageLoader
import coil3.memory.MemoryCache
import me.tatarka.inject.annotations.Provides

interface ImageLoadingComponent {
    val imageLoader: ImageLoader

    @Provides
    @ApplicationScope
    fun provideImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, percent = 0.25)
                    .build()
            }.build()
    }
}