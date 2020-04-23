package demo.zkttestdemo.effect.circleprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2017/6/23.
 */

public class CircleProgressBar extends View {
    private Paint mPaint;
    private int strokeWidth = 5;//线条宽度
    private RectF rectF;
    private int normalColor = Color.parseColor("#A5A5A5");//普通的颜色
    private int progressColor = Color.parseColor("#FA9025");//已经走了的进度条颜色
    private int textColor = normalColor;//文字颜色
    private float textSize = 20;//文字大小
    private int progress = 0;//进度条
    private String centerText = "0%";//中心填充文字
    private Paint fontPaint = null;
    private Paint.Style progress_style = Paint.Style.STROKE;//填充式还是环形式
    private Matrix matrix;
    private Paint mPointPaint;
    private int mHeight;
    private int mWidth;


    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        textSize = array.getDimension(R.styleable.CircleProgressBar_text_size, textSize);
        textColor = array.getColor(R.styleable.CircleProgressBar_text_color, textColor);
        centerText = array.getString(R.styleable.CircleProgressBar_text) == null ? centerText : array.getString(R.styleable.CircleProgressBar_text);
        strokeWidth = array.getInteger(R.styleable.CircleProgressBar_stroke_width, strokeWidth);
        normalColor = array.getColor(R.styleable.CircleProgressBar_normal_color, normalColor);
        progressColor = array.getColor(R.styleable.CircleProgressBar_progress_color, progressColor);
        progress = array.getInt(R.styleable.CircleProgressBar_progress, progress);
        progress_style = array.getInt(R.styleable.CircleProgressBar_progress_style, 0) == 0 ? Paint.Style.STROKE : Paint.Style.FILL;
        array.recycle();

        initPaint();
    }

    /**
     * 2. 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(normalColor);  //设置画笔颜色
        mPaint.setStyle(progress_style);  //设置模式为描边
        mPaint.setStrokeWidth(strokeWidth);  //设置画笔宽度为10px

        fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fontPaint.setTextSize(textSize);
        fontPaint.setColor(textColor);

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStrokeWidth(10);
        mPointPaint.setColor(Color.BLACK);
        mPointPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSize(400, widthMeasureSpec), resolveSize(400, heightMeasureSpec));
        Log.e("CircleProgressBar", "onMeasure");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("CircleProgressBar", "onSizeChanged");
        mWidth = w;
        mHeight = h;
        if (w > h || h > w) {
            int min = Math.min(w, h);
            rectF = new RectF(strokeWidth, strokeWidth,
                    min - strokeWidth, min - strokeWidth);

        } else {
            rectF = new RectF(strokeWidth, strokeWidth,
                    w - strokeWidth, h - strokeWidth);
        }

        matrix = new Matrix();
        matrix.postRotate(6, mWidth / 2, mHeight / 2);
    }

    Paint.FontMetrics fontMetrics = null;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(normalColor);
        if (progress < 360) {
            canvas.drawArc(rectF, 270 + progress, 360 - progress,
                    progress_style == Paint.Style.FILL, mPaint);
        }

        mPaint.setColor(progressColor);
        canvas.drawArc(rectF, 270, progress, progress_style == Paint.Style.FILL, mPaint);

        fontMetrics = fontPaint.getFontMetrics();
        float textWidth = fontPaint.measureText(centerText);
        float textHeight = fontPaint.ascent() + fontPaint.descent();

        if (getMeasuredHeight() > getMeasuredWidth() || getMeasuredHeight() < getMeasuredWidth()) {
            int min = Math.min(getMeasuredHeight(), getMeasuredWidth());
            canvas.drawText(centerText, min / 2 - textWidth / 2, min / 2 - textHeight / 2, fontPaint);
        } else {
            canvas.drawText(centerText, getMeasuredWidth() / 2 - textWidth / 2, getMeasuredHeight() / 2 - textHeight / 2, fontPaint);
        }

        // 画点
        canvas.save();
        for (int i = 0; i < 60; i++) {
            canvas.concat(matrix);
            canvas.drawPoint(mWidth / 2, 10, mPointPaint);
        }
        canvas.restore();
    }

    /**
     * 更新界面
     */
    public void update(int progress, String text) {
        this.progress = progress;
        this.centerText = text;
        postInvalidate();
    }

}
