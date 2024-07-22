import java.util.Properties

plugins {
    alias(libs.plugins.snakechat.android.application)
    alias(libs.plugins.snakechat.android.application.compose)
    alias(libs.plugins.snakechat.android.application.flavors)
}

android {
    namespace = "cc.snakechat"

    defaultConfig {
        applicationId = "cc.snakechat"
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    val props = rootDir.resolve("debug_keystore.properties")
        .inputStream()
        .use { inputStream -> Properties().also { it.load(inputStream) } }
    val keystore = rootDir.resolve("debug.keystore")

    signingConfigs {
        named("debug") {
            storeFile = keystore
            keyAlias = props["alias"] as String
            storePassword = props["password"] as String
            keyPassword = props["password"] as String
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.named("debug").get()
        }
        release {
            isShrinkResources = true
            isCrunchPngs = true
            isMinifyEnabled = true
            signingConfig = signingConfigs.named("debug").get()
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material3)
    implementation(projects.ui.design)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

dependencyGuard {
    configuration("prodReleaseRuntimeClasspath")
}