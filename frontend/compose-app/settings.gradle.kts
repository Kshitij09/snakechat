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

rootProject.name = "SnakeChat"
include(":app")
include(":lint")

includeBuild("../common")

include(":macrobenchmark")

include(":ui:design")
include(":ui:home")
include(":ui:strings")
include(":ui:likers")
include(":ui:common")
include(":ui:comments")
include(":ui:profile")

