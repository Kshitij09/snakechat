package cc.snakechat.benchmark.baseline

import androidx.benchmark.macro.junit4.BaselineProfileRule
import cc.snakechat.benchmark.PACKAGE_NAME
import cc.snakechat.benchmark.allowNotifications
import cc.snakechat.benchmark.waitUntilHelloWorld
import org.junit.Rule
import org.junit.Test

class StartupBaselineProfile {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        PACKAGE_NAME,
        includeInStartupProfile = true,
        profileBlock = {
            startActivityAndWait()
            allowNotifications()
            waitUntilHelloWorld()
        },
    )
}
