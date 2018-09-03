package demo.zkttestdemo.effect.loadingdialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * Created by zkt on 2018-9-3.
 * Description:
 */

public class LoadingHsView extends RelativeLayout {
    private CircleView mLeftView, mMiddleView, mRightView;
    private int mTranslationDistance = 20;
    private final long ANIMATION_TIME = 350;
    private boolean mIsStopAnimator = false;

    public LoadingHsView(Context context) {
        this(context, null);
    }

    public LoadingHsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingHsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTranslationDistance = dp2px(mTranslationDistance);
        setBackgroundColor(Color.WHITE);

        //左
        mLeftView = getCircleView(context);
        mLeftView.exchangeColor(Color.BLUE);
        //中
        mMiddleView = getCircleView(context);
        mMiddleView.exchangeColor(Color.RED);
        //右
        mRightView = getCircleView(context);
        mRightView.exchangeColor(Color.GREEN);

        addView(mLeftView);
        addView(mRightView);
        addView(mMiddleView);

        post(new Runnable() {
            @Override
            public void run() {
                //等布局实例化完成后，再开启动画
                expandAnimation();
            }
        });
    }

    /**
     * 开启动画
     */
    private void expandAnimation() {
        if (mIsStopAnimator) {
            return;
        }
        //左边跑
        ObjectAnimator leftTranslationAnimator = ObjectAnimator.ofFloat(mLeftView, "translationX", 0, -mTranslationDistance);
        ObjectAnimator rightTranslationAnimator = ObjectAnimator.ofFloat(mRightView, "translationX", 0, mTranslationDistance);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(leftTranslationAnimator, rightTranslationAnimator);
        animatorSet.setDuration(ANIMATION_TIME);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //往里面跑
                innerAnimation();
            }
        });
        animatorSet.start();
    }

    private void innerAnimation() {
        if (mIsStopAnimator) {
            return;
        }
        ObjectAnimator leftTranslationAnimator = ObjectAnimator.ofFloat(mLeftView, "translationX", -mTranslationDistance, 0);
        ObjectAnimator rightTranslationAnimator = ObjectAnimator.ofFloat(mRightView, "translationX", mTranslationDistance, 0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(leftTranslationAnimator, rightTranslationAnimator);
        animatorSet.setDuration(ANIMATION_TIME);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //往外面跑
                expandAnimation();
                //切换颜色 左边的给中间，中间的给右边，右边的给左边
                int leftColor = mLeftView.getcolor();
                int rightColor = mRightView.getcolor();
                int middleColor = mMiddleView.getcolor();
                mMiddleView.exchangeColor(leftColor);
                mRightView.exchangeColor(middleColor);
                mLeftView.exchangeColor(rightColor);
            }
        });
        animatorSet.start();
    }


    public CircleView getCircleView(Context context) {
        CircleView circleView = new CircleView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dp2px(10), dp2px(10));
        params.addRule(CENTER_IN_PARENT);
        circleView.setLayoutParams(params);

        return circleView;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(View.INVISIBLE);// 不要再去排放和计算，少走一些系统的源码（View的绘制流程）
        // 清理动画
        mMiddleView.clearAnimation();
        mLeftView.clearAnimation();
        mRightView.clearAnimation();
        // 把LoadingView从父布局移除
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            removeAllViews();// 移除自己所有的View
            parent.removeView(this);// 从父布局移除
        }
        mIsStopAnimator = true;
    }
}
