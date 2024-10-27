plugins {
    alias(libs.plugins.snakechat.multiplatform)
}

snakeKmp {
    targets {
        jvm()
    }
}
