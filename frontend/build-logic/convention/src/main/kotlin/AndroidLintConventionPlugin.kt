import cc.snakechat.SnakeChatVersions
import cc.snakechat.libs
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.Lint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class AndroidLintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val snakeLibs = SnakeChatVersions(libs, target)
            when {
                pluginManager.hasPlugin(snakeLibs.plugins.androidApplication) ->
                    configure<ApplicationExtension> { lint(Lint::configure) }

                pluginManager.hasPlugin(snakeLibs.plugins.androidLibrary) ->
                    configure<LibraryExtension> { lint(Lint::configure) }

                else -> {
                    pluginManager.apply("com.android.lint")
                    configure<Lint>(Lint::configure)
                }
            }
            dependencies {
                add("lintChecks", project(":lint"))
            }
            tasks.configureEach {
                if (name.startsWith("assemble")) {
                    dependsOn("lint")
                }
            }
        }
    }
}

private fun Lint.configure() {
    xmlReport = true
    checkDependencies = true
    abortOnError = true
}