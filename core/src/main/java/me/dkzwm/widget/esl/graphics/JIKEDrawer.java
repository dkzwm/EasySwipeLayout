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
package me.dkzwm.widget.esl.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;

public class JIKEDrawer extends MIUIDrawer {

    public JIKEDrawer(Context context) {
        super(context);
    }

    private int calcBackgroundAlpha(float pos) {
        return Math.min(Math.max((int) (pos / mBackgroundMaxDynamicSize * 255), 25), 200);
    }

    private int calcArrowAlpha(float pos) {
        return Math.min(Math.max((int) (pos / mBackgroundMaxDynamicSize * 255), 25), 255);
    }

    @Override
    public void drawLeft(Canvas canvas, PointF downPoint, PointF movedPoint) {
        mBackgroundPath.reset();
        int size = (int) Math.min(movedPoint.x, mBackgroundMaxDynamicSize);
        mBackgroundPath.moveTo(0, 0);
        mBackgroundPath.cubicTo(
                0,
                mBackgroundFixedSize / 5,
                size,
                mBackgroundFixedSize / 3,
                size,
                mBackgroundFixedSize / 2);
        mBackgroundPath.cubicTo(
                size,
                mBackgroundFixedSize / 3 * 2,
                0,
                mBackgroundFixedSize / 5 * 4,
                0,
                mBackgroundFixedSize);
        mBackgroundPath.offset(0, movedPoint.y - mBackgroundFixedSize / 2f);
        final int alpha = calcBackgroundAlpha(size);
        mBackgroundPaint.setAlpha(alpha);
        canvas.drawPath(mBackgroundPath, mBackgroundPaint);
        if (size >= mBackgroundMaxDynamicSize / 2.5f) {
            final int arrowAlpha = calcArrowAlpha(size);
            float arrowScale = (float) size / mBackgroundMaxDynamicSize;
            float arrowAngle = arrowScale < 0.8f ? 0 : (arrowScale - 0.8f) * 2;
            mArrowPath.reset();
            mArrowPath.moveTo(
                    size / 2f + (mArrowWidth * arrowAngle),
                    movedPoint.y - (arrowScale * mArrowHeight / 2));
            mArrowPath.lineTo(size / 2f - (mArrowWidth * arrowAngle), movedPoint.y);
            mArrowPath.lineTo(
                    size / 2f + (mArrowWidth * arrowAngle),
                    movedPoint.y + (arrowScale * mArrowHeight / 2));
            mArrowPaint.setAlpha(arrowAlpha);
            canvas.drawPath(mArrowPath, mArrowPaint);
        }
    }

    @Override
    public void drawTop(Canvas canvas, PointF downPoint, PointF movedPoint) {
        mBackgroundPath.reset();
        int size = (int) Math.min(movedPoint.y, mBackgroundMaxDynamicSize);
        mBackgroundPath.moveTo(0, 0);
        mBackgroundPath.cubicTo(
                mBackgroundFixedSize / 5, 0,
                mBackgroundFixedSize / 3, size,
                mBackgroundFixedSize / 2, size);
        mBackgroundPath.cubicTo(
                mBackgroundFixedSize / 3 * 2,
                size,
                mBackgroundFixedSize / 5 * 4,
                0,
                mBackgroundFixedSize,
                0);
        mBackgroundPath.offset(movedPoint.x - mBackgroundFixedSize / 2f, 0);
        final int alpha = calcBackgroundAlpha(size);
        mBackgroundPaint.setAlpha(alpha);
        canvas.drawPath(mBackgroundPath, mBackgroundPaint);
        if (size >= mBackgroundMaxDynamicSize / 2.5f) {
            final int arrowAlpha = calcArrowAlpha(size);
            float arrowScale = (float) size / mBackgroundMaxDynamicSize;
            float arrowAngle = arrowScale < 0.8f ? 0 : (arrowScale - 0.8f) * 2;
            mArrowPath.reset();
            mArrowPath.moveTo(
                    movedPoint.x - (arrowScale * mArrowHeight / 2),
                    size / 2f + (mArrowWidth * arrowAngle));
            mArrowPath.lineTo(movedPoint.x, size / 2f - (mArrowWidth * arrowAngle));
            mArrowPath.lineTo(
                    movedPoint.x + (arrowScale * mArrowHeight / 2),
                    size / 2f + (mArrowWidth * arrowAngle));
            mArrowPaint.setAlpha(arrowAlpha);
            canvas.drawPath(mArrowPath, mArrowPaint);
        }
    }

