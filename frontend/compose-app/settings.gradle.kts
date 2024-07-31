enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
includeBuild("build-logic")

pluginManagement {
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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SnakeChat"
include(":app")
include(":lint")

include(":libraries:inject")
include(":libraries:ktor-client")

include(":macrobenchmark")

include(":ui:design")
include(":ui:home")
include(":ui:strings")

include(":domain:common")
include(":domain:feed")
include(":domain:post")

include(":data:network:feed:api")
include(":data:network:feed:impl")
include(":data:network:post:api")
include(":data:network:post:impl")


include(":data:network:fake")
include(":libraries:json")
include(":libraries:imageloading")
include(":libraries:test")
include(":ui:likes")
