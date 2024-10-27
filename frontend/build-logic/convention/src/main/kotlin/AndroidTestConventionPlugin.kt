import cc.snakechat.SnakeChatVersions
import com.android.build.gradle.TestExtension
import cc.snakechat.configureGradleManagedDevices
import cc.snakechat.configureKotlinAndroid
import cc.snakechat.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val snakeLibs = SnakeChatVersions(libs, target)
            with(pluginManager) {
                apply("com.android.test")
                apply(snakeLibs.plugins.kotlinAndroid)
            }

            extensions.configure<TestExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
                configureGradleManagedDevices(this)
            }
        }
    }

}
