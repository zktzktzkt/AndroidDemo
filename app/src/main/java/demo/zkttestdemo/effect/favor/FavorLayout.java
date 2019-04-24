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
    private Interpolator[] interpolators;

    private int mWidth;
    private int mHeight;
    private LayoutParams lp;
    private Drawable[] drawables;
    Random random = new Random();

    private int drawableWidth;
    private int drawableHeight;

    public FavorLayout(Context context) {
        this(context, null);
    }

    public FavorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FavorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        /*
         * 初始化显示的图片
         */
        Drawable red = getResources().getDrawable(R.mipmap.ic_heart_red);
        Drawable yellow = getResources().getDrawable(R.mipmap.ic_heart_yellow);
        Drawable blue = getResources().getDrawable(R.mipmap.ic_heart_blue);
        drawables = new Drawable[3];
        drawables[0] = red;
        drawables[1] = yellow;
        drawables[2] = blue;

        /*
         * 初始化插值器
         */
        Interpolator line = new LinearInterpolator();//线性
        Interpolator acc = new AccelerateInterpolator();//加速
        Interpolator dce = new DecelerateInterpolator();//减速
        Interpolator accdec = new AccelerateDecelerateInterpolator();//先加速后减速
        interpolators = new Interpolator[4];
        interpolators[0] = line;
        interpolators[1] = acc;
        interpolators[2] = dce;
        interpolators[3] = accdec;

        drawableWidth = red.getIntrinsicWidth();
        drawableHeight = red.getIntrinsicHeight();
        //底部 并且 水平居中
        lp = new LayoutParams(drawableWidth, drawableHeight);
        lp.addRule(CENTER_HORIZONTAL, TRUE);
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE);
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
        //1.先添加进layout中
        addView(imageView);
        //2.执行动画
        startAnimator(imageView);
    }

    //属性动画 是一个值到另一个值的改变
    private void startAnimator(View target) {
        //1.先设置缩放动画
        ObjectAnimator enterAnimtor = getEnterAnimtor(target);
        //2.再设置路径
        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);
        //3.依次执行上面两个动画，结束后销毁View
        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(enterAnimtor, bezierValueAnimator);//有先后顺序
        finalSet.setInterpolator(interpolators[random.nextInt(4)]);
        finalSet.addListener(new AnimEndListener(target));
        finalSet.start();
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
        //初始化一个贝塞尔估值器
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
