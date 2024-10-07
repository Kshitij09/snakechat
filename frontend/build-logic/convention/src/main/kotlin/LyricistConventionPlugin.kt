import cc.snakechat.libs
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class LyricistConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")
            dependencies {
                add("implementation", libs.findLibrary("lyricist").get())
                add("ksp", libs.findLibrary("lyricist.processor").get())
            }
            configure<KspExtension> {
                arg("lyricist.generateStringsProperty", "true")
            }
        }
    }
}