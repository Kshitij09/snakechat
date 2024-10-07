plugins {
    alias(libs.plugins.snakechat.jvm.library)
    alias(libs.plugins.snakechat.kotlininject)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.data.network.feed.dataNetworkFeedApi)
    implementation(libs.androidx.paging.common)
    api(projects.domain.domainCommon)
}
