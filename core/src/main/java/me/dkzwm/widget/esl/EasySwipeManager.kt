/*
 * MIT License
 *
 * Copyright (c) 2018 dkzwm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.dkzwm.widget.esl

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import java.lang.ref.WeakReference

object EasySwipeManager {
    private var sInitialized = false
    @JvmStatic
    fun attach(activity: Activity): EasySwipeLayout? {
        val view = activity.window.decorView
        if (view is ViewGroup) {
            val easySwipeLayout = view.findViewById<View>(R.id.sl_swipe_gesture_layout)
            if (easySwipeLayout != null) return null
            val layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            val layout = EasySwipeLayout(activity)
            view.addView(layout, layoutParams)
            return layout
        }
        return null
    }

    @JvmStatic
    fun detach(activity: Activity) {
        val view = activity.window.decorView
        if (view is ViewGroup) {
            val layout = view.findViewById<EasySwipeLayout>(R.id.sl_swipe_gesture_layout)
            if (layout != null) {
                layout.setSwipeListener(null)
                view.removeView(layout)
            }
        }
    }

    @JvmStatic
    fun init(config: EasySwipeConfig) {
        if (sInitialized) throw IllegalArgumentException("Can only be initialized once")
        if (!config.mOnlyRunMainProcess || TextUtils.equals(
                        config.mApplication.packageName,
                        getProcessName(config.mApplication))) {
            config.mApplication.registerActivityLifecycleCallbacks(
                    EasyActivityLifecycleCallback(config))
            sInitialized = true
        }
    }

    @JvmStatic
    private fun getProcessName(context: Context): String? {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = am.runningAppProcesses ?: return null
        for (proInfo in runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName
                }
            }
        }
        return null
    }

    private class EasyActivityLifecycleCallback constructor(private val mConfig: EasySwipeConfig) :
            Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (activity is IgnoreMakeEasy) return
            val view = activity.window.decorView
            if (view is ViewGroup) {
                val easySwipeLayout = view.findViewById<View>(R.id.sl_swipe_gesture_layout)
                if (easySwipeLayout != null) return
                val layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
                val layout = EasySwipeLayout(activity)
                layout.id = R.id.sl_swipe_gesture_layout
                layout.setSwipeListener(SafetyListener(activity, mConfig.mSwipeListener))
                layout.setResistance(mConfig.mResistance)
                layout.setDirection(mConfig.mDirection)
                layout.setStyle(mConfig.mStyle, mConfig.mClassName)
                layout.setDurationOfClose(mConfig.mDurationOfClose)
                if (mConfig.mDrawer != null) layout.setDrawer(mConfig.mDrawer)
                layout.setEdgeDiff(mConfig.mEdgeDiff)
                view.addView(layout, layoutParams)
            }
        }

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityResumed(activity: Activity) {}

        override fun onActivityPaused(activity: Activity) {
            if (activity.isFinishing) {
                if (activity is IgnoreMakeEasy) return
                val view = activity.window.decorView
                if (view is ViewGroup) {
                    view.findViewById<EasySwipeLayout>(R.id.sl_swipe_gesture_layout)?.reset()
                }
            }
        }

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {
            if (activity is IgnoreMakeEasy) return
            val view = activity.window.decorView
            if (view is ViewGroup) {
                val layout = view.findViewById<EasySwipeLayout>(R.id.sl_swipe_gesture_layout)
                if (layout != null) {
                    layout.setSwipeListener(null)
                    view.removeView(layout)
                }
            }
        }
    }

    private class SafetyListener constructor(activity: Activity, val mSwipeListener: OnEasySwipeListener) :
            OnSwipeListener {
        private val mWeakActivity: WeakReference<Activity> = WeakReference(activity)
        override fun onSwipe(side: Int) {
            val activity = mWeakActivity.get()
            if (activity != null) {
                mSwipeListener.onSwipe(activity, side)
            }
        }
    }
}
