package com.zkt.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by zkt on 2018-9-30.
 * Description:
 */

public class TriangleProgressView extends View {

    private static final int DEFAULT_BACKGROUND_COLOR = Color.GRAY;
    private static final int DEFAULT_PROGRESS_COLOR = Color.BLUE;
    private static final int DEFAULT_TRIANGLE_COLOR = Color.RED;

    private int mWidth;
    private int mHeight;
    private int mProgress = 0;
    private int mProgressHeight = 0;
    private int mTotalProgress;

    private final Paint mTrianglePaint;
    private final Paint mProgressPaint;
    private Paint mBgPaint;
    private Path mTrianglePath;
    private int mSideLenth;
    private int mTriangleHeight;

    public TriangleProgressView(Context context) {
        this(context, null);
    }

    public TriangleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.triangleProgressView);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.triangleProgressView_background_color:
                    array.getColor(attr, DEFAULT_BACKGROUND_COLOR);
                    break;
            }
        }
        array.recycle();

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setDither(true);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(DEFAULT_BACKGROUND_COLOR);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setDither(true);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setColor(DEFAULT_PROGRESS_COLOR);

        mTrianglePaint = new Paint();
        mTrianglePaint.setColor(DEFAULT_TRIANGLE_COLOR);

        mTrianglePath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = dp2px(25);
        setMeasuredDimension(widthMeasureSpec, resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        mProgressHeight = dp2px(5);
        mSideLenth = dp2px(15); //三角形的边长
        double triangleHeight_2 = mSideLenth * mSideLenth - mSideLenth / 2 * mSideLenth / 2;
        mTriangleHeight = (int) Math.sqrt(triangleHeight_2); //三角形的高
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mProgress = mWidth * mProgress / mTotalProgress;
        canvas.drawRoundRect(0, mHeight - mProgressHeight, mWidth, mHeight, 100, 100, mBgPaint);
        canvas.drawRoundRect(0, mHeight - mProgressHeight, mProgress, mHeight, 100, 100, mProgressPaint);
        //画三角形
        mTrianglePath.reset();
        mTrianglePath.moveTo(mProgress, mHeight - mProgressHeight - dp2px(3));
        mTrianglePath.lineTo(mProgress - mSideLenth / 2, mHeight - mProgressHeight - dp2px(3) - mTriangleHeight);
        mTrianglePath.lineTo(mProgress + mSideLenth / 2, mHeight - mProgressHeight - dp2px(3) - mTriangleHeight);
        mTrianglePath.close();
        canvas.drawPath(mTrianglePath, mTrianglePaint);
    }

    public void setProgress(int progress, int totalProgress) {
        this.mTotalProgress = totalProgress;
        this.mProgress = progress;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
