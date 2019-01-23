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
package me.dkzwm.widget.esl;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;
import me.dkzwm.widget.esl.annotation.Direction;
import me.dkzwm.widget.esl.annotation.Style;
import me.dkzwm.widget.esl.config.Constants;
import me.dkzwm.widget.esl.graphics.Drawer;
import me.dkzwm.widget.esl.graphics.JIKEDrawer;
import me.dkzwm.widget.esl.graphics.MIUIDrawer;
import me.dkzwm.widget.esl.util.Transformer;

/** Easy to swipe */
public class EasySwipeLayout extends FrameLayout {
    private float mResistance = Constants.DEFAULT_RESISTANCE;
    @Direction private int mDirection = Constants.DIRECTION_LEFT;
    @Style private int mStyle = Constants.STYLE_NONE;
    private int mDurationOfClose = Constants.DEFAULT_DURATION_OF_CLOSE;
    private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;
    private int mTouchPointerId;
    private int mTouchSlop;
    private int mCurrEdge = 0;
    private int mEdgeDiff = -1;
    private float mCurrPosF = 0;
    private VelocityTracker mVelocityTracker;
    private EasyScroller mScroller;
    private Drawer mDrawer;
    private PointF mDownPoint = new PointF();
    private PointF mLastPoint = new PointF();
    private PointF mMovedPoint = new PointF();
    private boolean mHasSendCancelEvent;
    private boolean mHasSendDownEvent;
    private boolean mPreventForAnotherDirection;
    private boolean mDealAnotherDirectionMove;
    private boolean mTriggered = false;
    private boolean mTouched = false;
    private OnSwipeListener mSwipeListener;

    public EasySwipeLayout(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public EasySwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public EasySwipeLayout(
            @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    protected void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        TypedArray arr =
                context.obtainStyledAttributes(attrs, R.styleable.EasySwipeLayout, defStyleAttr, 0);
        if (arr != null) {
            try {
                mEdgeDiff =
                        arr.getDimensionPixelSize(
                                R.styleable.EasySwipeLayout_esl_edgeDiff, mEdgeDiff);
                mDirection = arr.getInt(R.styleable.EasySwipeLayout_esl_direction, mDirection);
                mStyle = arr.getInt(R.styleable.EasySwipeLayout_esl_style, mStyle);
                mResistance = arr.getFloat(R.styleable.EasySwipeLayout_esl_resistance, mResistance);
                mDurationOfClose =
                        arr.getInt(
                                R.styleable.EasySwipeLayout_esl_durationOfClose, mDurationOfClose);
                if (mStyle == Constants.STYLE_CUSTOM) {
                    mDrawer =
                            Transformer.parseDrawer(
                                    context,
                                    arr.getString(R.styleable.EasySwipeLayout_esl_specified));
                    setWillNotDraw(mDrawer == null);
                } else {
                    setStyle(mStyle, null);
                }
            } finally {
                arr.recycle();
            }
        }
        if (mEdgeDiff <= 0) mEdgeDiff = mTouchSlop * 2;
        mScroller = new EasyScroller();
    }

    public void setSwipeListener(@Nullable OnSwipeListener swipeListener) {
        mSwipeListener = swipeListener;
    }

    public void setDirection(@Direction int direction) {
        mDirection = direction;
    }

    public void setStyle(@Style int style, @Nullable String className) {
        if (mStyle != style) {
            mStyle = style;
            switch (mStyle) {
                case Constants.STYLE_NONE:
                    mDrawer = null;
                    break;
                case Constants.STYLE_MIUI:
                    mDrawer = new MIUIDrawer(getContext());
                    break;
                case Constants.STYLE_JIKE:
                    mDrawer = new JIKEDrawer(getContext());
                    break;
                case Constants.STYLE_CUSTOM:
                    mDrawer = Transformer.parseDrawer(getContext(), className);
                    break;
            }
            setWillNotDraw(mDrawer == null);
            if (mDrawer != null && getWidth() > 0 || getHeight() > 0)
                mDrawer.onSizeChanged(getWidth(), getHeight());
            postInvalidate();
        }
    }

    public void setDrawer(@NonNull Drawer drawer) {
        mStyle = Constants.STYLE_CUSTOM;
        if (mDrawer != null) mDrawer.onDetached();
        mDrawer = drawer;
        mDrawer.onAttached(this);
        setWillNotDraw(false);
        if (mDrawer != null && getWidth() > 0 || getHeight() > 0)
            mDrawer.onSizeChanged(getWidth(), getHeight());
        postInvalidate();
    }

    public void setEdgeDiff(@IntRange(from = 0) int edgeDiff) {
        if (edgeDiff <= 0) mEdgeDiff = mTouchSlop * 2;
        else mEdgeDiff = edgeDiff;
        checkConfig();
    }

    public void setResistance(@FloatRange(from = 0) float resistance) {
        mResistance = resistance;
    }

    public void setDurationOfClose(@IntRange(from = 0) int durationOfClose) {
        mDurationOfClose = durationOfClose;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mDrawer != null) mDrawer.onSizeChanged(w, h);
        checkConfig();
    }

