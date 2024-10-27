plugins {
    alias(libs.plugins.snakechat.multiplatform)
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
            api(libs.kotlinx.serialization)
        }
    }
}