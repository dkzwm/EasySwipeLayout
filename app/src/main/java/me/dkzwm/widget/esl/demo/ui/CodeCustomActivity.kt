package me.dkzwm.widget.esl.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.dkzwm.widget.esl.EasySwipeLayout
import me.dkzwm.widget.esl.EasySwipeManager
import me.dkzwm.widget.esl.IgnoreMakeEasy
import me.dkzwm.widget.esl.OnSwipeListener
import me.dkzwm.widget.esl.config.Constants
import me.dkzwm.widget.esl.demo.R
import me.dkzwm.widget.esl.demo.graphics.CustomDrawer

class CodeCustomActivity : AppCompatActivity(), IgnoreMakeEasy {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_custom)
        val layout = EasySwipeManager.attach(this)
        if (layout != null) {
            layout.setDirection(Constants.DIRECTION_LEFT)
            layout.setDrawer(CustomDrawer(this))
            layout.setSwipeListener(
                    object : OnSwipeListener {
                        override fun onSwipe(side: Int) {
                            onBackPressed()
                        }
                    })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EasySwipeManager.detach(this)
    }
}
