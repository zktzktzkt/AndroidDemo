package demo.zkttestdemo.effect.countdownView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by zkt on 19/04/09.
 * Description:
 */
public class CountdownView extends View {

    // 时间改变监听
    private OnCountdownListener onCountdownListener;
    // 控件宽
    private int width;
    // 控件高
    private int height;
    // 刻度盘半径
    private int dialRadius;
    // 时间-分
    private int time = 20;
    // 当前旋转的角度
    private float rotateAngle;
    // 小时刻度高
    private float hourScaleHeight = dp2px(6);
    // 分钟刻度高
    private float minuteScaleHeight = dp2px(4);
    // 定时进度条宽
    private float arcWidth = dp2px(6);

    private Paint dialPaint;
    private Paint timePaint;

    public CountdownView(Context context) {
        this(context, null);
    }

    public CountdownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountdownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //刻度盘画笔
        dialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dialPaint.setColor(Color.parseColor("#94C5FF"));
        dialPaint.setStyle(Paint.Style.STROKE);
        dialPaint.setStrokeCap(Paint.Cap.ROUND);

        //时间画笔
        timePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        timePaint.setColor(Color.parseColor("#94C5FF"));
        timePaint.setTextSize(sp2px(33));
        timePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = height = Math.min(h, w);
        // 刻度盘半径
        dialRadius = width / 2 - dp2px(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 将坐标原点移到控件中心
        canvas.translate(getWidth() / 2, getHeight() / 2);

        //绘制刻度盘
        drawDial(canvas);
        //绘制定时进度条
        drawArc(canvas);

        //绘制时间
        //drawTime(canvas);
    }

    /**
     * 绘制定时进度条
     */
    private void drawArc(Canvas canvas) {
        if (time > 0) {
            canvas.save();
            //绘制起始标志
            dialPaint.setStrokeWidth(dp2px(3));
            canvas.drawLine(0, -dialRadius - hourScaleHeight, 0, -dialRadius + hourScaleHeight, dialPaint);

            //取消圆角
            dialPaint.setStrokeCap(Paint.Cap.BUTT);
            //绘制进度
            for (int i = 0; i <= time * 6; i++) {
                canvas.drawLine(0, -dialRadius - arcWidth / 2, 0, -dialRadius + arcWidth / 2, dialPaint);
                // 最后一次绘制后不旋转画布
                if (i != time * 6) {
                    canvas.rotate(1);
                }
            }

            //绘制结束标志
            dialPaint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawLine(0, -dialRadius - hourScaleHeight, 0, -dialRadius + hourScaleHeight, dialPaint);

            canvas.restore();
        }
    }

    /**
     * 刻度盘
     */
    private void drawDial(Canvas canvas) {
        // 绘制外层圆盘
        dialPaint.setStrokeWidth(dp2px(4));
        canvas.drawCircle(0, 0, dialRadius, dialPaint);

        canvas.save();
        //绘制小时刻度
        for (int i = 0; i < 12; i++) {
            //定时时间为0时，绘制正常小时刻度
            //小时刻度没有被定时进度条覆盖时，正常绘制小时刻度
            if (time == 0 || i > time / 5) {
                canvas.drawLine(0, -dialRadius, 0, -dialRadius + hourScaleHeight, dialPaint);
            }
            // 360/12=30
            canvas.rotate(30);
        }
        canvas.restore();

        canvas.save();
        //绘制分钟刻度
        dialPaint.setStrokeWidth(dp2px(2));
        for (int i = 0; i < 60; i++) {
            //小时刻度位置不绘制分钟刻度
            // 分钟刻度没有被定时进度条覆盖时，正常绘制分钟刻度
            if (i % 5 != 0 && i > time) {
                canvas.drawLine(0, -dialRadius, 0, -dialRadius + minuteScaleHeight, dialPaint);
            }
            // 360 / 60 = 6;
            canvas.rotate(6);
        }
        canvas.restore();


    }

    /**
     * 设置倒计时
     *
     * @param minute 分钟
     */
    public void setCountdown(int minute) {
        if (minute < 0 || minute > 60) {
            return;
        }
        time = minute;
        rotateAngle = minute * 6;
        invalidate();
    }

    /**
     * 设置倒计时监听
     *
     * @param onCountdownListener 倒计时监听接口
     */
    public void setOnCountdownListener(OnCountdownListener onCountdownListener) {
        this.onCountdownListener = onCountdownListener;
    }

    /**
     * 倒计时监听接口
     */
    public interface OnCountdownListener {
        /**
         * 倒计时
         *
         * @param time 时间
         */
        void countdown(int time);
    }

    /**
     * dp转px
     *
     * @param dp dp值
     * @return px值
     */
    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param sp sp值
     * @return px值
     */
    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }
}
