plugins {
    alias(libs.plugins.snakechat.multiplatform)
}

snakeKmp {
    targets {
        jvm()
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.result)
            api(projects.library.libraryKtorClient)
        }
    }
}
