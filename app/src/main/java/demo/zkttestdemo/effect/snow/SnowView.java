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
 */

public class SnowView extends View {

    private Paint testPaint;
    private int snowY;

    private static final int defaultWidth = 600;//默认宽度
    private static final int defaultHeight = 1000;//默认高度
    private static final int intervalTime = 5;//重绘间隔时间

    private int viewWidth;
    private int viewHeight;
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
        snowY = 0;

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
        viewWidth = w;
        viewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (snowObjects.size() > 0) {
            for (int i = 0; i < snowObjects.size(); i++) {
                //然后进行绘制
                snowObjects.get(i).drawObject(canvas);
            }
            // 隔一段时间重绘一次, 动画效果
            getHandler().postDelayed(runnable, intervalTime);
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            snowY += 15;
            if (snowY > viewHeight) {
                snowY = 0;
            }
            invalidate();
        }
    };

    /**
     * 向View添加下落物体对象
     *
     * @param num
     */
    public void addFallObject(final SnowObject snowObject, final int num) {
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                for (int i = 0; i < num; i++) {
                    SnowObject newFallObject = new SnowObject(snowObject, viewWidth, viewHeight);
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
