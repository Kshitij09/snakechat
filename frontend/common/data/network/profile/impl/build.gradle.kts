plugins {
    alias(libs.plugins.snakechat.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.snakechat.kotlininject)
}

snakeKmp {
    targets {
        jvm()
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization)
            implementation(libs.kotlinx.coroutines.core)
            implementation(projects.library.libraryKtorClient)
            api(projects.data.network.profile.dataNetworkProfileApi)
        }
    }
}