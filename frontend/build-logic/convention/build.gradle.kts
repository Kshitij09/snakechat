import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "snakechat.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "snakechat.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplicationFlavors") {
           id = "snakechat.android.application.flavors"
           implementationClass = "AndroidApplicationFlavorsConventionPlugin"
        }
        register("androidLint") {
            id = "snakechat.android.lint"
            implementationClass = "AndroidLintConventionPlugin"
        }
        register("jvmLibrary") {
            id = "snakechat.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("androidLibrary") {
            id = "snakechat.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "snakechat.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidTest") {
            id = "snakechat.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("androidUi") {
            id = "snakechat.android.ui"
            implementationClass = "AndroidUiConventionPlugin"
        }
        register("kotlinInject") {
            id = "snakechat.kotlininject"
            implementationClass = "KotlinInjectConventionPlugin"
        }
        register("lyricist") {
            id = "snakechat.lyricist"
            implementationClass = "LyricistConventionPlugin"
        }
        register("multiplatform") {
            id = "snakechat.multiplatform"
            implementationClass = "MultiplatformConventionPlugin"
        }
    }
}
