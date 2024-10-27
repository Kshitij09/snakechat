package cc.snakechat.inject

import android.app.Activity
import android.app.Application
import android.content.Context
import coil3.PlatformContext
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@ActivityScope
abstract class AndroidActivityComponent(
    @get:Provides val activity: Activity,
    @Component val applicationComponent: AndroidApplicationComponent,
) : UiComponent {

    companion object
}

@Component
@ApplicationScope
abstract class AndroidApplicationComponent(
    @get:Provides val application: Application,
) : DataComponent,
    LibrariesComponent {
    @get:Provides
    val context: Context = application

    @get:Provides
    val coilContext: PlatformContext = application
    companion object
}
