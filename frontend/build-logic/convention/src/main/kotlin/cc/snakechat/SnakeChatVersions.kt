package cc.snakechat

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.project

class SnakeChatVersions(private val libs: VersionCatalog, project: Project) {
    val plugins = SnakeChatPlugins()
    val projects = SnakeChatProjects(project)
    val kotlininjectRuntime: Provider<MinimalExternalModuleDependency>
        get() = libs.findLibrary("kotlininject-runtime").get()

    val kotlininjectCompiler: Provider<MinimalExternalModuleDependency>
        get() = libs.findLibrary("kotlininject-compiler").get()

    val libraryInject: String get() = "cc.snakechat:library-inject:1.0.0"
}

class SnakeChatPlugins(
    val ksp: String = "com.google.devtools.ksp",
    val kmp: String = "org.jetbrains.kotlin.multiplatform"
)

class SnakeChatProjects(private val project: Project) {
    val libraryInject: ProjectDependency
        get() = project.dependencies.project(":library:library-inject")
}