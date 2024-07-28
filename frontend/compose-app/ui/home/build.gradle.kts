plugins {
    alias(libs.plugins.snakechat.android.ui)
    alias(libs.plugins.kotlin.parcelize)
}

android.namespace = "cc.snakechat.ui.home"

dependencies {
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.circuit.foundation)
    implementation(libs.androidx.activity.compose)
}