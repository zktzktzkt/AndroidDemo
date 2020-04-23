package com.zkt.progress.sportprogress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.zkt.progress.R;

/**
 * Created by zkt on 2018-8-9.
 * Description: 运动数据的progress
 */

public class SportProgressView extends View {

    private int mWidth;
    private int mHeight;
    private Paint mCirclePaint;
    private Paint mArcPaint;

    private Paint mTxtPaint;
    private Bitmap icon_sport;
    private Paint mBitmapPaint;
    private int mCurrentStep = 0;
    private int mTargetStep = 0;
    private Paint mPointWhitePaint;
    private Paint mPointGrayPaint;
    private int mCircleColor;
    private int mProgressColor;
    private int mSelectPointColor;
    private int mUnSelectPointColor;
    private Paint mPercentTxtPaint;
    private float mPercentTextSize;
    private int mArcAngle;
    private int mSweepAngle;

    public SportProgressView(Context context) {
        this(context, null);
    }

    public SportProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SportProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SportProgressView);
        mCircleColor = array.getColor(R.styleable.SportProgressView_circleColor, Color.LTGRAY);
        mProgressColor = array.getColor(R.styleable.SportProgressView_progressColor, Color.WHITE);
        mSelectPointColor = array.getColor(R.styleable.SportProgressView_selectPointColor, Color.WHITE);
        mUnSelectPointColor = array.getColor(R.styleable.SportProgressView_unSelectPointColor, Color.LTGRAY);
        mPercentTextSize = array.getDimension(R.styleable.SportProgressView_percentTextSize, dp2px(35));
        array.recycle();

        initPaint();
    }

    private void initPaint() {
        icon_sport = BitmapFactory.decodeResource(getResources(), R.drawable.sport);
        mBitmapPaint = new Paint();

        mPointWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointWhitePaint.setStrokeWidth(10);
        mPointWhitePaint.setColor(mSelectPointColor);
        mPointWhitePaint.setStrokeCap(Paint.Cap.ROUND);

        mPointGrayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointGrayPaint.setStrokeWidth(10);
        mPointGrayPaint.setColor(mUnSelectPointColor);
        mPointGrayPaint.setStrokeCap(Paint.Cap.ROUND);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(dp2px(3));
        mCirclePaint.setColor(mCircleColor);

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(dp2px(3));
        mArcPaint.setColor(mProgressColor);

        mPercentTxtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPercentTxtPaint.setTextAlign(Paint.Align.CENTER);
        mPercentTxtPaint.setColor(Color.WHITE);
        mPercentTxtPaint.setTextSize(mPercentTextSize);

        mTxtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTxtPaint.setTextAlign(Paint.Align.CENTER);
        mTxtPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(dp2px(200), dp2px(200));
        } else {
            int minSize = Math.min(width, height);
            setMeasuredDimension(minSize, minSize);
        }

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mArcAngle > 360) {
            mArcAngle = 360;
        }
        //画点
        canvas.save();
        int pointAngle = 0;
        for (int i = 0; i < 30; i++) {
            if (mSweepAngle >= pointAngle) {
                canvas.drawPoint(mWidth / 2, dp2px(10), mPointWhitePaint);
            } else {
                canvas.drawPoint(mWidth / 2, dp2px(10), mPointGrayPaint);
            }
            canvas.rotate(12, mWidth / 2, mHeight / 2);
            pointAngle += 12;
        }
        canvas.restore();

        //画底层的圆
        int radius = mWidth / 2 - dp2px(22);
        canvas.drawCircle(mWidth / 2, mHeight / 2, radius, mCirclePaint);
        //画进度弧形
        canvas.drawArc(dp2px(22), dp2px(22), mWidth - dp2px(22), mWidth - dp2px(22),
                270, mSweepAngle, false, mArcPaint);
        //百分比
        int percent = (int) ((float) mCurrentStep / mTargetStep * 100);
        if (percent > 100) {
            percent = 100;
        }
        canvas.drawText(percent + "%", mWidth / 2, mHeight / 2 - dp2px(5), mPercentTxtPaint);
        //每日目标
        mTxtPaint.setTextSize(dp2px(12));
        canvas.drawText("每日目标", mWidth / 2, mHeight / 2 + dp2px(30), mTxtPaint);
        canvas.drawText(String.valueOf(mTargetStep), mWidth / 2, mHeight / 2 + dp2px(48), mTxtPaint);
        //画图片
        canvas.drawBitmap(icon_sport, mWidth / 2 - icon_sport.getWidth() / 2, dp2px(22) - icon_sport.getHeight() / 2, mBitmapPaint);
    }

    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    /**
     * 设置当前步数和总步数
     *
     * @param currentStep 当前步数
     * @param targetStep  目标步数
     */
    public void setCurrentAndTarget(@NonNull int currentStep, @NonNull int targetStep, boolean animate) {
        this.mCurrentStep = currentStep;
        this.mTargetStep = targetStep;

        mArcAngle = (int) ((float) mCurrentStep / mTargetStep * 360);

        invalidate();

        if (animate) {
            ValueAnimator animator = ValueAnimator.ofInt(0, mArcAngle).setDuration(2000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatedValue = (int) animation.getAnimatedValue();
                    mSweepAngle = animatedValue;
                    invalidate();
                }
            });
            animator.start();
        } else {
            mSweepAngle = mArcAngle;
        }
    }

}
