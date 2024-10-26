plugins {
    alias(libs.plugins.snakechat.multiplatform)
}

snakeKmp {
    jvm()
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlininject.runtime)
        }
    }
}
