package cc.snakechat

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class SnakeChatBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}
