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
            from(files("../build-logic/gradle/libs.versions.toml"))
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

    ":data:network:common",
    ":data:network:fake",
    ":data:network:feed:api",
    ":data:network:feed:impl",
    ":data:network:post:api",
    ":data:network:post:impl",
    ":data:network:profile:api",
    ":data:network:profile:impl",


    ":domain:common",
    ":domain:common-model",
    ":domain:feed",
    ":domain:post",
    ":domain:profile",
)

include(*projects)

projects.forEach { path ->
    project(path).name = path.removePrefix(":").replace(":", "-")
}