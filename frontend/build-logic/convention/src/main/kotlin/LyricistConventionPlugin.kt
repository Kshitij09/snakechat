import cc.snakechat.SnakeChatVersions
import cc.snakechat.implementation
import cc.snakechat.ksp
import cc.snakechat.libs
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class LyricistConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val snakeLibs = SnakeChatVersions(libs, target)
            pluginManager.apply(snakeLibs.plugins.ksp)
            dependencies {
                implementation(snakeLibs.lyricist)
                ksp(snakeLibs.lyricistProcessor)
            }
            configure<KspExtension> {
                arg("lyricist.generateStringsProperty", "true")
            }
        }
    }
}