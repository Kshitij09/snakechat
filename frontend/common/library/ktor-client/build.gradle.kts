plugins {
    alias(libs.plugins.snakechat.multiplatform)
    alias(libs.plugins.snakechat.kotlininject)
}

snakeKmp {
    targets {
        jvm()
        android("cc.snakechat.ktorclient")
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.ktor.client.core)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.serialization.kotlinxJson)
            implementation(libs.ktor.client.logging)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
    }
}