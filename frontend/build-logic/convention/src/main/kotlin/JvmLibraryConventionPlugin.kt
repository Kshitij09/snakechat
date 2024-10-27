import cc.snakechat.SnakeChatVersions
import cc.snakechat.configureKotlinJvm
import cc.snakechat.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val snakeLibs = SnakeChatVersions(libs, target)
            with(pluginManager) {
                apply(snakeLibs.plugins.kotlinJvm)
            }
            configureKotlinJvm()
        }
    }
}
