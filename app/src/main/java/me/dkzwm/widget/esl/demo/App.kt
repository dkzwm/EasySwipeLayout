package me.dkzwm.widget.esl.demo

import android.app.Application
import me.dkzwm.widget.esl.EasySwipeConfig
import me.dkzwm.widget.esl.EasySwipeManager
import me.dkzwm.widget.esl.config.Constants

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val config = EasySwipeConfig.Builder(this)
                .direction(Constants.DIRECTION_ALL)
                .style(Constants.STYLE_MIUI)
                .build()
        EasySwipeManager.init(config)
    }
}
