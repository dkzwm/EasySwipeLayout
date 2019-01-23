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
import android.support.annotation.CallSuper;
import me.dkzwm.widget.esl.EasySwipeLayout;

public abstract class Drawer {
    protected Context mContext;
    protected EasySwipeLayout mSwipeLayout;
    protected boolean mAttached = false;
    protected int mWidth = 0;
    protected int mHeight = 0;

    public Drawer(Context context) {
        mContext = context;
    }

    public void onSizeChanged(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public void onAttached(EasySwipeLayout layout) {
        mAttached = true;
        mSwipeLayout = layout;
    }

    public abstract int getMaxSize();

    public abstract void drawLeft(Canvas canvas, PointF downPoint, PointF movedPoint);

    public abstract void drawTop(Canvas canvas, PointF downPoint, PointF movedPoint);

    public abstract void drawRight(Canvas canvas, PointF downPoint, PointF movedPoint);

    public abstract void drawBottom(Canvas canvas, PointF downPoint, PointF movedPoint);

    public abstract boolean canTrigger(int edge, float pos);

    protected void requestInvalidate() {
        if (mSwipeLayout != null) mSwipeLayout.postInvalidate();
    }

    @CallSuper
    public void onDetached() {
        mAttached = false;
        mSwipeLayout = null;
        mContext = null;
    }
}
