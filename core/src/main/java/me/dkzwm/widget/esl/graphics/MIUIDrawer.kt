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
import android.graphics.*
import android.util.TypedValue
import androidx.annotation.ColorInt

open class MIUIDrawer(context: Context) : Drawer(context) {
    protected var mBackgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    protected var mArrowPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    protected var mBackgroundPath = Path()
    protected var mArrowPath = Path()
    protected var mBackgroundFixedSize: Float = 0f
    protected var mArrowStrokeWidth: Float = 0f
    override var maxSize: Int = 0
        protected set
    protected var mArrowHeight: Int = 0
    protected var mArrowWidth: Int = 0
    @ColorInt
    protected var mBackgroundColor = Color.BLACK
    @ColorInt
    protected var mArrowColor = Color.WHITE

    init {
        mBackgroundPaint.color = mBackgroundColor
        mBackgroundPaint.strokeWidth = 1f
        mBackgroundPaint.style = Paint.Style.FILL
        mBackgroundPaint.alpha = (0.74f * 255).toInt()
        mArrowStrokeWidth = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                2f,
                context.resources.displayMetrics).toInt().toFloat()
        mArrowHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                12f,
                context.resources.displayMetrics).toInt()
        mArrowWidth = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                5f,
                context.resources.displayMetrics).toInt()
        mArrowPaint.style = Paint.Style.STROKE
        mArrowPaint.color = mArrowColor
        mArrowPaint.strokeWidth = mArrowStrokeWidth
        mArrowPaint.strokeJoin = Paint.Join.ROUND
        mBackgroundFixedSize = context.resources.displayMetrics.heightPixels / 3.5f
        maxSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                26f,
                context.resources.displayMetrics).toInt()
    }

    fun setBackgroundMaxDynamicSize(size: Int) {
        maxSize = size
        requestInvalidate()
    }

    fun setBackgroundFixedSize(size: Int) {
        mBackgroundFixedSize = size.toFloat()
        requestInvalidate()
    }

    fun setBackgroundColor(@ColorInt color: Int) {
        if (mBackgroundColor != color) {
            mBackgroundColor = color
            mBackgroundPaint.color = mBackgroundColor
            requestInvalidate()
        }
    }

    fun setArrowColor(@ColorInt color: Int) {
        if (mArrowColor != color) {
            mArrowColor = color
            mArrowPaint.color = mArrowColor
            requestInvalidate()
        }
    }

    fun setArrowStrokeWidth(arrowStrokeWidth: Float) {
        if (mArrowStrokeWidth != arrowStrokeWidth) {
            mArrowStrokeWidth = arrowStrokeWidth
            mArrowPaint.strokeWidth = mArrowStrokeWidth
            requestInvalidate()
        }
    }

    fun setArrowHeight(arrowHeight: Int) {
        mArrowHeight = arrowHeight
        requestInvalidate()
    }

    fun setArrowWidth(arrowWidth: Int) {
        mArrowWidth = arrowWidth
        requestInvalidate()
    }

    override fun drawLeft(canvas: Canvas, downPoint: PointF, movedPoint: PointF) {
        mBackgroundPath.reset()
        val size = Math.min(movedPoint.x, maxSize.toFloat()).toInt()
        mBackgroundPath.moveTo(0f, 0f)
        mBackgroundPath.cubicTo(
                0f,
                mBackgroundFixedSize / 5,
                size.toFloat(),
                mBackgroundFixedSize / 3,
                size.toFloat(),
                mBackgroundFixedSize / 2)
        mBackgroundPath.cubicTo(
                size.toFloat(),
                mBackgroundFixedSize / 3 * 2,
                0f,
                mBackgroundFixedSize / 5 * 4,
                0f,
                mBackgroundFixedSize)
        mBackgroundPath.offset(0f, downPoint.y - mBackgroundFixedSize / 2f)
        canvas.drawPath(mBackgroundPath, mBackgroundPaint)
        if (size >= maxSize / 2.5f) {
            mArrowPath.reset()
            mArrowPath.moveTo(size / 2f + mArrowWidth / 2f, downPoint.y + mArrowHeight / 2f)
            mArrowPath.lineTo(size / 2f - mArrowWidth / 2f, downPoint.y)
            mArrowPath.lineTo(size / 2f + mArrowWidth / 2f, downPoint.y - mArrowHeight / 2f)
            canvas.drawPath(mArrowPath, mArrowPaint)
        }
    }

    override fun drawTop(canvas: Canvas, downPoint: PointF, movedPoint: PointF) {
        mBackgroundPath.reset()
        val size = Math.min(movedPoint.y, maxSize.toFloat()).toInt()
        mBackgroundPath.moveTo(0f, 0f)
        mBackgroundPath.cubicTo(
                mBackgroundFixedSize / 5, 0f,
                mBackgroundFixedSize / 3, size.toFloat(),
                mBackgroundFixedSize / 2, size.toFloat())
        mBackgroundPath.cubicTo(
                mBackgroundFixedSize / 3 * 2,
                size.toFloat(),
                mBackgroundFixedSize / 5 * 4,
                0f,
                mBackgroundFixedSize,
                0f)
        mBackgroundPath.offset(downPoint.x - mBackgroundFixedSize / 2f, 0f)
        canvas.drawPath(mBackgroundPath, mBackgroundPaint)
        if (size >= maxSize / 2.5f) {
            mArrowPath.reset()
            mArrowPath.moveTo(downPoint.x + mArrowHeight / 2f, size / 2f + mArrowWidth / 2f)
            mArrowPath.lineTo(downPoint.x, size / 2f - mArrowWidth / 2f)
            mArrowPath.lineTo(downPoint.x - mArrowHeight / 2f, size / 2f + mArrowWidth / 2f)
            canvas.drawPath(mArrowPath, mArrowPaint)
        }
    }

    override fun drawRight(canvas: Canvas, downPoint: PointF, movedPoint: PointF) {
        mBackgroundPath.reset()
        val size = Math.min(mWidth - movedPoint.x, maxSize.toFloat()).toInt()
        mBackgroundPath.moveTo(0f, 0f)
        mBackgroundPath.cubicTo(
                0f,
                mBackgroundFixedSize / 5,
                (-size).toFloat(),
                mBackgroundFixedSize / 3,
                (-size).toFloat(),
                mBackgroundFixedSize / 2)
        mBackgroundPath.cubicTo(
                (-size).toFloat(),
                mBackgroundFixedSize / 3 * 2,
                0f,
                mBackgroundFixedSize / 5 * 4,
                0f,
                mBackgroundFixedSize)
        mBackgroundPath.offset(mWidth.toFloat(), downPoint.y - mBackgroundFixedSize / 2f)
        canvas.drawPath(mBackgroundPath, mBackgroundPaint)
        if (size >= maxSize / 2.5f) {
            val x = mWidth - size
            mArrowPath.reset()
            mArrowPath.moveTo(x + size / 2f - mArrowWidth / 2f, downPoint.y + mArrowHeight / 2f)
            mArrowPath.lineTo(x.toFloat() + size / 2f + mArrowWidth / 2f, downPoint.y)
            mArrowPath.lineTo(x + size / 2f - mArrowWidth / 2f, downPoint.y - mArrowHeight / 2f)
            canvas.drawPath(mArrowPath, mArrowPaint)
        }
    }

    override fun drawBottom(canvas: Canvas, downPoint: PointF, movedPoint: PointF) {
        mBackgroundPath.reset()
        val size = Math.min(mHeight - movedPoint.y, maxSize.toFloat()).toInt()
        mBackgroundPath.moveTo(0f, 0f)
        mBackgroundPath.cubicTo(
                mBackgroundFixedSize / 5, 0f,
                mBackgroundFixedSize / 3, (-size).toFloat(),
                mBackgroundFixedSize / 2, (-size).toFloat())
        mBackgroundPath.cubicTo(
                mBackgroundFixedSize / 3 * 2,
                (-size).toFloat(),
                mBackgroundFixedSize / 5 * 4,
                0f,
                mBackgroundFixedSize,
                0f)
        mBackgroundPath.offset(downPoint.x - mBackgroundFixedSize / 2f, mHeight.toFloat())
        canvas.drawPath(mBackgroundPath, mBackgroundPaint)
        if (size >= maxSize / 2.5f) {
            val y = mHeight - size
            mArrowPath.reset()
            mArrowPath.moveTo(downPoint.x + mArrowHeight / 2f, y + size / 2f - mArrowWidth / 2f)
            mArrowPath.lineTo(downPoint.x, y.toFloat() + size / 2f + mArrowWidth / 2f)
            mArrowPath.lineTo(downPoint.x - mArrowHeight / 2f, y + size / 2f - mArrowWidth / 2f)
            canvas.drawPath(mArrowPath, mArrowPaint)
        }
    }

    override fun canTrigger(edge: Int, pos: Float): Boolean {
        return pos >= maxSize / 3 * 2
    }
}
