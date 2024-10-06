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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
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

rootProject.name = "SnakeChat"
include(":app")
include(":lint")

includeBuild("../common")
//include(":libraries:inject")
//include(":libraries:ktor-client")

include(":macrobenchmark")

include(":ui:design")
include(":ui:home")
include(":ui:strings")
include(":ui:likers")
include(":ui:common")
include(":ui:comments")
include(":ui:profile")

include(":domain:common")
include(":domain:common-model")
include(":domain:feed")
include(":domain:post")
include(":domain:profile")

include(":domain:model:likers")

include(":data:network:common")
include(":data:network:feed:api")
include(":data:network:feed:impl")
include(":data:network:post:api")
include(":data:network:post:impl")
include(":data:network:profile:api")
include(":data:network:profile:impl")


include(":data:network:fake")
include(":libraries:json")
include(":libraries:imageloading")
include(":libraries:test")
