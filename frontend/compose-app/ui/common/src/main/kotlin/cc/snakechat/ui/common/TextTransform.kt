package cc.snakechat.ui.common

import java.time.LocalDateTime

fun formatDate(dateTime: LocalDateTime) = "${dateTime.dayOfMonth} ${dateTime.month} ${dateTime.year}"

fun countString(count: Long): String = if (count < 999) {
    count.toString()
} else {
    "${(count / 1000.0).round(2)}k"
}

private fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}
