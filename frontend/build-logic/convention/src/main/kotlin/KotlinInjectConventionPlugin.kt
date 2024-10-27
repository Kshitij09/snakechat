import cc.snakechat.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinInjectConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")
            val isKmpProject = pluginManager.hasPlugin("org.jetbrains.kotlin.multiplatform")
            if (isKmpProject) {
                configure<KotlinMultiplatformExtension> {
                    with(sourceSets) {
                        commonMain.dependencies {
                            implementation(libs.findLibrary("kotlininject.runtime").get())
                            if (rootProject.name == "snakechat-common") {
                                implementation(project(":library:library-inject"))
                            } else {
                                implementation("cc.snakechat:library-inject:1.0.0")
                            }
                        }
                    }
                }
                configure<SnakeMultiplatformExtension> {
                    ksp(libs.findLibrary("kotlininject.compiler").get())
                }
            } else {
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
}