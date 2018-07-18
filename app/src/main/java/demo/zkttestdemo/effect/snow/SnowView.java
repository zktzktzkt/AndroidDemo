package demo.zkttestdemo.effect.snow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zkt on 2017-12-23.
 * https://juejin.im/post/5a32b34c51882506146ef1a0
 */

public class SnowView extends View {

    private Paint testPaint;

    private static final int defaultWidth = 600;//默认宽度
    private static final int defaultHeight = 1000;//默认高度
    private static final int intervalTime = 5;//重绘间隔时间

    private int mWidth;
    private int mHeight;
    private List<SnowObject> snowObjects;

    public SnowView(Context context) {
        this(context, null);
    }

    public SnowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SnowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        testPaint = new Paint();
        testPaint.setColor(Color.WHITE);
        testPaint.setStyle(Paint.Style.FILL);

        snowObjects = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSize(defaultWidth, widthMeasureSpec), resolveSize(defaultHeight, heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (snowObjects.size() > 0) {
            for (int i = 0; i < snowObjects.size(); i++) {
                //然后进行绘制
                snowObjects.get(i).drawObject(canvas);
            }

            getHandler().postDelayed(runnable, intervalTime);
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    /**
     * 向View添加下落物体对象
     *
     * @param snowObject 存放“雪花”的宽高、下落速度
     * @param num
     */
    public void addFallObject(final SnowObject snowObject, final int num) {
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                for (int i = 0; i < num; i++) {
                    SnowObject newFallObject = new SnowObject(snowObject, mWidth, mHeight);
                    snowObjects.add(newFallObject);
                }
                invalidate();
                return true;
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getHandler().removeCallbacks(runnable);
    }
}
