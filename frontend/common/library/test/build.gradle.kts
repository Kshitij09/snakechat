plugins {
    alias(libs.plugins.snakechat.android.library)
    alias(libs.plugins.snakechat.kotlininject)
}
android.namespace = "cc.snakechat"

dependencies {
    api(libs.coil.test)
}
