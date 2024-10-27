package cc.snakechat

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandlerScope.implementation(dependencyNotation: Any) {
    add("implementation", dependencyNotation)
}

fun DependencyHandlerScope.api(dependencyNotation: Any) {
    add("api", dependencyNotation)
}

fun DependencyHandlerScope.ksp(dependencyNotation: Any) {
    add("ksp", dependencyNotation)
}

fun Project.isCommonProject(): Boolean = rootProject.name == "snakechat-common"