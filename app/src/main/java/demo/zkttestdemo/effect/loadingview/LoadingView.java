package demo.zkttestdemo.effect.loadingview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import demo.zkttestdemo.R;

/**
 * 仿雅虎的splash页
 * Created by zkt on 2018-1-20.
 */

public class LoadingView extends View {
    //每个小圆的半径 = 大圆半径的1/8
    private float mCircleRadius;
    //小圆的颜色列表
    private int[] mCircleColors;
    //旋转动画执行的时间
    private final long ROTATION_ANIMATION_TIME = 3000;
    //第二部分动画执行的总时间（包括三个动画的时间）
    private final long SPLASH_ANIMATION_TIME = 10000;
    //整体的颜色背景
    private int mSplashColor = Color.WHITE;
    //当前大圆旋转的角度（弧度）
    private float mCurrentRotationAngle = 0;
    //大圆的半径 = 整体宽度的1/4
    private float mRotationRadius;

    private Paint mPaint;

    //中心点
    private int mCenterX, mCenterY;
    private ValueAnimator animator;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCircleColors = context.getResources().getIntArray(R.array.splash_circle_colors);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
        mRotationRadius = getMeasuredWidth() / 4;
        mCircleRadius = mRotationRadius / 8;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //怎样让圆旋转起来
        drawRotationAnimator(canvas);
    }

    private void drawRotationAnimator(Canvas canvas) {
        //思路：既然是转，那就搞一个变量，不断去改变这个变量值
        //打算采用属性动画，旋转 0~360
        if (null == animator) {
            animator = ValueAnimator.ofFloat(0, 2 * (float) Math.PI);
            animator.setDuration(ROTATION_ANIMATION_TIME);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotationAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            //系统默认的是先加速后减速，所以要重新设置线性
            animator.setInterpolator(new LinearInterpolator());
            animator.setRepeatCount(-1);
            animator.start();
        }

        canvas.drawColor(mSplashColor);

        //画六个圆 每份角度
        double percentAngle = Math.PI * 2 / mCircleColors.length;
        for (int i = 0; i < mCircleColors.length; i++) {
            mPaint.setColor(mCircleColors[i]);
            //当前的角度 = 初始角度 + 旋转角度
            double currentAngle = percentAngle * i + mCurrentRotationAngle;

            int cx = (int) (mCenterX + mRotationRadius * Math.cos(currentAngle));
            int cy = (int) (mCenterY + mRotationRadius * Math.sin(currentAngle));

            canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
        }
    }

    /**
     * 消失
     */
    public void disappear(){
        //开始聚合动画

    }
}
