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

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Scroller
import androidx.annotation.IntRange
import androidx.core.view.ViewCompat
import me.dkzwm.widget.esl.annotation.Direction
import me.dkzwm.widget.esl.annotation.Style
import me.dkzwm.widget.esl.config.Constants
import me.dkzwm.widget.esl.graphics.Drawer
import me.dkzwm.widget.esl.graphics.JIKEDrawer
import me.dkzwm.widget.esl.graphics.MIUIDrawer
import me.dkzwm.widget.esl.graphics.NoneDrawer
import me.dkzwm.widget.esl.util.Transformer

/** Easy to swipe  */
open class EasySwipeLayout : FrameLayout {
    private lateinit var mScroller: EasyScroller
    private var mResistance = Constants.DEFAULT_RESISTANCE
    @Direction
    private var mDirection = Constants.DIRECTION_LEFT
    @Style
    private var mStyle = Constants.STYLE_MIUI
    private var mDurationOfClose = Constants.DEFAULT_DURATION_OF_CLOSE
    private var mMinimumFlingVelocity = 0F
    private var mMaximumFlingVelocity = 0F
    private var mTouchPointerId: Int = 0
    private var mTouchSlop: Int = 0
    private var mCurrEdge = 0
    private var mEdgeDiff = -1
    private var mCurrPosF = 0f
    private var mVelocityTracker: VelocityTracker? = null
    private var mDrawer: Drawer? = null
    private val mDownPoint = PointF()
    private val mLastPoint = PointF()
    private val mMovedPoint = PointF()
    private var mHasSendCancelEvent: Boolean = false
    private var mHasSendDownEvent: Boolean = false
    private var mPreventForAnotherDirection: Boolean = false
    private var mDealAnotherDirectionMove: Boolean = false
    private var mTriggered = false
    private var mTouched = false
    private var mSwipeListener: OnSwipeListener? = null

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(
            context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    protected open fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val viewConfiguration = ViewConfiguration.get(getContext())
        mTouchSlop = viewConfiguration.scaledTouchSlop
        mMaximumFlingVelocity = viewConfiguration.scaledMaximumFlingVelocity.toFloat()
        mMinimumFlingVelocity = viewConfiguration.scaledMinimumFlingVelocity.toFloat()
        val arr = context.obtainStyledAttributes(attrs, R.styleable.EasySwipeLayout,
                defStyleAttr, 0)
        if (arr != null) {
            try {
                mEdgeDiff = arr.getDimensionPixelSize(
                        R.styleable.EasySwipeLayout_esl_edgeDiff, mEdgeDiff)
                if (mEdgeDiff <= 0) mEdgeDiff = mTouchSlop * 2
                mDirection = arr.getInt(R.styleable.EasySwipeLayout_esl_direction, mDirection)
                mStyle = arr.getInt(R.styleable.EasySwipeLayout_esl_style, mStyle)
                mResistance = arr.getFloat(R.styleable.EasySwipeLayout_esl_resistance, mResistance)
                mDurationOfClose = arr.getInt(
                        R.styleable.EasySwipeLayout_esl_durationOfClose, mDurationOfClose)
                when (mStyle) {
                    Constants.STYLE_CUSTOM -> {
                        mDrawer = Transformer.parseDrawer(
                                context,
                                arr.getString(R.styleable.EasySwipeLayout_esl_specified))
                        setWillNotDraw(mDrawer == null)
                    }
                    Constants.STYLE_NONE -> {
                        mDrawer = NoneDrawer(getContext())
                        setWillNotDraw(true)
                    }
                    Constants.STYLE_MIUI -> {
                        mDrawer = MIUIDrawer(getContext())
                        setWillNotDraw(false)
                    }
                    else -> {
                        mDrawer = JIKEDrawer(getContext())
                        setWillNotDraw(false)
                    }
                }
            } finally {
                arr.recycle()
            }
        } else {
            mDrawer = MIUIDrawer(getContext())
            setWillNotDraw(false)
            mEdgeDiff = mTouchSlop * 2
        }
        mScroller = EasyScroller(context)
    }

