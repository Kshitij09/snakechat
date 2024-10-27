plugins {
    alias(libs.plugins.snakechat.multiplatform)
    alias(libs.plugins.snakechat.kotlininject)
}

snakeKmp {
    targets {
        jvm()
        android("cc.snakechat.testutil")
    }
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            api(libs.coil.test)
        }
    }
}
