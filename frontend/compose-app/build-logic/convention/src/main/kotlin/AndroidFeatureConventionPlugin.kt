import com.android.build.gradle.LibraryExtension
import cc.snakechat.configureGradleManagedDevices
import cc.snakechat.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("snakechat.android.library")
                apply("snakechat.hilt")
            }
            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
            }

            dependencies {
                add("implementation", project("ui:designsystem"))
                add("implementation", libs.findLibrary("androidx.tracing.ktx").get())
            }
        }
    }
}
