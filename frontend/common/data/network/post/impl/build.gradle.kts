plugins {
    alias(libs.plugins.snakechat.jvm.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.snakechat.kotlininject)
}

dependencies {
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.library.libraryKtorClient)
    api(projects.data.network.post.dataNetworkPostApi)
}