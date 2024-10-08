plugins {
    alias(libs.plugins.snakechat.android.library)
    alias(libs.plugins.snakechat.android.library.compose)
}

android.namespace = "cc.snakechat.design"

android {
    lint {
       disable.add("DesignSystem")
    }
}

dependencies {
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.material3)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.runtime)
    api(libs.coil.compose)
}