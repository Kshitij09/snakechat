plugins {
    alias(libs.plugins.snakechat.jvm.library)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization)
    api(projects.data.network.dataNetworkCommon)
    api(libs.result)
}