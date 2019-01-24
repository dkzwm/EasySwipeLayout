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
import android.app.Application
import androidx.annotation.IntRange
import me.dkzwm.widget.esl.annotation.Direction
import me.dkzwm.widget.esl.annotation.Style
import me.dkzwm.widget.esl.config.Constants
import me.dkzwm.widget.esl.graphics.Drawer

class EasySwipeConfig private constructor(application: Application) {
    @JvmField
    internal val mApplication = application
    @JvmField
    internal var mSwipeListener = sSwipeListener
    @JvmField
    internal var mOnlyRunMainProcess: Boolean = false
    @Style
    @JvmField
    internal var mStyle = Constants.STYLE_MIUI
    @JvmField
    internal var mClassName: String? = null
    @JvmField
    internal var mDrawer: Drawer? = null
    @Direction
    @JvmField
    internal var mDirection = Constants.DIRECTION_HORIZONTAL
    @JvmField
    internal var mResistance = Constants.DEFAULT_RESISTANCE
    @JvmField
    internal var mDurationOfClose = Constants.DEFAULT_DURATION_OF_CLOSE
    @JvmField
    internal var mEdgeDiff = -1

    class Builder constructor(application: Application) {
        private val mConfig = EasySwipeConfig(application)

        fun swipeListener(listener: OnEasySwipeListener?): Builder {
            mConfig.mSwipeListener = listener ?: sSwipeListener
            return this
        }

        fun runOnMainProcess(run: Boolean): Builder {
            mConfig.mOnlyRunMainProcess = run
            return this
        }


        fun durationOfClose(@IntRange(from = 0) duration: Int): Builder {
            mConfig.mDurationOfClose = duration
            return this
        }

        @JvmOverloads
        fun style(@Style style: Int, className: String? = null): Builder {
            mConfig.mStyle = style
            mConfig.mClassName = className
            mConfig.mDrawer = null
            return this
        }

        fun edgeDiff(@IntRange(from = 0) edgeDiff: Int): Builder {
            mConfig.mEdgeDiff = edgeDiff
            return this
        }

        fun resistance(resistance: Float): Builder {
            mConfig.mResistance = resistance
            return this
        }

        fun direction(@Direction direction: Int): Builder {
            mConfig.mDirection = direction
            return this
        }

        fun drawer(drawer: Drawer): Builder {
            mConfig.mStyle = Constants.STYLE_CUSTOM
            mConfig.mClassName = null
            mConfig.mDrawer = drawer
            return this
        }

        fun build(): EasySwipeConfig {
            return mConfig
        }
    }

    companion object {
        private val sSwipeListener = object : OnEasySwipeListener {
            override fun onSwipe(activity: Activity, side: Int) {
                activity.onBackPressed()
            }
        }
    }
}
