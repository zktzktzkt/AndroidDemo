package com.zkt.progress.triangleprogress;

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

import com.zkt.progress.R;

/**
 * Created by zkt on 2018-9-30.
 * Description:
 */

public class TriangleProgressView extends View {

    private static final int DEFAULT_BACKGROUND_COLOR = Color.GRAY;
    private static final int DEFAULT_PROGRESS_COLOR = Color.BLUE;
    private static final int DEFAULT_TRIANGLE_COLOR = Color.RED;
    private static final int DEFAULT_NUMBER_COLOR = Color.BLACK;

    private int mWidth;
    private int mHeight;
    private int mProgress = 0;
    private int mProgressHeight = 0;
    private int mTotalProgress = 100;

    private final Paint mTrianglePaint;
    private final Paint mProgressPaint;
    private Paint mBgPaint;
    private Path mTrianglePath;
    private int mTriangleHeight;
    private float mNumberSize;
    private Paint mNumberPaint;
    private int mProgressColor;
    private int mBackgroundColor;
    private int mTriangleColor;
    private int mNumberColor;
    private int mTriangleSideLength;
    private int mGap;
    private int mProgressLenth;

    public TriangleProgressView(Context context) {
        this(context, null);
    }

    public TriangleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.triangleProgressView);
        mBackgroundColor = array.getColor(R.styleable.triangleProgressView_background_color, DEFAULT_BACKGROUND_COLOR);
        mProgressColor = array.getColor(R.styleable.triangleProgressView_progress_color, DEFAULT_PROGRESS_COLOR);
        mTriangleColor = array.getColor(R.styleable.triangleProgressView_triangle_color, DEFAULT_TRIANGLE_COLOR);
        mTriangleSideLength = (int) array.getDimension(R.styleable.triangleProgressView_triangle_side_length, 15);
        mNumberSize = array.getDimension(R.styleable.triangleProgressView_number_size, 18);
        mNumberColor = array.getColor(R.styleable.triangleProgressView_number_color, DEFAULT_NUMBER_COLOR);
        mGap = (int) array.getDimension(R.styleable.triangleProgressView_gap, 5);
        array.recycle();

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setDither(true);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(mBackgroundColor);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setDither(true);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setColor(mProgressColor);

        mTrianglePaint = new Paint();
        mTrianglePaint.setColor(mTriangleColor);

        mNumberPaint = new Paint();
        mNumberPaint.setColor(mNumberColor);
        mNumberPaint.setTextSize(mNumberSize);
        mNumberPaint.setTextAlign(Paint.Align.CENTER);

        mTrianglePath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mProgressHeight = dp2px(5);
        double triangleHeight_2 = mTriangleSideLength * mTriangleSideLength - mTriangleSideLength / 2 * mTriangleSideLength / 2;
        mTriangleHeight = (int) Math.sqrt(triangleHeight_2); //三角形的高

        int height = (int) (mProgressHeight + mTriangleHeight + mNumberSize + mGap * 2);

        setMeasuredDimension(widthMeasureSpec, resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //测量文字的宽度
        int numberWidth = (int) mNumberPaint.measureText("12345");
        mProgressLenth = (mWidth - numberWidth) * mProgress / mTotalProgress;

        //进度条
        //1.背景
        canvas.drawRoundRect(
                numberWidth / 2,
                mHeight - mProgressHeight,
                mWidth - numberWidth / 2,
                mHeight,
                100, 100, mBgPaint
        );
        //2.进度
        canvas.drawRoundRect(
                numberWidth / 2,
                mHeight - mProgressHeight,
                mProgressLenth + numberWidth / 2,
                mHeight,
                100, 100, mProgressPaint
        );

        //画三角形
        mTrianglePath.reset();
        int x = mProgressLenth + numberWidth / 2;
        int y = mHeight - mProgressHeight - mGap;
        mTrianglePath.moveTo(x, y);
        int x1 = mProgressLenth - mTriangleSideLength / 2 + numberWidth / 2;
        int y1 = mHeight - mProgressHeight - mGap - mTriangleHeight;
        mTrianglePath.lineTo(x1, y1);
        int x2 = mProgressLenth + mTriangleSideLength / 2 + numberWidth / 2;
        int y2 = mHeight - mProgressHeight - mGap - mTriangleHeight;
        mTrianglePath.lineTo(x2, y2);
        mTrianglePath.close();
        canvas.drawPath(mTrianglePath, mTrianglePaint);

        //画文字
        canvas.drawText(mProgress + "",
                mProgressLenth + numberWidth / 2,
                mHeight - mProgressHeight - mGap - mTriangleHeight - mGap,
                mNumberPaint);

    }

    public void setProgress(int progress, int totalProgress) {
        this.mTotalProgress = totalProgress;
        this.mProgress = progress;

        invalidate();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
