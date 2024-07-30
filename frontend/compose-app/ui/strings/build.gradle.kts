plugins {
    alias(libs.plugins.snakechat.android.library.compose)
    alias(libs.plugins.snakechat.lyricist)
}

android.namespace = "cc.snakechat.resources"

dependencies {
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui.text)
}

ksp {
    arg("lyricist.packageName", "cc.snakechat.resources")
    arg("lyricist.moduleName", "snake")
}