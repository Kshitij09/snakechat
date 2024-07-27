import cc.snakechat.configureFlavors

plugins {
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.snakechat.android.test)
}

android {
    namespace = "cc.snakechat.benchmark"

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
        buildConfigField("String", "APP_BUILD_TYPE_SUFFIX", "\"\"")
    }

    buildFeatures {
        buildConfig = true
    }

    // Use the same flavor dimensions as the application to allow generating Baseline Profiles on prod,
    // which is more close to what will be shipped to users (no fake data), but has ability to run the
    // benchmarks on demo, so we benchmark on stable data.
    configureFlavors(this) { flavor ->
        buildConfigField(
            "String",
            "APP_FLAVOR_SUFFIX",
            "\"${flavor.applicationIdSuffix ?: ""}\""
        )
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    implementation(libs.androidx.junit)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}
