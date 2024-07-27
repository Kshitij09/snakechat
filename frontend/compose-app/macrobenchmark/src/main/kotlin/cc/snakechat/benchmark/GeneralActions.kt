package cc.snakechat.benchmark

import android.Manifest.permission
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until

fun MacrobenchmarkScope.allowNotifications() {
    if (SDK_INT >= TIRAMISU) {
        val command = "pm grant $packageName ${permission.POST_NOTIFICATIONS}"
        device.executeShellCommand(command)
    }
}

fun MacrobenchmarkScope.waitUntilHelloWorld() {
    device.wait(Until.hasObject(By.text("Hello Android!")), 5000)
}
