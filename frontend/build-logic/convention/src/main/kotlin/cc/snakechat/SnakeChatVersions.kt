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
    val androidxComposeUiToolingPreview: Provider<MinimalExternalModuleDependency>
        get() = libs.findLibrary("androidx-compose-ui-tooling-preview").get()
    val androidxComposeUiTooling: Provider<MinimalExternalModuleDependency>
        get() = libs.findLibrary("androidx-compose-ui-tooling").get()
    val androidComposeBom: Provider<MinimalExternalModuleDependency>
        get() = libs.findLibrary("androidx-compose-bom").get()
    val androidxTracingKtx: Provider<MinimalExternalModuleDependency>
        get() = libs.findLibrary("androidx.tracing.ktx").get()
    val circuitFoundation: Provider<MinimalExternalModuleDependency>
        get() = libs.findLibrary("circuit.foundation").get()
    val lyricist: Provider<MinimalExternalModuleDependency>
        get() = libs.findLibrary("lyricist").get()
    val lyricistProcessor: Provider<MinimalExternalModuleDependency>
        get() = libs.findLibrary("lyricist.processor").get()
}

class SnakeChatPlugins(
    val ksp: String = "com.google.devtools.ksp",
    val kmp: String = "org.jetbrains.kotlin.multiplatform",
    val androidLibrary: String = "com.android.library",
    val androidApplication: String = "com.android.application",
    val compose: String = "org.jetbrains.kotlin.plugin.compose",
    val kotlinAndroid: String = "org.jetbrains.kotlin.android",
    val kotlinJvm: String = "org.jetbrains.kotlin.jvm",
    val kotlinParcelize: String = "kotlin-parcelize",
    val snakechatAndroidLibrary: String = "snakechat.android.library",
    val snakechatAndroidLibraryCompose: String = "snakechat.android.library.compose",
    val snakechatAndroidLint: String = "snakechat.android.lint",
    val snakechatKotlinInject: String = "snakechat.kotlininject",
    val dropboxDependencyGuard: String = "com.dropbox.dependency-guard",
)

class SnakeChatProjects(private val project: Project) {
    val libraryInject: ProjectDependency
        get() = project.dependencies.project(":library:library-inject")
    val uiDesign: ProjectDependency
        get() = project.dependencies.project(":ui:design")
}