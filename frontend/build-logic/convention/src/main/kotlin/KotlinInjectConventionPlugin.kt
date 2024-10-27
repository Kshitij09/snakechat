import cc.snakechat.SnakeChatVersions
import cc.snakechat.implementation
import cc.snakechat.ksp
import cc.snakechat.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinInjectConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val snakeLibs = SnakeChatVersions(target.libs, target)
        with(target) {
            pluginManager.apply(snakeLibs.plugins.ksp)
            val isKmpProject = pluginManager.hasPlugin(snakeLibs.plugins.kmp)
            if (isKmpProject) {
                configure<KotlinMultiplatformExtension> {
                    with(sourceSets) {
                        commonMain.dependencies {
                            implementation(snakeLibs.kotlininjectRuntime)
                            if (rootProject.name == "snakechat-common") {
                                implementation(snakeLibs.projects.libraryInject)
                            } else {
                                implementation(snakeLibs.libraryInject)
                            }
                        }
                    }
                }
                configure<SnakeMultiplatformExtension> {
                    ksp(snakeLibs.kotlininjectCompiler)
                }
            } else {
                dependencies {
                    ksp(snakeLibs.kotlininjectCompiler)
                    implementation(snakeLibs.kotlininjectRuntime)
                    if (rootProject.name == "snakechat-common") {
                        implementation(snakeLibs.projects.libraryInject)
                    } else {
                        implementation(snakeLibs.libraryInject)
                    }
                }
            }
        }
    }
}