enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    includeBuild("../build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "snakechat-common"

val projects = arrayOf(
    ":library:inject",
    ":library:ktor-client",
    ":library:json",
    ":library:imageloading",
    ":library:test",

    ":data:network:feed:api",
    ":data:network:feed:impl",
)

include(*projects)

projects.forEach { path ->
    project(path).name = path.removePrefix(":").replace(":", "-")
}