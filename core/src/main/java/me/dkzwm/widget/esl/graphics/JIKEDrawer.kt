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

class JIKEDrawer(context: Context) : MIUIDrawer(context) {

    private fun calcBackgroundAlpha(pos: Float): Int {
        return Math.min(Math.max((pos / maxSize * 255).toInt(), 25), 200)
    }

    private fun calcArrowAlpha(pos: Float): Int {
        return Math.min(Math.max((pos / maxSize * 255).toInt(), 25), 255)
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
        mBackgroundPath.offset(0f, movedPoint.y - mBackgroundFixedSize / 2f)
        val alpha = calcBackgroundAlpha(size.toFloat())
        mBackgroundPaint.alpha = alpha
        canvas.drawPath(mBackgroundPath, mBackgroundPaint)
        if (size >= maxSize / 2.5f) {
            val arrowAlpha = calcArrowAlpha(size.toFloat())
            val arrowScale = size.toFloat() / maxSize
            val arrowAngle = if (arrowScale < 0.8f) 0f else (arrowScale - 0.8f) * 2
            mArrowPath.reset()
            mArrowPath.moveTo(
                    size / 2f + mArrowWidth * arrowAngle,
                    movedPoint.y - arrowScale * mArrowHeight / 2)
            mArrowPath.lineTo(size / 2f - mArrowWidth * arrowAngle, movedPoint.y)
            mArrowPath.lineTo(
                    size / 2f + mArrowWidth * arrowAngle,
                    movedPoint.y + arrowScale * mArrowHeight / 2)
            mArrowPaint.alpha = arrowAlpha
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
        mBackgroundPath.offset(movedPoint.x - mBackgroundFixedSize / 2f, 0f)
        val alpha = calcBackgroundAlpha(size.toFloat())
        mBackgroundPaint.alpha = alpha
        canvas.drawPath(mBackgroundPath, mBackgroundPaint)
        if (size >= maxSize / 2.5f) {
            val arrowAlpha = calcArrowAlpha(size.toFloat())
            val arrowScale = size.toFloat() / maxSize
            val arrowAngle = if (arrowScale < 0.8f) 0f else (arrowScale - 0.8f) * 2
            mArrowPath.reset()
            mArrowPath.moveTo(
                    movedPoint.x - arrowScale * mArrowHeight / 2,
                    size / 2f + mArrowWidth * arrowAngle)
            mArrowPath.lineTo(movedPoint.x, size / 2f - mArrowWidth * arrowAngle)
            mArrowPath.lineTo(
                    movedPoint.x + arrowScale * mArrowHeight / 2,
                    size / 2f + mArrowWidth * arrowAngle)
            mArrowPaint.alpha = arrowAlpha
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
        mBackgroundPath.offset(mWidth.toFloat(), movedPoint.y - mBackgroundFixedSize / 2f)
        val alpha = calcBackgroundAlpha(size.toFloat())
        mBackgroundPaint.alpha = alpha
        canvas.drawPath(mBackgroundPath, mBackgroundPaint)
        if (size >= maxSize / 2.5f) {
            val arrowAlpha = calcArrowAlpha(size.toFloat())
            val x = mWidth - size
            val arrowScale = size.toFloat() / maxSize
            val arrowAngle = if (arrowScale < 0.8f) 0f else (arrowScale - 0.8f) * 2
            mArrowPath.reset()
            mArrowPath.moveTo(
                    x + size / 2f - mArrowWidth * arrowAngle,
                    movedPoint.y - arrowScale * mArrowHeight / 2)
            mArrowPath.lineTo(x.toFloat() + size / 2f + mArrowWidth * arrowAngle, movedPoint.y)
            mArrowPath.lineTo(
                    x + size / 2f - mArrowWidth * arrowAngle,
                    movedPoint.y + arrowScale * mArrowHeight / 2)
            mArrowPaint.alpha = arrowAlpha
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
        mBackgroundPath.lineTo(0f, 0f)
        mBackgroundPath.offset(movedPoint.x - mBackgroundFixedSize / 2f, mHeight.toFloat())
        val alpha = calcBackgroundAlpha(size.toFloat())
        mBackgroundPaint.alpha = alpha
        canvas.drawPath(mBackgroundPath, mBackgroundPaint)
        if (size >= maxSize / 2.5f) {
            val arrowAlpha = calcArrowAlpha(size.toFloat())
            val y = mHeight - size
            val arrowScale = size.toFloat() / maxSize
            val arrowAngle = if (arrowScale < 0.8f) 0f else (arrowScale - 0.8f) * 2
            mArrowPath.reset()
            mArrowPath.moveTo(
                    movedPoint.x - arrowScale * mArrowHeight / 2,
                    y + size / 2f - mArrowWidth * arrowAngle)
            mArrowPath.lineTo(movedPoint.x, y.toFloat() + size / 2f + mArrowWidth * arrowAngle)
            mArrowPath.lineTo(
                    movedPoint.x + arrowScale * mArrowHeight / 2,
                    y + size / 2f - mArrowWidth * arrowAngle)
            mArrowPaint.alpha = arrowAlpha
            canvas.drawPath(mArrowPath, mArrowPaint)
        }
    }
}
