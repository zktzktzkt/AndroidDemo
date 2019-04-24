package demo.zkttestdemo.effect.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zkt on 19/04/24.
 * Description:
 */
public class ChartView extends View {

    private Paint bgLinePaint;
    PathEffect pathEffect = new DashPathEffect(new float[]{3, 10}, 0);
    Path bgDashPath = new Path();
    private Paint textPaint;
    private int yTextWidth = SizeUtils.dp2px(80);
    List<PointF> pointList = new ArrayList<>(8);

    String[] xArr = {"12:00", "01/09", "01/10", "01/11", "01/12", "01/13", "01/14"};
    String[] yArr = {"0.60", "0.50", "0.40", "0.30", "0.20", "0.10"};

    String[] pointArr = {"0.60", "0.50", "0.40", "0.30", "0.20", "0.10", "0.40"};

    private final Paint pointPaint;

    {
        for (int i = 0; i < pointArr.length; i++) {
            pointList.add(new PointF());
        }

        //背景线
        bgLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgLinePaint.setColor(Color.parseColor("#FF2C93EC"));
        bgLinePaint.setStrokeWidth(2);
        bgLinePaint.setStyle(Paint.Style.STROKE);

        //文字
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#FF989898"));
        textPaint.setTextSize(SizeUtils.sp2px(10));
        textPaint.setTextAlign(Paint.Align.CENTER);

        //点
        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setColor(Color.parseColor("#FF2C93EC"));
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setStrokeWidth(SizeUtils.dp2px(2));
    }

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //        setMeasuredDimension(resolveSize(), resolveSize());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画背景线
        drawBgLine(canvas);
        //画X轴
        drawX(canvas);
        //画Y轴
        drawY(canvas);
        //画点
        drawPoint(canvas);
    }

    private void drawPoint(Canvas canvas) {
        double itemHeight = (double) getHeight() / 6;
        //先算出最小的点对应的y
        //        Double.parseDouble(pointArr[0]) / itemHeight = Double.parseDouble(pointArr[i]) / y;
        //        y = Double.parseDouble(pointArr[i]) / Double.parseDouble(pointArr[0]) / itemHeight;
        int itemWidth = (getWidth() - yTextWidth) / 7;
        //FIXME y计算的还是有问题
        for (int i = 0; i < pointArr.length; i++) {
            double y = getHeight() - Double.parseDouble(pointArr[i]) / (Double.parseDouble(pointArr[0]) / itemHeight);
            pointList.get(i).y = (float) y;
        }
        for (int i = 0; i < pointArr.length; i++) {
            float x = (i + 1) * itemWidth - itemWidth / 2;
            pointList.get(i).x = x;
        }

        for (int i = 0; i < pointList.size(); i++) {
            canvas.drawCircle(pointList.get(i).x,
                    (float) (getHeight() - pointList.get(i).y + itemHeight / 2),
                    10, pointPaint);
        }

    }

    private void drawY(Canvas canvas) {
        int itemHeight = getHeight() / 6;
        int x = getWidth() - yTextWidth / 2;
        for (int i = 0; i < yArr.length; i++) {
            canvas.drawText(yArr[i], x, (i + 1) * itemHeight - itemHeight / 2, textPaint);
        }
    }

    private void drawX(Canvas canvas) {
        int itemX = (getWidth() - yTextWidth) / 7;
        for (int i = 0; i < xArr.length; i++) {
            canvas.drawText(xArr[i], (i + 1) * itemX - itemX / 2, getHeight() - SizeUtils.dp2px(5), textPaint);
        }
    }

    private void drawBgLine(Canvas canvas) {
        //顶部和底部
        canvas.drawLine(0, 0, getWidth(), 2, bgLinePaint);
        canvas.drawLine(0, getHeight() - 2, getWidth(), getHeight(), bgLinePaint);
        //中间的竖线
        bgDashPath.reset();
        // bgLinePaint.setPathEffect(new PathDashPathEffect(path, 15, 0, PathDashPathEffect.Style.ROTATE));
        bgLinePaint.setPathEffect(pathEffect);
        int itemWidth = (getWidth() - yTextWidth) / 7;
        for (int i = 1; i <= 7; i++) {
            bgDashPath.moveTo(i * itemWidth, 0);
            bgDashPath.lineTo(i * itemWidth, getHeight());
        }
        canvas.drawPath(bgDashPath, bgLinePaint);
    }

}
