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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.TypedValue;
import androidx.annotation.ColorInt;

public class MIUIDrawer extends Drawer {
    protected Paint mBackgroundPaint;
    protected Paint mArrowPaint;
    protected Path mBackgroundPath = new Path();
    protected Path mArrowPath = new Path();
    protected float mBackgroundFixedSize;
    protected float mArrowStrokeWidth;
    protected int mBackgroundMaxDynamicSize;
    protected int mArrowHeight;
    protected int mArrowWidth;
    @ColorInt protected int mBackgroundColor = Color.BLACK;
    @ColorInt protected int mArrowColor = Color.WHITE;

    public MIUIDrawer(Context context) {
        super(context);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setStrokeWidth(1);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setAlpha((int) (0.74f * 255));
        mArrowStrokeWidth =
                (int)
                        TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                2,
                                mContext.getResources().getDisplayMetrics());
        mArrowHeight =
                (int)
                        TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                12,
                                mContext.getResources().getDisplayMetrics());
        mArrowWidth =
                (int)
                        TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                5f,
                                mContext.getResources().getDisplayMetrics());
        mArrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArrowPaint.setStyle(Paint.Style.STROKE);
        mArrowPaint.setColor(mArrowColor);
        mArrowPaint.setStrokeWidth(mArrowStrokeWidth);
        mArrowPaint.setStrokeJoin(Paint.Join.ROUND);
        mBackgroundFixedSize = context.getResources().getDisplayMetrics().heightPixels / 3.5f;
        mBackgroundMaxDynamicSize =
                (int)
                        TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                26,
                                mContext.getResources().getDisplayMetrics());
    }

    public void setBackgroundMaxDynamicSize(int size) {
        mBackgroundMaxDynamicSize = size;
        requestInvalidate();
    }

    public void setBackgroundFixedSize(int size) {
        mBackgroundFixedSize = size;
        requestInvalidate();
    }

    public void setBackgroundColor(@ColorInt int color) {
        if (mBackgroundColor != color) {
            mBackgroundColor = color;
            mBackgroundPaint.setColor(mBackgroundColor);
            requestInvalidate();
        }
    }

    public void setArrowColor(@ColorInt int color) {
        if (mArrowColor != color) {
            mArrowColor = color;
            mArrowPaint.setColor(mArrowColor);
            requestInvalidate();
        }
    }

    public void setArrowStrokeWidth(float arrowStrokeWidth) {
        if (mArrowStrokeWidth != arrowStrokeWidth) {
            mArrowStrokeWidth = arrowStrokeWidth;
            mArrowPaint.setStrokeWidth(mArrowStrokeWidth);
            requestInvalidate();
        }
    }

    public void setArrowHeight(int arrowHeight) {
        mArrowHeight = arrowHeight;
        requestInvalidate();
    }

    public void setArrowWidth(int arrowWidth) {
        mArrowWidth = arrowWidth;
        requestInvalidate();
    }

    @Override
    public int getMaxSize() {
        return mBackgroundMaxDynamicSize;
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
        mBackgroundPath.offset(0, downPoint.y - mBackgroundFixedSize / 2f);
        canvas.drawPath(mBackgroundPath, mBackgroundPaint);
        if (size >= mBackgroundMaxDynamicSize / 2.5f) {
            mArrowPath.reset();
            mArrowPath.moveTo(size / 2f + mArrowWidth / 2f, downPoint.y + mArrowHeight / 2f);
            mArrowPath.lineTo(size / 2f - mArrowWidth / 2f, downPoint.y);
            mArrowPath.lineTo(size / 2f + mArrowWidth / 2f, downPoint.y - mArrowHeight / 2f);
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
        mBackgroundPath.offset(downPoint.x - mBackgroundFixedSize / 2f, 0);
        canvas.drawPath(mBackgroundPath, mBackgroundPaint);
        if (size >= mBackgroundMaxDynamicSize / 2.5f) {
            mArrowPath.reset();
            mArrowPath.moveTo(downPoint.x + mArrowHeight / 2f, size / 2f + mArrowWidth / 2f);
            mArrowPath.lineTo(downPoint.x, size / 2f - mArrowWidth / 2f);
            mArrowPath.lineTo(downPoint.x - mArrowHeight / 2f, size / 2f + mArrowWidth / 2f);
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
        mBackgroundPath.offset(mWidth, downPoint.y - mBackgroundFixedSize / 2f);
        canvas.drawPath(mBackgroundPath, mBackgroundPaint);
        if (size >= mBackgroundMaxDynamicSize / 2.5f) {
            final int x = mWidth - size;
            mArrowPath.reset();
            mArrowPath.moveTo(x + size / 2f - mArrowWidth / 2f, downPoint.y + mArrowHeight / 2f);
            mArrowPath.lineTo(x + size / 2f + mArrowWidth / 2f, downPoint.y);
            mArrowPath.lineTo(x + size / 2f - mArrowWidth / 2f, downPoint.y - mArrowHeight / 2f);
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
        mBackgroundPath.offset(downPoint.x - mBackgroundFixedSize / 2f, mHeight);
        canvas.drawPath(mBackgroundPath, mBackgroundPaint);
        if (size >= mBackgroundMaxDynamicSize / 2.5f) {
            final int y = mHeight - size;
            mArrowPath.reset();
            mArrowPath.moveTo(downPoint.x + mArrowHeight / 2f, y + size / 2f - mArrowWidth / 2f);
            mArrowPath.lineTo(downPoint.x, y + size / 2f + mArrowWidth / 2f);
            mArrowPath.lineTo(downPoint.x - mArrowHeight / 2f, y + size / 2f - mArrowWidth / 2f);
            canvas.drawPath(mArrowPath, mArrowPaint);
        }
    }

    @Override
    public boolean canTrigger(int edge, float x) {
        return x >= getMaxSize() / 3 * 2;
    }
}
