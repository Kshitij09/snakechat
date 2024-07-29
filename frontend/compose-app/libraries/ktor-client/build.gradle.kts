plugins {
    alias(libs.plugins.snakechat.jvm.library)
    alias(libs.plugins.snakechat.kotlininject)
}

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.serialization.kotlinxJson)
}