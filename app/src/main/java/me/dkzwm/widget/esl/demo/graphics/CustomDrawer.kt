package me.dkzwm.widget.esl.demo.graphics

import android.content.Context
import android.graphics.*
import android.util.TypedValue
import me.dkzwm.widget.esl.graphics.Drawer

class CustomDrawer(context: Context) : Drawer(context) {
    private val mBackgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mCyclePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBackgroundPath = Path()
    private val mBackgroundFixedSize: Float
    override val maxSize: Int
    private val mCycleMaxRadius: Int

    init {
        mBackgroundPaint.color = Color.GREEN
        mBackgroundPaint.strokeWidth = 1f
        mBackgroundPaint.style = Paint.Style.FILL
        mCycleMaxRadius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4f,
                context.resources.displayMetrics).toInt()
        mCyclePaint.style = Paint.Style.FILL
        mCyclePaint.color = Color.WHITE
        mCyclePaint.strokeWidth = 1f
        mBackgroundFixedSize = context.resources.displayMetrics.heightPixels / 3.5f
        maxSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                26f,
                context.resources.displayMetrics).toInt()
    }

    private fun calcRadius(size: Int): Float {
        return Math.min(
                size.toFloat() / maxSize * mCycleMaxRadius, mCycleMaxRadius.toFloat())
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
        val radius = calcRadius(size)
        canvas.drawCircle(size / 2f, downPoint.y, radius, mCyclePaint)
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
        val radius = calcRadius(size)
        canvas.drawCircle(downPoint.x, size / 2f, radius, mCyclePaint)
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
        val x = mWidth - size
        val radius = calcRadius(size)
        canvas.drawCircle(x + size / 2f, downPoint.y, radius, mCyclePaint)
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
        val y = mHeight - size
        val radius = calcRadius(size)
        canvas.drawCircle(downPoint.x, y + size / 2f, radius, mCyclePaint)
    }

    override fun canTrigger(edge: Int, pos: Float): Boolean {
        return pos >= maxSize / 3 * 2
    }
}
