package demo.zkttestdemo.effect.loadingview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
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
    private final long ROTATION_ANIMATION_TIME = 2000;
    //第二部分动画执行的总时间（包括三个动画的时间）
    private final long SPLASH_ANIMATION_TIME = 2000;
    //整体的颜色背景
    private int mSplashColor = Color.WHITE;
    //当前大圆旋转的角度（弧度）
    private float mCurrentRotationAngle = 0;
    //大圆的半径 = 整体宽度的1/4
    private float mRotationRadius;
    //当前的大圆半径
    private float mCurrRotationRadius;

    private Paint mPaint;

    //中心点
    private int mCenterX, mCenterY;
    //代表当前状态所画动画
    private LoadingState mLoadingState;

    //空心圆的半径
    private float mHoleRadius = 0F;
    //屏幕对角线的一半
    private float mDiagonalDist;


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

        mDiagonalDist = (float) Math.sqrt(mCenterX * mCenterX + mCenterY * mCenterY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLoadingState == null) {
            mLoadingState = new RotationState();
        }

        mLoadingState.draw(canvas);
    }

    /**
     * 消失
     */
    public void disappear() {
        //关闭旋转动画
        if (mLoadingState instanceof RotationState) {
            RotationState rotationState = (RotationState) mLoadingState;
            rotationState.cancel();
        }
        //开始聚合动画
        mLoadingState = new MergeState();
    }

    public abstract class LoadingState {
        public abstract void draw(Canvas canvas);
    }

    /**
     * 旋转动画
     */
    public class RotationState extends LoadingState {
        private ValueAnimator animator;

        public RotationState() {
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

        @Override
        public void draw(Canvas canvas) {
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

         // 取消动画
        private void cancel() {
            animator.cancel();
        }
    }

    /**
     * 聚合动画
     */
    public class MergeState extends LoadingState {
        private ValueAnimator animator;

        public MergeState() {
            animator = ObjectAnimator.ofFloat(mRotationRadius, 0);
            animator.setDuration(ROTATION_ANIMATION_TIME / 2);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrRotationRadius = (float) animation.getAnimatedValue(); //最大半径 -> 0
                    invalidate();
                }
            });
            //插值器，先向后甩，再向前甩
            animator.setInterpolator(new AnticipateInterpolator(3f));

            //等聚合完毕后展开
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoadingState = new ExpandState();
                }
            });
            animator.start();
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawColor(mSplashColor);

            //画六个圆 每份角度
            double percentAngle = Math.PI * 2 / mCircleColors.length;
            for (int i = 0; i < mCircleColors.length; i++) {
                mPaint.setColor(mCircleColors[i]);
                //当前的角度 = 初始角度 + 旋转角度
                double currentAngle = percentAngle * i + mCurrentRotationAngle;

                int cx = (int) (mCenterX + mCurrRotationRadius * Math.cos(currentAngle));
                int cy = (int) (mCenterY + mCurrRotationRadius * Math.sin(currentAngle));

                canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
            }
        }
    }

    /**
     * 展开动画
     */
    public class ExpandState extends LoadingState {
        ValueAnimator animator;

        public ExpandState() {
            animator = ObjectAnimator.ofFloat(0, mDiagonalDist);
            animator.setDuration(SPLASH_ANIMATION_TIME);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mHoleRadius = (float) animator.getAnimatedValue(); // 0 -》 对角线的一半
                    invalidate();
                }
            });
            animator.start();
        }

        @Override
        public void draw(Canvas canvas) {
            float strokeWidth = mDiagonalDist - mHoleRadius;
            mPaint.setStrokeWidth(strokeWidth);
            mPaint.setColor(mSplashColor);
            mPaint.setStyle(Paint.Style.STROKE);

            float radius = strokeWidth / 2 + mHoleRadius;
            //绘制一个圆
            canvas.drawCircle(mCenterX, mCenterY, radius, mPaint);
        }
    }
}
