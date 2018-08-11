package demo.zkttestdemo.effect.circleprogress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2017-12-22.
 */

public class CountdownProgress extends View {

    private Paint circlePaint;
    private Paint strokePaint;
    private int circleColor;
    private int strokeColor;

    private int strokeSize = 20;
    private int width = 500;
    private int progress = 0;
    private ValueAnimator animator;
    private Paint paint;

    public CountdownProgress(Context context) {
        this(context, null);
    }

    public CountdownProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountdownProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.countdownProgress);
        circleColor = array.getColor(R.styleable.countdownProgress_circle_color, Color.BLACK);
        strokeColor = array.getColor(R.styleable.countdownProgress_stroke_color, Color.GRAY);
        array.recycle();

        initPaint();
    }

    private void initPaint() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.FILL);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(strokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeSize);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(width, widthMeasureSpec));
    }

    Rect rect = new Rect(0, 0, 200, 200);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(width / 2, width / 2, width / 2 - strokeSize * 1.5f, circlePaint);
        canvas.drawArc(strokeSize, strokeSize, width - strokeSize, width - strokeSize,
                -90, progress, false, strokePaint);

        canvas.translate(width / 2, width / 2);
        canvas.rotate(45);
        canvas.drawRect(rect, paint);
    }

    public void startCountdown(long duration) {
        animator = ValueAnimator.ofInt(0, 360).setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                Log.e("progress", animatedValue + "");
                progress = animatedValue;
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != animator) {
            animator.cancel();
        }
    }
}
