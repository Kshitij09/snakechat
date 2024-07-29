plugins {
    alias(libs.plugins.snakechat.android.ui)
    alias(libs.plugins.kotlin.parcelize)
}

android.namespace = "cc.snakechat.ui.home"

dependencies {
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.activity.compose)
    implementation(libs.coil.compose)
    implementation(projects.ui.strings)
    implementation(projects.domain.feed)
    implementation(libs.kotlinx.serialization)
}