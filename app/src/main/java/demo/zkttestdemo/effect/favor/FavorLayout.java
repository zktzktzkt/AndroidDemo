package demo.zkttestdemo.effect.favor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2017-12-25.
 */

public class FavorLayout extends RelativeLayout {

    private Interpolator line = new LinearInterpolator();//线性
    private Interpolator acc = new AccelerateInterpolator();//加速
    private Interpolator dce = new DecelerateInterpolator();//减速
    private Interpolator accdec = new AccelerateDecelerateInterpolator();//先加速后减速
    private Interpolator[] interpolators;

    private int mWidth;
    private int mHeight;
    private LayoutParams lp;
    private Drawable[] drawables;
    Random random = new Random();

    private int drawableWidth;
    private int drawableHeight;

    public FavorLayout(Context context) {
        super(context);
        init();
    }

    public FavorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FavorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化显示的图片
        drawables = new Drawable[3];
        Drawable red = getResources().getDrawable(R.mipmap.ic_heart_red);
        Drawable yellow = getResources().getDrawable(R.mipmap.ic_heart_yellow);
        Drawable blue = getResources().getDrawable(R.mipmap.ic_heart_blue);
        //赋值给drawables
        drawables[0] = red;
        drawables[1] = yellow;
        drawables[2] = blue;

        drawableWidth = red.getIntrinsicWidth();
        drawableHeight = red.getIntrinsicHeight();

        //底部 并且 水平居中
        lp = new LayoutParams(drawableWidth, drawableHeight);
        lp.addRule(CENTER_HORIZONTAL, TRUE);
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE);

        // 初始化插补器
        interpolators = new Interpolator[4];
        interpolators[0] = line;
        interpolators[1] = acc;
        interpolators[2] = dce;
        interpolators[3] = accdec;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    public void addHeart() {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(drawables[random.nextInt(3)]);
        imageView.setLayoutParams(lp);

        addView(imageView);

        Animator set = getAnimator(imageView);
        set.addListener(new AnimEndListener(imageView));
        set.start();
    }

    private Animator getAnimator(View target) {
        ObjectAnimator enterAnimtor = getEnterAnimtor(target);
        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);

        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(enterAnimtor, bezierValueAnimator);
        finalSet.setInterpolator(interpolators[random.nextInt(4)]);

        return finalSet;
    }

    /**
     * 1. 先执行图片出现的动画
     *
     * @param target
     * @return
     */
    private ObjectAnimator getEnterAnimtor(View target) {
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0.2f, 1f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.2f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.2f, 1f);
        ObjectAnimator enter = ObjectAnimator.ofPropertyValuesHolder(target, alpha, scaleX, scaleY);
        enter.setDuration(500);
        enter.setInterpolator(new LinearInterpolator());
        return enter;
    }

    /**
     * 2. 再执行图片位移的动画
     *
     * @param target
     * @return
     */
    private ValueAnimator getBezierValueAnimator(View target) {
        //初始化一个贝塞尔计算器- - 传入
        BezierEvaluator evaluator = new BezierEvaluator(getPointF(1), getPointF(2));

        //这里最好画个图 理解一下 传入了起点 和 终点
        ValueAnimator animator = ValueAnimator.ofObject(
                evaluator,
                new PointF((mWidth - drawableWidth) / 2, mHeight - drawableHeight),
                new PointF(random.nextInt(getWidth()), 0)
        );
        animator.addUpdateListener(new BezierListener(target));
        animator.setDuration(3000);
        return animator;
    }

    /**
     * 获取中间的两个 点
     *
     * @param scale
     */
    private PointF getPointF(int scale) {
        PointF pointF = new PointF();
        pointF.x = random.nextInt(mWidth - 100);
        pointF.y = random.nextInt(mHeight - 100) / scale;

        return pointF;
    }

    private class BezierListener implements ValueAnimator.AnimatorUpdateListener {
        private View target;

        private BezierListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //这里获取到贝塞尔曲线计算出来的x,y值，赋给view，这样就能让爱心随曲线走啦
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            //这里顺便做一个alpha动画
            target.setAlpha(1 - animation.getAnimatedFraction());
        }
    }

    /**
     * 动画结束后remove掉View
     */
    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;

        private AnimEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            removeView(target);
        }
    }

}
