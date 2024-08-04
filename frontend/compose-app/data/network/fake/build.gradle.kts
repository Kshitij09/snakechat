plugins {
    alias(libs.plugins.snakechat.android.library)
    alias(libs.plugins.snakechat.kotlininject)
}

android.namespace = "cc.snakechat.data"

dependencies {
    api(projects.data.network.feed.api)
    api(projects.data.network.post.api)
    api(projects.data.network.profile.api)
    api(libs.result)
    implementation(libs.kotlinx.serialization)
}

kotlin {
    compilerOptions {
        optIn.add("kotlinx.serialization.ExperimentalSerializationApi")
    }
}