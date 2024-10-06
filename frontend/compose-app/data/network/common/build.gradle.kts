plugins {
    alias(libs.plugins.snakechat.jvm.library)
}

dependencies {
    api(libs.result)
    api(libs.snakechat.library.ktorClient)
}