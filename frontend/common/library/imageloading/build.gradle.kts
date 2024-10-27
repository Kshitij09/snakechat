plugins {
    alias(libs.plugins.snakechat.multiplatform)
    alias(libs.plugins.snakechat.kotlininject)
}

snakeKmp {
    targets {
        jvm()
        android("cc.snakechat.imageloading")
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.coil.network)
        }
    }
}
