package cc.snakechat.imageloading

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import cc.snakechat.inject.ApplicationScope
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.test.FakeImageLoaderEngine
import coil3.test.default
import me.tatarka.inject.annotations.Provides

interface ImageLoadingComponent {
    val imageLoader: ImageLoader

    @Provides
    @ApplicationScope
    fun provideImageLoader(context: PlatformContext): ImageLoader {
        val engine = FakeImageLoaderEngine.Builder()
            .default(ColorDrawable(Color.BLUE))
            .build()
        return ImageLoader.Builder(context)
            .components { add(engine) }
            .build()
    }
}
