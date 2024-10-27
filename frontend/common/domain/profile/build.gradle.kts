plugins {
    alias(libs.plugins.snakechat.multiplatform)
    alias(libs.plugins.snakechat.kotlininject)
}

snakeKmp {
    targets {
        jvm()
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(projects.data.network.profile.dataNetworkProfileApi)
            implementation(libs.androidx.paging.common)
            api(projects.domain.domainCommon)
            api(projects.domain.domainCommonModel)
        }
    }
}