    fun setSwipeListener(swipeListener: OnSwipeListener?) {
        mSwipeListener = swipeListener
    }

    fun setDirection(@Direction direction: Int) {
        mDirection = direction
    }

    fun setStyle(@Style style: Int, className: String?) {
        if (mStyle != style) {
            mStyle = style
            when (mStyle) {
                Constants.STYLE_NONE -> {
                    mDrawer = NoneDrawer(context)
                    setWillNotDraw(true)
                }
                Constants.STYLE_MIUI -> {
                    mDrawer = MIUIDrawer(context)
                    setWillNotDraw(false)
                }
                Constants.STYLE_JIKE -> {
                    mDrawer = JIKEDrawer(context)
                    setWillNotDraw(false)
                }
                Constants.STYLE_CUSTOM -> {
                    mDrawer = Transformer.parseDrawer(context, className)
                    setWillNotDraw(mDrawer == null)
                }
            }
            if (mDrawer != null && width > 0 || height > 0)
                mDrawer?.onSizeChanged(width, height)
            postInvalidate()
        }
    }

    fun setDrawer(drawer: Drawer?) {
        mStyle = Constants.STYLE_CUSTOM
        mDrawer?.onDetached()
        mDrawer = drawer
        if (drawer != null) {
            drawer.onAttached(this)
            setWillNotDraw(false)
            if (width > 0 || height > 0)
                drawer.onSizeChanged(width, height)
            postInvalidate()
        } else {
            setWillNotDraw(true)
        }
    }

    fun setEdgeDiff(@IntRange(from = 0) edgeDiff: Int) {
        mEdgeDiff = if (edgeDiff <= 0) mTouchSlop * 2 else edgeDiff
        checkConfig()
    }

    fun setResistance(resistance: Float) {
        mResistance = resistance
    }

    fun setDurationOfClose(@IntRange(from = 0) durationOfClose: Int) {
        mDurationOfClose = durationOfClose
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mDrawer?.onSizeChanged(w, h)
        checkConfig()
    }

