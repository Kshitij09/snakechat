plugins {
    alias(libs.plugins.snakechat.multiplatform)
    alias(libs.plugins.kotlin.serialization)
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
        }
    }
}