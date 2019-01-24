package me.dkzwm.widget.esl.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.dkzwm.widget.esl.EasySwipeLayout
import me.dkzwm.widget.esl.IgnoreMakeEasy
import me.dkzwm.widget.esl.OnSwipeListener
import me.dkzwm.widget.esl.demo.R

class XmlCustomActivity : AppCompatActivity(), IgnoreMakeEasy {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xml_custom)
        val layout = findViewById<EasySwipeLayout>(R.id.easySwipeLayout)
        layout.setSwipeListener(
                object : OnSwipeListener {
                    override fun onSwipe(side: Int) {
                        onBackPressed()
                    }
                })
    }
}
