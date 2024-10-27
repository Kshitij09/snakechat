import cc.snakechat.configureJava
import cc.snakechat.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.newInstance
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
    objectFactory: ObjectFactory,
    private val project: Project
) {
    private val targets = KmpTargets()
    private val dependencyHandler =
        objectFactory.newInstance<KmpDependencyHandler>(
            project,
            targets
        )

    fun jvm() {
        targets.jvm = true
        with(project) {
            configure<KotlinMultiplatformExtension> {
                jvm {
                    configureJava()
                    configureKotlin<KotlinMultiplatformExtension>()
                }
            }
        }
    }

    fun ksp(dependencyNotation: Any) {
        dependencyHandler.ksp(dependencyNotation)
    }
}

class KmpTargets(
    var jvm: Boolean = false,
)

abstract class KmpDependencyHandler @Inject constructor(
    private val project: Project,
    private val targets: KmpTargets,
) {
    fun ksp(dependencyNotation: Any) {
        project.dependencies {
            if (targets.jvm) {
                add("kspJvm", dependencyNotation)
            }
        }
    }
}