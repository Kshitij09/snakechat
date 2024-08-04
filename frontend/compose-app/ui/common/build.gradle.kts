plugins {
    alias(libs.plugins.snakechat.android.ui)
}

android.namespace = "cc.snakechat.ui.common"

dependencies {
    api(libs.androidx.paging.compose)
    api(libs.result)
}