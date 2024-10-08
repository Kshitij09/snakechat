import java.util.Properties

plugins {
    alias(libs.plugins.snakechat.android.application)
    alias(libs.plugins.snakechat.android.application.compose)
    alias(libs.plugins.snakechat.android.application.flavors)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.bytemask)
    alias(libs.plugins.module.graph)
}

android {
    namespace = "cc.snakechat"

    defaultConfig {
        applicationId = "cc.snakechat"
        versionCode = 1
        versionName = "0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    val props = rootDir.resolve("signing/debug_keystore.properties")
        .inputStream()
        .use { inputStream -> Properties().also { it.load(inputStream) } }
    val keystore = rootDir.resolve("signing/debug.keystore")

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
            signingConfig = signingConfigs.getByName("debug")
            // Ensure Baseline Profile is fresh for release builds.
            baselineProfile.automaticGenerationDuringBuild = true
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
    implementation(libs.circuit.foundation)
    implementation(libs.coil.core)
    implementation(libs.coil.network)
    demoImplementation(libs.coil.test)
    implementation(projects.ui.common)
    implementation(projects.ui.design)
    implementation(projects.ui.home)
    implementation(projects.ui.likers)
    implementation(projects.ui.comments)
    implementation(projects.ui.profile)

    implementation(libs.snakechat.library.ktorClient)
    implementation(libs.snakechat.library.json)
    prodImplementation(libs.snakechat.library.imageLoading)
    demoImplementation(libs.snakechat.library.test)

    implementation(libs.snakechat.domain.feed)
    implementation(libs.snakechat.domain.post)
    implementation(libs.snakechat.domain.profile)

    debugImplementation(libs.slf4j.android)
    demoImplementation(libs.snakechat.data.network.fake)
    prodImplementation(libs.snakechat.data.network.feed.impl)
    prodImplementation(libs.snakechat.data.network.post.impl)
    prodImplementation(libs.snakechat.data.network.profile.impl)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    baselineProfile(projects.macrobenchmark)
}

dependencyGuard {
    configuration("prodReleaseRuntimeClasspath")
}

baselineProfile {
    // Don't build on every iteration of a full assemble.
    // Instead enable generation directly for the release build variant.
    automaticGenerationDuringBuild = false
}

bytemaskConfig {
    configure("release") {
        enableEncryption = true
    }
}

moduleGraphAssert {
    configurations = setOf("implementation", "prodImplementation")
}