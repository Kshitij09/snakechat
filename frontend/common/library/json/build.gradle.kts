plugins {
    alias(libs.plugins.snakechat.jvm.library)
    alias(libs.plugins.snakechat.kotlininject)
}

dependencies {
    api(libs.kotlinx.serialization)
}