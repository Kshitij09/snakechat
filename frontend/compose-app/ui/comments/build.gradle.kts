plugins {
    alias(libs.plugins.snakechat.android.ui)
}

android.namespace = "cc.snakechat.comments"

dependencies {
    implementation(projects.ui.common)
    implementation(projects.domain.post)
    implementation(projects.ui.strings)
    implementation(libs.androidx.paging.compose)
}