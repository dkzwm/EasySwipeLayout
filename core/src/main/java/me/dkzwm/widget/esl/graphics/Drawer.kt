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
package me.dkzwm.widget.esl.graphics

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import androidx.annotation.CallSuper
import me.dkzwm.widget.esl.EasySwipeLayout

abstract class Drawer(context: Context) {
    @JvmField
    protected var mSwipeLayout: EasySwipeLayout? = null
    @JvmField
    protected var mAttached = false
    @JvmField
    protected var mWidth = 0
    @JvmField
    protected var mHeight = 0
    @JvmField
    protected var mContext: Context? = context
    abstract val maxSize: Int

    fun onSizeChanged(width: Int, height: Int) {
        mWidth = width
        mHeight = height
    }

    fun onAttached(layout: EasySwipeLayout) {
        mAttached = true
        mSwipeLayout = layout
    }

    abstract fun drawLeft(canvas: Canvas, downPoint: PointF, movedPoint: PointF)

    abstract fun drawTop(canvas: Canvas, downPoint: PointF, movedPoint: PointF)

    abstract fun drawRight(canvas: Canvas, downPoint: PointF, movedPoint: PointF)

    abstract fun drawBottom(canvas: Canvas, downPoint: PointF, movedPoint: PointF)

    abstract fun canTrigger(edge: Int, pos: Float): Boolean

    protected fun requestInvalidate() {
        mSwipeLayout?.postInvalidate()
    }

    @CallSuper
    fun onDetached() {
        mAttached = false
        mSwipeLayout = null
        mContext = null
    }
}
