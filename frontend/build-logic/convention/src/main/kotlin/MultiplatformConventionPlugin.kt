import cc.snakechat.configureAndroid
import cc.snakechat.configureFlavors
import cc.snakechat.configureGradleManagedDevices
import cc.snakechat.configureJava
import cc.snakechat.configureKotlinJvmTarget
import cc.snakechat.configurePrintApksTask
import cc.snakechat.disableUnnecessaryAndroidTests
import cc.snakechat.libs
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
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
    private val targetsHandler =
        objectFactory.newInstance<TargetsHandler>(
            project,
            targets
        )

    fun targets(action: TargetsHandler.() -> Unit) {
        action(targetsHandler)
    }

    fun ksp(dependencyNotation: Any) {
        dependencyHandler.ksp(dependencyNotation)
    }
}

class KmpTargets(
    var jvm: Boolean = false,
)

abstract class TargetsHandler @Inject constructor(
    private val project: Project,
    private val targets: KmpTargets,
) {
    fun jvm() {
        targets.jvm = true
        with(project) {
            configure<KotlinMultiplatformExtension> {
                jvm {
                    configureJava()
                    configureKotlinJvmTarget()
                }
            }
        }
    }

    fun android(namespace: String) {
        with(project) {
            pluginManager.apply("com.android.library")
            configure<KotlinMultiplatformExtension> {
                androidTarget { configureKotlinJvmTarget() }
            }
            extensions.configure<LibraryExtension> {
                this.namespace = namespace
                configureAndroid(this)
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
                disableUnnecessaryAndroidTests(project)
            }
            dependencies {
                add("androidTestImplementation", kotlin("test"))
                add("testImplementation", kotlin("test"))

                add("implementation", libs.findLibrary("androidx.tracing.ktx").get())
            }
        }
    }
}

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