    private fun checkConfig() {
        if (width <= 0 || height <= 0) return
        if (mDirection == Constants.DIRECTION_ALL) {
            if (width / 2 <= mEdgeDiff || height / 2 <= mEdgeDiff)
                throw IllegalArgumentException(
                        "EdgeDiff must be less than the half of " + "width or height")
        } else if (mDirection == Constants.DIRECTION_HORIZONTAL) {
            if (width / 2 <= mEdgeDiff)
                throw IllegalArgumentException(
                        "EdgeDiff must be less than the half of " + "width")
        } else if (mDirection == Constants.DIRECTION_VERTICAL) {
            if (height / 2 <= mEdgeDiff)
                throw IllegalArgumentException(
                        "EdgeDiff must be less than the half of " + "height")
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (!enabled) reset()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (!isEnabled || mSwipeListener == null) return super.dispatchTouchEvent(ev)
        val action = ev.actionMasked
        if (mVelocityTracker == null) mVelocityTracker = VelocityTracker.obtain()
        mVelocityTracker?.addMovement(ev)
        when (action) {
            MotionEvent.ACTION_UP -> {
                val pointerId = ev.getPointerId(0)
                val velocityTracker = mVelocityTracker
                if (velocityTracker != null) {
                    velocityTracker.computeCurrentVelocity(200, mMaximumFlingVelocity)
                    val vy = velocityTracker.getYVelocity(pointerId)
                    val vx = velocityTracker.getXVelocity(pointerId)
                    if (Math.abs(vx) >= mMinimumFlingVelocity || Math.abs(vy) >= mMinimumFlingVelocity) {
                        val handler = onFling(vx, vy)
                        if (handler) ev.action = MotionEvent.ACTION_CANCEL
                    }
                    velocityTracker.recycle()
                }
                mVelocityTracker = null
                mHasSendCancelEvent = false
                mDealAnotherDirectionMove = false
                mPreventForAnotherDirection = false
                mTouched = false
                mLastPoint.set(ev.x, ev.y)
                requestParentDisallowInterceptTouchEvent(false)
                if (mScroller.isFinished) mScroller.startScroll(0, mDurationOfClose)
            }
            MotionEvent.ACTION_CANCEL -> {
                mVelocityTracker?.recycle()
                mVelocityTracker = null
                mHasSendCancelEvent = false
                mDealAnotherDirectionMove = false
                mPreventForAnotherDirection = false
                mTouched = false
                mLastPoint.set(ev.x, ev.y)
                requestParentDisallowInterceptTouchEvent(false)
                if (mScroller.isFinished) mScroller.startScroll(0, mDurationOfClose)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = ev.action and
                        MotionEvent.ACTION_POINTER_INDEX_MASK shr
                        MotionEvent.ACTION_POINTER_INDEX_SHIFT
                if (ev.getPointerId(pointerIndex) == mTouchPointerId) {
                    val newIndex = if (pointerIndex == 0) 1 else 0
                    mTouchPointerId = ev.getPointerId(newIndex)
                    mLastPoint.set(ev.getX(newIndex), ev.getY(newIndex))
                }
                val count = ev.pointerCount
                val velocityTracker = mVelocityTracker
                if (velocityTracker != null) {
                    velocityTracker.addMovement(ev)
                    velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity)
                    val upIndex = ev.actionIndex
                    val id1 = ev.getPointerId(upIndex)
                    val x1 = velocityTracker.getXVelocity(id1)
                    val y1 = velocityTracker.getYVelocity(id1)
                    for (i in 0 until count) {
                        if (i == upIndex) continue
                        val id2 = ev.getPointerId(i)
                        val x = x1 * velocityTracker.getXVelocity(id2)
                        val y = y1 * velocityTracker.getYVelocity(id2)
                        val dot = x + y
                        if (dot < 0) {
                            velocityTracker.clear()
                            break
                        }
                    }
                }
            }
            MotionEvent.ACTION_DOWN -> {
                reset()
                mTouchPointerId = ev.getPointerId(0)
                mDownPoint.set(ev.x, ev.y)
                mLastPoint.set(ev.x, ev.y)
                mCurrEdge = getEdgesTouched(ev.x, ev.y) and mDirection
                if (mCurrEdge == 0) {
                    updatePos()
                    return super.dispatchTouchEvent(ev)
                }
                requestParentDisallowInterceptTouchEvent(true)
                mTouched = true
                super.dispatchTouchEvent(ev)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val index = ev.findPointerIndex(mTouchPointerId)
                if (index < 0) return super.dispatchTouchEvent(ev)
                dealAnotherDirectionMove(ev, index)
                if (mPreventForAnotherDirection) return super.dispatchTouchEvent(ev)
                sendCancelEvent(ev)
                calcOffset(ev.getX(index), ev.getY(index))
                return if (mCurrPosF == 0f) {
                    sendDownEvent(ev)
                    updatePos()
                    super.dispatchTouchEvent(ev)
                } else {
                    updatePos()
                    true
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun requestParentDisallowInterceptTouchEvent(intercept: Boolean) {
        if (parent != null) parent.requestDisallowInterceptTouchEvent(intercept)
    }

    private fun dealAnotherDirectionMove(event: MotionEvent, index: Int) {
        if (!mDealAnotherDirectionMove) {
            val offsetX = event.getX(index) - mDownPoint.x
            val offsetY = event.getY(index) - mDownPoint.y
            when (mCurrEdge) {
                Constants.DIRECTION_LEFT -> when {
                    offsetX < -mTouchSlop -> {
                        mDealAnotherDirectionMove = true
                        mPreventForAnotherDirection = false
                    }
                    offsetX >= mTouchSlop -> {
                        mPreventForAnotherDirection = false
                        mDealAnotherDirectionMove = true
                    }
                    else -> mPreventForAnotherDirection = false
                }
                Constants.DIRECTION_RIGHT -> when {
                    offsetX > mTouchSlop -> {
                        mDealAnotherDirectionMove = true
                        mPreventForAnotherDirection = false
                    }
                    -offsetX >= mTouchSlop -> {
                        mPreventForAnotherDirection = false
                        mDealAnotherDirectionMove = true
                    }
                    else -> mPreventForAnotherDirection = false
                }
                Constants.DIRECTION_TOP -> when {
                    offsetY < -mTouchSlop -> {
                        mDealAnotherDirectionMove = true
                        mPreventForAnotherDirection = false
                    }
                    offsetY >= mTouchSlop -> {
                        mPreventForAnotherDirection = false
                        mDealAnotherDirectionMove = true
                    }
                    else -> mPreventForAnotherDirection = false
                }
                else -> when {
                    offsetY > mTouchSlop -> {
                        mDealAnotherDirectionMove = true
                        mPreventForAnotherDirection = false
                    }
                    -offsetY >= mTouchSlop -> {
                        mPreventForAnotherDirection = false
                        mDealAnotherDirectionMove = true
                    }
                    else -> mPreventForAnotherDirection = false
                }
            }
        }
    }

    private fun calcOffset(x: Float, y: Float) {
        val offset: Float = if (mCurrEdge and Constants.DIRECTION_HORIZONTAL != 0) {
            (x - mLastPoint.x) / mResistance
        } else {
            (y - mLastPoint.y) / mResistance
        }
        mCurrPosF += offset
        val drawer = mDrawer
        if (mCurrEdge == Constants.DIRECTION_LEFT || mCurrEdge == Constants.DIRECTION_TOP) {
            if (mCurrPosF < 0)
                mCurrPosF = 0f
            else if (drawer != null && mCurrPosF > drawer.maxSize)
                mCurrPosF = drawer.maxSize.toFloat()
        } else {
            if (mCurrPosF > 0)
                mCurrPosF = 0f
            else if (drawer != null && mCurrPosF < -drawer.maxSize)
                mCurrPosF = (-drawer.maxSize).toFloat()
        }
        mLastPoint.set(x, y)
    }

    private fun getEdgesTouched(x: Float, y: Float): Int {
        var result = 0
        if (x < mEdgeDiff) result = result or Constants.DIRECTION_LEFT
        if (y < mEdgeDiff) result = result or Constants.DIRECTION_TOP
        if (x > width - mEdgeDiff) result = result or Constants.DIRECTION_RIGHT
        if (y > height - mEdgeDiff) result = result or Constants.DIRECTION_BOTTOM
        return result
    }

    protected fun sendCancelEvent(event: MotionEvent?) {
        if (mHasSendCancelEvent || event == null) return
        val ev = MotionEvent.obtain(
                event.downTime,
                event.eventTime,
                MotionEvent.ACTION_CANCEL,
                event.x,
                event.y,
                event.metaState)
        super.dispatchTouchEvent(ev)
        ev.recycle()
        mHasSendCancelEvent = true
        mHasSendDownEvent = false
    }

    protected fun sendDownEvent(event: MotionEvent?) {
        if (mHasSendDownEvent || event == null) return
        requestParentDisallowInterceptTouchEvent(false)
        val ev = MotionEvent.obtain(
                event.downTime,
                event.eventTime,
                MotionEvent.ACTION_DOWN,
                event.x,
                event.y,
                event.metaState)
        if (parent != null && parent is ViewGroup) {
            (parent as ViewGroup).dispatchTouchEvent(ev)
        } else {
            super.dispatchTouchEvent(ev)
        }
        ev.recycle()
        mHasSendCancelEvent = false
        mHasSendDownEvent = true
    }

    protected fun onFling(vx: Float, vy: Float): Boolean {
        var handler = false
        var velocity = 0
        when (mCurrEdge) {
            Constants.DIRECTION_LEFT -> {
                handler = vx > mMinimumFlingVelocity
                velocity = vx.toInt()
            }
            Constants.DIRECTION_RIGHT -> {
                handler = vx < -mMinimumFlingVelocity
                velocity = vx.toInt()
            }
            Constants.DIRECTION_TOP -> {
                handler = vy > mMinimumFlingVelocity
                velocity = vy.toInt()
            }
            Constants.DIRECTION_BOTTOM -> {
                handler = vy < -mMinimumFlingVelocity
                velocity = vy.toInt()
            }
        }
        val drawer = mDrawer
        return if (handler && drawer != null) {
            val calc = mScroller.calc(velocity)
            mScroller.startScroll(
                    Math.min(calc[0] + Math.round(mCurrPosF), drawer.maxSize),
                    Math.min(calc[1], mDurationOfClose))
            true
        } else {
            false
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val drawer = mDrawer
        if (drawer != null) {
            when (mCurrEdge) {
                Constants.DIRECTION_LEFT -> {
                    mMovedPoint.set(mCurrPosF, mLastPoint.y)
                    drawer.drawLeft(canvas, mDownPoint, mMovedPoint)
                }
                Constants.DIRECTION_RIGHT -> {
                    mMovedPoint.set(width + mCurrPosF, mLastPoint.y)
                    drawer.drawRight(canvas, mDownPoint, mMovedPoint)
                }
                Constants.DIRECTION_TOP -> {
                    mMovedPoint.set(mLastPoint.x, mCurrPosF)
                    drawer.drawTop(canvas, mDownPoint, mMovedPoint)
                }
                Constants.DIRECTION_BOTTOM -> {
                    mMovedPoint.set(mLastPoint.x, height + mCurrPosF)
                    drawer.drawBottom(canvas, mDownPoint, mMovedPoint)
                }
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mDrawer?.onAttached(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        reset()
        mDrawer?.onDetached()
    }

    private fun updatePos() {
        if (mDrawer == null) return
        if (!mTouched && !mTriggered) {
            if (mDrawer?.canTrigger(mCurrEdge, Math.abs(mCurrPosF)) == true) {
                mSwipeListener?.onSwipe(mCurrEdge)
                mTriggered = true
            }
        }
        invalidate()
    }

    public fun reset() {
        mScroller.stop()
        mCurrPosF = 0f
        mTriggered = false
        invalidate()
    }

    private inner class EasyScroller constructor(context: Context) : Runnable {
        internal var iScroller: Scroller = Scroller(context)

        val isFinished: Boolean
            get() = iScroller.isFinished

        override fun run() {
            when {
                iScroller.computeScrollOffset() -> {
                    mCurrPosF = iScroller.currY.toFloat()
                    updatePos()
                    if (mCurrPosF == iScroller.finalY.toFloat()) iScroller.abortAnimation()
                    ViewCompat.postOnAnimation(this@EasySwipeLayout, this)
                }
                mCurrPosF != 0f -> startScroll(0, mDurationOfClose)
                else -> reset()
            }
        }

        fun startScroll(to: Int, duration: Int) {
            stop()
            val currPos = Math.round(mCurrPosF)
            val distance = currPos - to
            iScroller.startScroll(0, currPos, 0, -distance, duration)
            ViewCompat.postOnAnimation(this@EasySwipeLayout, this)
        }

        fun calc(velocity: Int): IntArray {
            stop()
            iScroller.fling(
                    0,
                    0,
                    0,
                    velocity,
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE,
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE)
            val calc = IntArray(2)
            calc[0] = iScroller.finalY
            calc[1] = iScroller.duration
            iScroller.forceFinished(true)
            return calc
        }

        fun stop() {
            removeCallbacks(this)
            iScroller.abortAnimation()
        }
    }
}
