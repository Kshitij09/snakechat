import cc.snakechat.SnakeChatVersions
import cc.snakechat.configureAndroidCompose
import cc.snakechat.libs
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val snakeLibs = SnakeChatVersions(libs, target)
            apply(snakeLibs.plugins.snakechatAndroidLibrary)
            apply(snakeLibs.plugins.compose)

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }

}
