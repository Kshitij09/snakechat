plugins {
    alias(libs.plugins.snakechat.android.ui)
}

android.namespace = "cc.snakechat.likers"

dependencies {
    implementation(projects.domain.post)
}