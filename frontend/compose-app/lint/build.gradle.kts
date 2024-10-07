plugins {
    `java-library`
    alias(libs.plugins.snakechat.jvm.library)
    alias(libs.plugins.snakechat.android.lint)
}

dependencies {
    compileOnly(libs.kotlin.stdlib)
    compileOnly(libs.lint.api)
    testImplementation(libs.lint.checks)
    testImplementation(libs.lint.tests)
    testImplementation(kotlin("test"))
}

lint {
    xmlReport = true
    checkDependencies = true
}