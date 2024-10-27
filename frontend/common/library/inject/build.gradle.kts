plugins {
    alias(libs.plugins.snakechat.multiplatform)
}

snakeKmp {
    targets {
        jvm()
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlininject.runtime)
        }
    }
}
