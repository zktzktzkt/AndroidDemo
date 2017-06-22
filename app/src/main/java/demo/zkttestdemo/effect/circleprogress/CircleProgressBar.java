package demo.zkttestdemo.effect.circleprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2017/6/23.
 */

public class CircleProgressBar extends View {
    private int height;
    private int width;
    private Paint mPaint;
    private int strokeWidth = 5;//线条宽度
    private RectF rectF;
    private int normalColor = Color.parseColor("#A5A5A5");//普通的颜色
    private int progressColor = Color.parseColor("#FA9025");//已经走了的进度条颜色
    private int textColor = normalColor;//文字颜色
    private float textSize = 20;//文字大小
    private int progress = 0;//进度条
    private String centerText = "100%";//中心填充文字
    private Paint fontPaint = null;
    private Paint.Style progress_style = Paint.Style.STROKE;//填充式还是环形式


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
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        if (height > width)  //高大于宽
        {
            rectF = new RectF(strokeWidth, (height / 2 - width / 2) + strokeWidth,
                    width - strokeWidth, (height / 2 - width / 2) - strokeWidth);
        } else if (width > height)  //宽大于高
        {
            rectF = new RectF(strokeWidth, strokeWidth,
                    width - strokeWidth, height - strokeWidth);
        } else //宽等于高
        {
            rectF = new RectF(strokeWidth, strokeWidth,
                    width - strokeWidth, height - strokeWidth);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    Paint.FontMetrics fontMetrics = null;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(normalColor);
        if (progress < 360) {
            canvas.drawArc(rectF, 270 + progress, 360 - progress, progress_style == Paint.Style.FILL, mPaint);
        }

        mPaint.setColor(progressColor);
        canvas.drawArc(rectF, 270, progress, progress_style == Paint.Style.FILL, mPaint);

        fontMetrics = fontPaint.getFontMetrics();
        float textWidth = fontPaint.measureText(centerText);
        float textHeight = fontPaint.ascent() + fontPaint.descent();
        canvas.drawText(centerText, width / 2 - textWidth / 2, height / 2 - textHeight / 2, fontPaint);
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
