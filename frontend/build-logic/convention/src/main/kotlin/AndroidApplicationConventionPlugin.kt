import cc.snakechat.SnakeChatVersions
import cc.snakechat.configureGradleManagedDevices
import cc.snakechat.configureKotlinAndroid
import cc.snakechat.configurePrintApksTask
import cc.snakechat.libs
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val snakeLibs = SnakeChatVersions(libs, target)
            with(pluginManager) {
                apply(snakeLibs.plugins.androidApplication)
                apply(snakeLibs.plugins.kotlinAndroid)
                apply(snakeLibs.plugins.snakechatAndroidLint)
                apply(snakeLibs.plugins.snakechatKotlinInject)
                apply(snakeLibs.plugins.dropboxDependencyGuard)
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
                @Suppress("UnstableApiUsage")
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
            }
            extensions.configure<ApplicationAndroidComponentsExtension> {
                configurePrintApksTask(this)
            }
        }
    }

}
