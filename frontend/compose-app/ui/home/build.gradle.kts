plugins {
    alias(libs.plugins.snakechat.android.ui)
}

android.namespace = "cc.snakechat.ui.home"

dependencies {
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.coil.compose)
    implementation(projects.ui.common)
    implementation(projects.ui.strings)
    implementation(projects.ui.likers)
    implementation(projects.ui.comments)
    implementation(projects.ui.profile)
    implementation(projects.domain.feed)
    implementation(projects.domain.commonModel)
    implementation(libs.kotlinx.serialization)
}