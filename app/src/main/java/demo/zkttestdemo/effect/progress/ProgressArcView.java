package demo.zkttestdemo.effect.progress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2017/6/10.
 */

public class ProgressArcView extends View {
    /**
     * 圆弧的宽度
     */
    private float borderWidth = dipToPx(14);

    /**
     * 画步数的数值的字体大小
     */
    private float numberTextSize = 0;

    /**
     * 步数
     */
    private String stepNumber = "0";

    /**
     * 开始绘制圆弧的角度
     */
    private int startAngle = 135;

    /**
     * 终点对应的角度和起始点对应的角度的夹角
     */
    private int totalAngle = 270;

    /**
     * 所要绘制的当前步数的红色圆弧终点到起点的夹角
     */
    private int endAngle = 135;

    /**
     * 动画时长
     */
    private int animationLength = 3000;
    private Paint redArcPaint;
    private Paint yellowArcPaint;


    public ProgressArcView(Context context) {
        this(context, null);
    }

    public ProgressArcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //红画笔
        redArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redArcPaint.setColor(ContextCompat.getColor(getContext(), R.color.red));
        redArcPaint.setStrokeCap(Paint.Cap.ROUND);
        redArcPaint.setStyle(Paint.Style.STROKE);
        redArcPaint.setStrokeWidth(borderWidth);
        redArcPaint.setStrokeJoin(Paint.Join.ROUND);
        //黄画笔
        yellowArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yellowArcPaint.setColor(ContextCompat.getColor(getContext(), R.color.yellow));
        yellowArcPaint.setStrokeCap(Paint.Cap.ROUND);
        yellowArcPaint.setStrokeJoin(Paint.Join.ROUND);
        yellowArcPaint.setStyle(Paint.Style.STROKE);
        yellowArcPaint.setStrokeWidth(borderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //中心点的X坐标
        float centerX = getWidth() / 2;
        //指定圆弧的外轮廓矩形区域
        RectF rectF = new RectF(borderWidth, borderWidth, getWidth() - borderWidth, getHeight() - borderWidth);

        //1.绘制整体的黄色圆弧
        drawArcYellow(canvas, rectF);
        //2.绘制当前进度的红色圆弧
        drawArcRed(canvas, rectF);
        //3.圆环中心的步数
        drawTextNumber(canvas, centerX);
        //4.绘制“步数”的红色数字
        drawTextStepString(canvas, centerX);
    }

    /**
     * 1.绘制整体的黄色圆弧
     */
    private void drawArcYellow(Canvas canvas, RectF rectF) {
        /*
         * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint yellowArcPaint)//画弧，
         参数一：RectF对象，一个矩形区域椭圆形的界限用于定义在形状、大小、电弧，
         参数二：起始角(度)在电弧的开始，圆弧起始角度，单位为度。
         参数三：圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度。
         参数四：如果这是true(真)的话,在绘制圆弧时将圆心包括在内，通常用来绘制扇形；如果它是false(假)这将是一个弧线,
         参数五：是Paint对象；
         */
        canvas.drawArc(rectF, startAngle, totalAngle, false, yellowArcPaint);
    }

    /**
     * 2.绘制当前进度的红色圆弧
     */
    private void drawArcRed(Canvas canvas, RectF rectF) {
        canvas.drawArc(rectF, startAngle, endAngle, false, redArcPaint);
    }

    /**
     * 3. 圆环中心的数字
     */
    private void drawTextNumber(Canvas canvas, float centerX) {
        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setTextSize(numberTextSize);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        paintText.setTypeface(font);  //字体风格
        paintText.setColor(ContextCompat.getColor(getContext(), R.color.red));
        Rect bounds_Number = new Rect();
        paintText.getTextBounds(stepNumber, 0, stepNumber.length(), bounds_Number);

        canvas.drawText(stepNumber, centerX, getHeight() / 2 + bounds_Number.height() / 2, paintText);
    }

    /**
     * 4.圆环中心【步数】文字
     */
    private void drawTextStepString(Canvas canvas, float centerX) {
        Paint yellowArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yellowArcPaint.setTextSize(dipToPx(16));
        yellowArcPaint.setTextAlign(Paint.Align.CENTER);
        yellowArcPaint.setColor(ContextCompat.getColor(getContext(), R.color.grey));
        String stepString = "步数";
        Rect rect = new Rect();
        yellowArcPaint.getTextBounds(stepString, 0, stepString.length(), rect);

        canvas.drawText(stepString, centerX, getHeight() / 2 + rect.height() + getFontHeight(numberTextSize), yellowArcPaint);
    }

    public void setCurrentCount(int totalStep, int currentStep) {
        //如果当前走的步数超过总步数则圆弧还是270度，不能成为圆
        if (currentStep > totalStep) {
            currentStep = totalStep;
        }

        //开始执行动画
        ValueAnimator arcAnimator = setAnimation(currentStep, totalStep);
        arcAnimator.setDuration(animationLength);
        arcAnimator.start();

        setTextSize(currentStep);
    }

    private ValueAnimator setAnimation(final int currentStep, final int totalStep) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, currentStep);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currStep = (int) animation.getAnimatedValue();
                //步数
                stepNumber = String.valueOf(currStep);
                //划过的角度
                endAngle = (int) (currStep * totalAngle    / (float) totalStep);
                invalidate();
            }
        });

        return valueAnimator;
    }

    /**
     * 获取当前步数的数字的高度
     *
     * @param fontSize 字体大小
     * @return 字体高度
     */
    public int getFontHeight(float fontSize) {
        Paint yellowArcPaint = new Paint();
        yellowArcPaint.setTextSize(fontSize);
        Rect bounds_Number = new Rect();
        yellowArcPaint.getTextBounds(stepNumber, 0, stepNumber.length(), bounds_Number);
        return bounds_Number.height();
    }

    /**
     * dip 转换成px
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 设置文本大小,防止步数特别大之后放不下，将字体大小动态设置
     *
     * @param num
     */
    public void setTextSize(int num) {
        String s = String.valueOf(num);
        int length = s.length();
        if (length <= 4) {
            numberTextSize = dipToPx(50);
        } else if (length > 4 && length <= 6) {
            numberTextSize = dipToPx(40);
        } else if (length > 6 && length <= 8) {
            numberTextSize = dipToPx(30);
        } else if (length > 8) {
            numberTextSize = dipToPx(25);
        }
    }


}
