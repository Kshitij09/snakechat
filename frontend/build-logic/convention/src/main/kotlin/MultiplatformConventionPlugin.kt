import cc.snakechat.configureJava
import cc.snakechat.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import javax.inject.Inject

class MultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.multiplatform")
            extensions.create("snakeKmp", SnakeMultiplatformExtension::class.java, target)
        }
    }
}

open class SnakeMultiplatformExtension @Inject constructor(
    objects: ObjectFactory,
    private val project: Project,
) {
    fun jvm() {
        with(project) {
            configure<KotlinMultiplatformExtension> {
                jvm {
                    configureJava()
                    configureKotlin<KotlinMultiplatformExtension>()
                }
            }
        }
    }
}