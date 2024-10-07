import cc.snakechat.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KotlinInjectConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")
            dependencies {
                add("ksp", libs.findLibrary("kotlininject.compiler").get())
                add("implementation", libs.findLibrary("kotlininject.runtime").get())
                if (rootProject.name == "snakechat-common") {
                    add("implementation", project(":library:library-inject"))
                } else {
                    add("implementation", "cc.snakechat:library-inject:1.0.0")
                }
            }
        }
    }
}