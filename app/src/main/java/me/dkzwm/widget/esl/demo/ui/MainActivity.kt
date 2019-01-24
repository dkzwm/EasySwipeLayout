package me.dkzwm.widget.esl.demo.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import me.dkzwm.widget.esl.demo.R

class MainActivity : AppCompatActivity() {
    private var mCount = 0
    private val mHandler = Handler()
    private val mDelayedCountdown = Runnable { mCount = 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.button_go_xml_custom)
                .setOnClickListener {
                    startActivity(
                            Intent(this@MainActivity, XmlCustomActivity::class.java))
                }
        findViewById<View>(R.id.button_go_code_custom)
                .setOnClickListener {
                    startActivity(
                            Intent(this@MainActivity, CodeCustomActivity::class.java))
                }
    }

    override fun onBackPressed() {
        mCount++
        mHandler.removeCallbacks(mDelayedCountdown)
        if (mCount < 2) {
            Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_SHORT).show()
            mHandler.postDelayed(mDelayedCountdown, 800)
            return
        }
        super.onBackPressed()
    }
}
