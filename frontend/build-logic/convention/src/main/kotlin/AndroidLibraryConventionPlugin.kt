import cc.snakechat.SnakeChatVersions
import cc.snakechat.androidTestImplementation
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import cc.snakechat.configureFlavors
import cc.snakechat.configureGradleManagedDevices
import cc.snakechat.configureKotlinAndroid
import cc.snakechat.configurePrintApksTask
import cc.snakechat.disableUnnecessaryAndroidTests
import cc.snakechat.implementation
import cc.snakechat.libs
import cc.snakechat.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val snakeLibs = SnakeChatVersions(libs, target)
            with(pluginManager) {
                apply(snakeLibs.plugins.androidLibrary)
                apply(snakeLibs.plugins.kotlinAndroid)
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testOptions.animationsDisabled = true
                configureFlavors(this)
                configureGradleManagedDevices(this)
                // The resource prefix is derived from the module name,
                // so resources inside ":core:module1" must be prefixed with "core_module1_"
                resourcePrefix = path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = "_").lowercase() + "_"
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                configurePrintApksTask(this)
                disableUnnecessaryAndroidTests(target)
            }
            dependencies {
                androidTestImplementation(kotlin("test"))
                testImplementation(kotlin("test"))
                implementation(snakeLibs.androidxTracingKtx)
            }
        }
    }
}
