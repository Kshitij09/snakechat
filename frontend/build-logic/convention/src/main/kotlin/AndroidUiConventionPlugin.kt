import cc.snakechat.SnakeChatVersions
import cc.snakechat.configureGradleManagedDevices
import cc.snakechat.implementation
import cc.snakechat.libs
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidUiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val snakeLibs = SnakeChatVersions(libs, target)
            pluginManager.apply {
                apply(snakeLibs.plugins.snakechatAndroidLibraryCompose)
                apply(snakeLibs.plugins.snakechatKotlinInject)
                apply(snakeLibs.plugins.kotlinParcelize)
            }
            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
            }

            dependencies {
                implementation(snakeLibs.projects.uiDesign)
                implementation(snakeLibs.androidxTracingKtx)
                implementation(snakeLibs.circuitFoundation)
            }
        }
    }
}
