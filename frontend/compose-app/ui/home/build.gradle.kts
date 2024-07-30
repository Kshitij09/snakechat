plugins {
    alias(libs.plugins.snakechat.android.ui)
}

android.namespace = "cc.snakechat.ui.home"

dependencies {
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.coil.compose)
    implementation(projects.ui.strings)
    implementation(projects.domain.feed)
    implementation(libs.kotlinx.serialization)
}