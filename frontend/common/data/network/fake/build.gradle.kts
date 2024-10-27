plugins {
    alias(libs.plugins.snakechat.multiplatform)
    alias(libs.plugins.snakechat.kotlininject)
}

snakeKmp {
    targets {
        jvm()
        android("cc.snakechat.data")
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.data.network.feed.dataNetworkFeedApi)
            api(projects.data.network.post.dataNetworkPostApi)
            api(projects.data.network.profile.dataNetworkProfileApi)
            api(libs.result)
            implementation(libs.kotlinx.serialization)
        }
    }
}
