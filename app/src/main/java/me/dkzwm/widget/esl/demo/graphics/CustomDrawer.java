package me.dkzwm.widget.esl.demo.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.TypedValue;
import me.dkzwm.widget.esl.graphics.Drawer;

public class CustomDrawer extends Drawer {
    private Paint mBackgroundPaint;
    private Paint mCyclePaint;
    private Path mBackgroundPath = new Path();
    private float mBackgroundFixedSize;
    private int mBackgroundMaxDynamicSize;
    private int mCycleMaxRadius;

    public CustomDrawer(Context context) {
        super(context);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.GREEN);
        mBackgroundPaint.setStrokeWidth(1);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mCycleMaxRadius =
                (int)
                        TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                4,
                                mContext.getResources().getDisplayMetrics());
        mCyclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCyclePaint.setStyle(Paint.Style.FILL);
        mCyclePaint.setColor(Color.WHITE);
        mCyclePaint.setStrokeWidth(1);
        mBackgroundFixedSize = context.getResources().getDisplayMetrics().heightPixels / 3.5f;
        mBackgroundMaxDynamicSize =
                (int)
                        TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                26,
                                mContext.getResources().getDisplayMetrics());
    }

    private float calcRadius(int size) {
        return Math.min(
                (float) size / mBackgroundMaxDynamicSize * mCycleMaxRadius, mCycleMaxRadius);
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
        float radius = calcRadius(size);
        canvas.drawCircle(size / 2f, downPoint.y, radius, mCyclePaint);
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
        float radius = calcRadius(size);
        canvas.drawCircle(downPoint.x, size / 2f, radius, mCyclePaint);
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
        final int x = mWidth - size;
        float radius = calcRadius(size);
        canvas.drawCircle(x + size / 2f, downPoint.y, radius, mCyclePaint);
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
        final int y = mHeight - size;
        float radius = calcRadius(size);
        canvas.drawCircle(downPoint.x, y + size / 2f, radius, mCyclePaint);
    }

    @Override
    public boolean canTrigger(int edge, float x) {
        return x >= getMaxSize() / 3 * 2;
    }
}
