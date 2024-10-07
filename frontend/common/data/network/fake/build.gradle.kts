plugins {
    alias(libs.plugins.snakechat.android.library)
    alias(libs.plugins.snakechat.kotlininject)
}

android.namespace = "cc.snakechat.data"

dependencies {
    api(projects.data.network.feed.dataNetworkFeedApi)
    api(projects.data.network.post.dataNetworkPostApi)
    api(projects.data.network.profile.dataNetworkProfileApi)
    api(libs.result)
    implementation(libs.kotlinx.serialization)
}

kotlin {
    compilerOptions {
        optIn.add("kotlinx.serialization.ExperimentalSerializationApi")
    }
}