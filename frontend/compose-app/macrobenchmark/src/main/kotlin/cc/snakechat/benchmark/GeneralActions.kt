package cc.snakechat.benchmark

import android.Manifest.permission
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.benchmark.macro.MacrobenchmarkScope

fun MacrobenchmarkScope.allowNotifications() {
    if (SDK_INT >= TIRAMISU) {
        val command = "pm grant $packageName ${permission.POST_NOTIFICATIONS}"
        device.executeShellCommand(command)
    }
}
