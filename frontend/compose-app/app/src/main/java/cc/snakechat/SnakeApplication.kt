package cc.snakechat

import android.app.Application
import cc.snakechat.inject.AndroidApplicationComponent
import cc.snakechat.inject.create

class SnakeApplication : Application() {
    val component: AndroidApplicationComponent by lazy(LazyThreadSafetyMode.NONE) {
        AndroidApplicationComponent::class.create(this)
    }
}