    private void checkConfig() {
        if (getWidth() <= 0 || getHeight() <= 0) return;
        if (mDirection == Constants.DIRECTION_ALL) {
            if (getWidth() / 2 <= mEdgeDiff || getHeight() / 2 <= mEdgeDiff)
                throw new IllegalArgumentException(
                        "EdgeDiff must be less than the half of " + "width or height");
        } else if (mDirection == Constants.DIRECTION_HORIZONTAL) {
            if (getWidth() / 2 <= mEdgeDiff)
                throw new IllegalArgumentException(
                        "EdgeDiff must be less than the half of " + "width");
        } else if (mDirection == Constants.DIRECTION_VERTICAL) {
            if (getHeight() / 2 <= mEdgeDiff)
                throw new IllegalArgumentException(
                        "EdgeDiff must be less than the half of " + "height");
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) reset();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isEnabled() || mSwipeListener == null) return super.dispatchTouchEvent(ev);
        final int action = ev.getActionMasked();
        if (mVelocityTracker == null) mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(ev);
        switch (action) {
            case MotionEvent.ACTION_UP:
                final int pointerId = ev.getPointerId(0);
                mVelocityTracker.computeCurrentVelocity(200, mMaximumFlingVelocity);
                float vy = mVelocityTracker.getYVelocity(pointerId);
                float vx = mVelocityTracker.getXVelocity(pointerId);
                if (Math.abs(vx) >= mMinimumFlingVelocity
                        || Math.abs(vy) >= mMinimumFlingVelocity) {
                    boolean handler = onFling(vx, vy);
                    if (handler) ev.setAction(MotionEvent.ACTION_CANCEL);
                }
            case MotionEvent.ACTION_CANCEL:
                if (mVelocityTracker != null) mVelocityTracker.recycle();
                mVelocityTracker = null;
                mHasSendCancelEvent = false;
                mDealAnotherDirectionMove = false;
                mPreventForAnotherDirection = false;
                mTouched = false;
                mLastPoint.set(ev.getX(), ev.getY());
                requestParentDisallowInterceptTouchEvent(false);
                if (mScroller.isFinished()) mScroller.startScroll(0, mDurationOfClose);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                final int pointerIndex =
                        (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                                >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                if (ev.getPointerId(pointerIndex) == mTouchPointerId) {
                    final int newIndex = pointerIndex == 0 ? 1 : 0;
                    mTouchPointerId = ev.getPointerId(newIndex);
                    mLastPoint.set(ev.getX(newIndex), ev.getY(newIndex));
                }
                final int count = ev.getPointerCount();
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(ev);
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                    final int upIndex = ev.getActionIndex();
                    final int id1 = ev.getPointerId(upIndex);
                    final float x1 = mVelocityTracker.getXVelocity(id1);
                    final float y1 = mVelocityTracker.getYVelocity(id1);
                    for (int i = 0; i < count; i++) {
                        if (i == upIndex) continue;
                        final int id2 = ev.getPointerId(i);
                        final float x = x1 * mVelocityTracker.getXVelocity(id2);
                        final float y = y1 * mVelocityTracker.getYVelocity(id2);
                        final float dot = x + y;
                        if (dot < 0) {
                            mVelocityTracker.clear();
                            break;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                reset();
                mTouchPointerId = ev.getPointerId(0);
                mDownPoint.set(ev.getX(), ev.getY());
                mLastPoint.set(ev.getX(), ev.getY());
                mCurrEdge = getEdgesTouched(ev.getX(), ev.getY()) & mDirection;
                if (mCurrEdge == 0) {
                    updatePos();
                    break;
                }
                requestParentDisallowInterceptTouchEvent(true);
                mTouched = true;
                super.dispatchTouchEvent(ev);
                return true;
            case MotionEvent.ACTION_MOVE:
                final int index = ev.findPointerIndex(mTouchPointerId);
                if (index < 0) break;
                dealAnotherDirectionMove(ev, index);
                if (mPreventForAnotherDirection) break;
                sendCancelEvent(ev);
                calcOffset(ev.getX(index), ev.getY(index));
                if (mCurrPosF == 0) {
                    sendDownEvent(ev);
                    updatePos();
                    break;
                } else {
                    updatePos();
                    return true;
                }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void requestParentDisallowInterceptTouchEvent(boolean intercept) {
        if (getParent() != null) getParent().requestDisallowInterceptTouchEvent(intercept);
    }

    private void dealAnotherDirectionMove(MotionEvent event, int index) {
        if (!mDealAnotherDirectionMove) {
            final float offsetX = event.getX(index) - mDownPoint.x;
            final float offsetY = event.getY(index) - mDownPoint.y;
            if (mCurrEdge == Constants.DIRECTION_LEFT) {
                if (offsetX < -mTouchSlop) {
                    mDealAnotherDirectionMove = true;
                    mPreventForAnotherDirection = false;
                } else if (offsetX >= mTouchSlop) {
                    mPreventForAnotherDirection = false;
                    mDealAnotherDirectionMove = true;
                } else {
                    mPreventForAnotherDirection = false;
                }
            } else if (mCurrEdge == Constants.DIRECTION_RIGHT) {
                if (offsetX > mTouchSlop) {
                    mDealAnotherDirectionMove = true;
                    mPreventForAnotherDirection = false;
                } else if (-offsetX >= mTouchSlop) {
                    mPreventForAnotherDirection = false;
                    mDealAnotherDirectionMove = true;
                } else {
                    mPreventForAnotherDirection = false;
                }
            } else if (mCurrEdge == Constants.DIRECTION_TOP) {
                if (offsetY < -mTouchSlop) {
                    mDealAnotherDirectionMove = true;
                    mPreventForAnotherDirection = false;
                } else if (offsetY >= mTouchSlop) {
                    mPreventForAnotherDirection = false;
                    mDealAnotherDirectionMove = true;
                } else {
                    mPreventForAnotherDirection = false;
                }
            } else {
                if (offsetY > mTouchSlop) {
                    mDealAnotherDirectionMove = true;
                    mPreventForAnotherDirection = false;
                } else if (-offsetY >= mTouchSlop) {
                    mPreventForAnotherDirection = false;
                    mDealAnotherDirectionMove = true;
                } else {
                    mPreventForAnotherDirection = false;
                }
            }
        }
    }

    private void calcOffset(float x, float y) {
        float offset;
        if ((mCurrEdge & Constants.DIRECTION_HORIZONTAL) != 0) {
            offset = (x - mLastPoint.x) / mResistance;
        } else {
            offset = (y - mLastPoint.y) / mResistance;
        }
        mCurrPosF = mCurrPosF + offset;
        if (mCurrEdge == Constants.DIRECTION_LEFT || mCurrEdge == Constants.DIRECTION_TOP) {
            if (mCurrPosF < 0) mCurrPosF = 0;
            else if (mDrawer != null && mCurrPosF > mDrawer.getMaxSize())
                mCurrPosF = mDrawer.getMaxSize();
        } else {
            if (mCurrPosF > 0) mCurrPosF = 0;
            else if (mDrawer != null && mCurrPosF < -mDrawer.getMaxSize())
                mCurrPosF = -mDrawer.getMaxSize();
        }
        mLastPoint.set(x, y);
    }

    private int getEdgesTouched(float x, float y) {
        int result = 0;
        if (x < mEdgeDiff) result |= Constants.DIRECTION_LEFT;
        if (y < mEdgeDiff) result |= Constants.DIRECTION_TOP;
        if (x > getWidth() - mEdgeDiff) result |= Constants.DIRECTION_RIGHT;
        if (y > getHeight() - mEdgeDiff) result |= Constants.DIRECTION_BOTTOM;
        return result;
    }

    private void sendCancelEvent(final MotionEvent event) {
        if (mHasSendCancelEvent || event == null) return;
        final MotionEvent ev =
                MotionEvent.obtain(
                        event.getDownTime(),
                        event.getEventTime(),
                        MotionEvent.ACTION_CANCEL,
                        event.getX(),
                        event.getY(),
                        event.getMetaState());
        super.dispatchTouchEvent(ev);
        ev.recycle();
        mHasSendCancelEvent = true;
        mHasSendDownEvent = false;
    }

    protected void sendDownEvent(final MotionEvent event) {
        if (mHasSendDownEvent || event == null) return;
        requestParentDisallowInterceptTouchEvent(false);
        final MotionEvent ev =
                MotionEvent.obtain(
                        event.getDownTime(),
                        event.getEventTime(),
                        MotionEvent.ACTION_DOWN,
                        event.getX(),
                        event.getY(),
                        event.getMetaState());
        if (getParent() != null && getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).dispatchTouchEvent(ev);
        } else {
            super.dispatchTouchEvent(ev);
        }
        ev.recycle();
        mHasSendCancelEvent = false;
        mHasSendDownEvent = true;
    }

    protected boolean onFling(float vx, final float vy) {
        boolean handler = false;
        int velocity = 0;
        switch (mCurrEdge) {
            case Constants.DIRECTION_LEFT:
                handler = vx > mMinimumFlingVelocity;
                velocity = (int) vx;
                break;
            case Constants.DIRECTION_RIGHT:
                handler = vx < -mMinimumFlingVelocity;
                velocity = (int) vx;
                break;
            case Constants.DIRECTION_TOP:
                handler = vy > mMinimumFlingVelocity;
                velocity = (int) vy;
                break;
            case Constants.DIRECTION_BOTTOM:
                handler = vy < -mMinimumFlingVelocity;
                velocity = (int) vy;
                break;
        }
        if (handler && mDrawer != null) {
            int[] calc = mScroller.calc(velocity);
            mScroller.startScroll(
                    Math.min(calc[0] + Math.round(mCurrPosF), mDrawer.getMaxSize()),
                    Math.min(calc[1], mDurationOfClose));
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawer != null) {
            switch (mCurrEdge) {
                case Constants.DIRECTION_LEFT:
                    mMovedPoint.set(mCurrPosF, mLastPoint.y);
                    mDrawer.drawLeft(canvas, mDownPoint, mMovedPoint);
                    break;
                case Constants.DIRECTION_RIGHT:
                    mMovedPoint.set(getWidth() + mCurrPosF, mLastPoint.y);
                    mDrawer.drawRight(canvas, mDownPoint, mMovedPoint);
                    break;
                case Constants.DIRECTION_TOP:
                    mMovedPoint.set(mLastPoint.x, mCurrPosF);
                    mDrawer.drawTop(canvas, mDownPoint, mMovedPoint);
                    break;
                case Constants.DIRECTION_BOTTOM:
                    mMovedPoint.set(mLastPoint.x, getHeight() + mCurrPosF);
                    mDrawer.drawBottom(canvas, mDownPoint, mMovedPoint);
                    break;
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mDrawer != null) mDrawer.onAttached(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        reset();
        if (mDrawer != null) mDrawer.onDetached();
    }

    private void updatePos() {
        if (mStyle == Constants.STYLE_NONE || mDrawer == null) return;
        if (!mTouched && !mTriggered) {
            if (mDrawer.canTrigger(mCurrEdge, Math.abs(mCurrPosF))) {
                if (mSwipeListener != null) mSwipeListener.onSwipe(mCurrEdge);
                mTriggered = true;
            }
        }
        invalidate();
    }

    private void reset() {
        mScroller.stop();
        mCurrPosF = 0;
        mTriggered = false;
        invalidate();
    }

    private class EasyScroller implements Runnable {
        Scroller $Scroller;

        private EasyScroller() {
            $Scroller = new Scroller(getContext());
        }

        public void run() {
            if ($Scroller.computeScrollOffset()) {
                mCurrPosF = $Scroller.getCurrY();
                updatePos();
                if (mCurrPosF == $Scroller.getFinalY()) $Scroller.abortAnimation();
                ViewCompat.postOnAnimation(EasySwipeLayout.this, this);
            } else if (mCurrPosF != 0) {
                startScroll(0, mDurationOfClose);
            } else {
                reset();
            }
        }

        private void startScroll(int to, int duration) {
            stop();
            final int currPos = Math.round(mCurrPosF);
            int distance = currPos - to;
            $Scroller.startScroll(0, currPos, 0, -distance, duration);
            ViewCompat.postOnAnimation(EasySwipeLayout.this, this);
        }

        private int[] calc(int velocity) {
            stop();
            $Scroller.fling(
                    0,
                    0,
                    0,
                    velocity,
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE,
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE);
            int[] calc = new int[2];
            calc[0] = $Scroller.getFinalY();
            calc[1] = $Scroller.getDuration();
            $Scroller.forceFinished(true);
            return calc;
        }

        private boolean isFinished() {
            return $Scroller.isFinished();
        }

        private void stop() {
            removeCallbacks(this);
            $Scroller.abortAnimation();
        }
    }
}
