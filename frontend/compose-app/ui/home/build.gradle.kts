plugins {
    alias(libs.plugins.snakechat.android.ui)
}

android.namespace = "cc.snakechat.ui.home"

dependencies {
    implementation(libs.androidx.compose.material.iconsExtended)
}