    @Override
    public void drawRight(Canvas canvas, PointF downPoint, PointF movedPoint) {
        mBackgroundPath.reset();
        int size = (int) Math.min(mWidth - movedPoint.x, mBackgroundMaxDynamicSize);
        mBackgroundPath.moveTo(0, 0);
        mBackgroundPath.cubicTo(
                0,
                mBackgroundFixedSize / 5,
                -size,
                mBackgroundFixedSize / 3,
                -size,
                mBackgroundFixedSize / 2);
        mBackgroundPath.cubicTo(
                -size,
                mBackgroundFixedSize / 3 * 2,
                0,
                mBackgroundFixedSize / 5 * 4,
                0,
                mBackgroundFixedSize);
        mBackgroundPath.offset(mWidth, movedPoint.y - mBackgroundFixedSize / 2f);
        final int alpha = calcBackgroundAlpha(size);
        mBackgroundPaint.setAlpha(alpha);
        canvas.drawPath(mBackgroundPath, mBackgroundPaint);
        if (size >= mBackgroundMaxDynamicSize / 2.5f) {
            final int arrowAlpha = calcArrowAlpha(size);
            final int x = mWidth - size;
            float arrowScale = (float) size / mBackgroundMaxDynamicSize;
            float arrowAngle = arrowScale < 0.8f ? 0 : (arrowScale - 0.8f) * 2;
            mArrowPath.reset();
            mArrowPath.moveTo(
                    x + size / 2f - (mArrowWidth * arrowAngle),
                    movedPoint.y - (arrowScale * mArrowHeight / 2));
            mArrowPath.lineTo(x + size / 2f + (mArrowWidth * arrowAngle), movedPoint.y);
            mArrowPath.lineTo(
                    x + size / 2f - (mArrowWidth * arrowAngle),
                    movedPoint.y + (arrowScale * mArrowHeight / 2));
            mArrowPaint.setAlpha(arrowAlpha);
            canvas.drawPath(mArrowPath, mArrowPaint);
        }
    }

    @Override
    public void drawBottom(Canvas canvas, PointF downPoint, PointF movedPoint) {
        mBackgroundPath.reset();
        int size = (int) Math.min(mHeight - movedPoint.y, mBackgroundMaxDynamicSize);
        mBackgroundPath.moveTo(0, 0);
        mBackgroundPath.cubicTo(
                mBackgroundFixedSize / 5, 0,
                mBackgroundFixedSize / 3, -size,
                mBackgroundFixedSize / 2, -size);
        mBackgroundPath.cubicTo(
                mBackgroundFixedSize / 3 * 2,
                -size,
                mBackgroundFixedSize / 5 * 4,
                0,
                mBackgroundFixedSize,
                0);
        mBackgroundPath.lineTo(0, 0);
        mBackgroundPath.offset(movedPoint.x - mBackgroundFixedSize / 2f, mHeight);
        final int alpha = calcBackgroundAlpha(size);
        mBackgroundPaint.setAlpha(alpha);
        canvas.drawPath(mBackgroundPath, mBackgroundPaint);
        if (size >= mBackgroundMaxDynamicSize / 2.5f) {
            final int arrowAlpha = calcArrowAlpha(size);
            final int y = mHeight - size;
            float arrowScale = (float) size / mBackgroundMaxDynamicSize;
            float arrowAngle = arrowScale < 0.8f ? 0 : (arrowScale - 0.8f) * 2;
            mArrowPath.reset();
            mArrowPath.moveTo(
                    movedPoint.x - (arrowScale * mArrowHeight / 2),
                    y + size / 2f - (mArrowWidth * arrowAngle));
            mArrowPath.lineTo(movedPoint.x, y + size / 2f + (mArrowWidth * arrowAngle));
            mArrowPath.lineTo(
                    movedPoint.x + (arrowScale * mArrowHeight / 2),
                    y + size / 2f - (mArrowWidth * arrowAngle));
            mArrowPaint.setAlpha(arrowAlpha);
            canvas.drawPath(mArrowPath, mArrowPaint);
        }
    }
}
