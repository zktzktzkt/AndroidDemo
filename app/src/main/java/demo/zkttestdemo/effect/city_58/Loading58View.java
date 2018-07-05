package demo.zkttestdemo.effect.city_58;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2018-1-22.
 * 58加载数据动画
 */

public class Loading58View extends LinearLayout {
    private ShapeView mShapeView; //形状
    private View mShadowView; //中间的阴影
    private int mTranslationYDistance = 0;
    private final long ANIMATOR_DURATION = 500;

    public Loading58View(Context context) {
        this(context, null);
    }

    public Loading58View(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Loading58View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTranslationYDistance = dip2px(80);
        initLayout(context);
    }

    /**
     * 初始化加载布局
     */
    private void initLayout(Context context) {
        inflate(context, R.layout.ui_loading58, this);
        mShapeView = findViewById(R.id.shape_view);
        mShadowView = findViewById(R.id.shadow_view);
        startfallAnimator();
    }

    /**
     * 开始下落动画
     */
    private void startfallAnimator() {
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mShapeView, "translationY", 0, mTranslationYDistance);
        //配合中间阴影缩小
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView, "scaleX", 1f, 0.3f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATOR_DURATION);
        set.setInterpolator(new AccelerateInterpolator());
        set.playTogether(translationAnimator, scaleAnimator);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startUpAnimator();
                mShapeView.exchange();
            }
        });
    }

    /**
     * 上抛动画
     */
    private void startUpAnimator() {
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mShapeView, "translationY", mTranslationYDistance, 0);
        //配合中间阴影缩小
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView, "scaleX", 0.3f, 1f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATOR_DURATION);
        set.setInterpolator(new DecelerateInterpolator());
        set.playTogether(translationAnimator, scaleAnimator);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                startRotationAnimator();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startfallAnimator();
            }
        });

        set.start();
    }

    /**
     * 上抛的时候旋转
     */
    private void startRotationAnimator() {
        ObjectAnimator animator = null;
        switch (mShapeView.getCurrentShape()) {
            case Circle:
            case Triangle:
                animator = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, -120);
                break;

            case Square:
                animator = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, 180);
                break;
        }
        animator.setDuration(ANIMATOR_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

}
