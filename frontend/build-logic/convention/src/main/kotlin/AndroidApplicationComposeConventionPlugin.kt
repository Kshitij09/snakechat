import cc.snakechat.SnakeChatVersions
import com.android.build.api.dsl.ApplicationExtension
import cc.snakechat.configureAndroidCompose
import cc.snakechat.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val snakeLibs = SnakeChatVersions(libs, target)
            apply(snakeLibs.plugins.androidApplication)
            apply(snakeLibs.plugins.compose)

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
        }
    }

}
