plugins {
    alias(libs.plugins.snakechat.android.ui)
}

android.namespace = "cc.snakechat.profile"

dependencies {
    implementation(projects.ui.common)
    implementation(libs.snakechat.domain.profile)
    implementation(projects.ui.strings)
}