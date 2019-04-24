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

    PathEffect pathEffect = new DashPathEffect(new float[]{3, 10}, 0);
    //圆环的半径
    private float RADIUS_RING = SizeUtils.dp2px(5);
    //中间的白色圆的半径
    private float RADIUS_INNER_CIRCLE = SizeUtils.dp2px(4);
    //虚线path
    Path bgDashPath = new Path();
    //连接线的path
    Path linesPath = new Path();
    //画笔
    private Paint bgLinePaint;
    private Paint textPaint;
    private Paint linesPaint;
    private Paint pointStrokePaint;
    private Paint pointInnerPaint;
    //Y轴的宽度
    private int yTextWidth = SizeUtils.dp2px(80);
    //存放绘制点的x、y坐标
    List<PointF> pointList = new ArrayList<>(8);

    //X轴坐标
    String[] xArr = {"12:00", "01/09", "01/10", "01/11", "01/12", "01/13", "01/14"};
    //Y轴坐标
    String[] yArr = {"0.610", "0.510", "0.403", "0.302", "0.201", "0.109"};
    //要绘制的坐标点
    String[] pointArr = {"0.407", "0.501", "0.410", "0.505", "0.210", "0.303", "0.303"};


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

        //点的描边
        pointStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointStrokePaint.setColor(Color.parseColor("#FF2C93EC"));
        pointStrokePaint.setStyle(Paint.Style.STROKE);
        pointStrokePaint.setStrokeWidth(SizeUtils.dp2px(2));
        //点的内圆
        pointInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointInnerPaint.setColor(Color.parseColor("#ffffff"));
        pointInnerPaint.setStyle(Paint.Style.FILL);

        //连接线
        linesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linesPaint.setColor(Color.parseColor("#FF2C93EC"));
        linesPaint.setStyle(Paint.Style.STROKE);
        linesPaint.setStrokeWidth(SizeUtils.dp2px(3));
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
        //画点与点连接的线
        drawLines(canvas);
        //画点
        drawPoint(canvas);
    }

    private void drawLines(Canvas canvas) {
        double itemHeight = (double) getHeight() / 6;
        int itemWidth = (getWidth() - yTextWidth) / 7;
        //已知值[0]-[1]/对应的高度 = 随机值[i]/y
        for (int i = 0; i < pointArr.length; i++) {
            double y = Double.parseDouble(pointArr[i]) / ((Double.parseDouble(yArr[0]) - Double.parseDouble(yArr[1])) / itemHeight);
            pointList.get(i).y = (float) y;
        }
        for (int i = 0; i < pointArr.length; i++) {
            float x = (i + 1) * itemWidth - itemWidth / 2;
            pointList.get(i).x = x;
        }

        linesPath.reset();
        linesPath.moveTo(pointList.get(0).x, (float) (getHeight() - pointList.get(0).y + itemHeight / 2));
        for (int i = 1; i < pointList.size(); i++) {
            linesPath.lineTo(pointList.get(i).x, (float) (getHeight() - pointList.get(i).y + itemHeight / 2));
        }
        //        PathEffect pathEffect = new CornerPathEffect(SizeUtils.dp2px(5));
        //        linesPaint.setPathEffect(pathEffect);
        linesPaint.setStrokeJoin(Paint.Join.BEVEL);//拐角平头
        canvas.drawPath(linesPath, linesPaint);
    }

    private void drawPoint(Canvas canvas) {
        double itemHeight = (double) getHeight() / 6;
        int itemWidth = (getWidth() - yTextWidth) / 7;
        //已知值[0]-[1]/对应的高度 = 随机值[i]/y
        for (int i = 0; i < pointArr.length; i++) {
            double y = Double.parseDouble(pointArr[i]) / ((Double.parseDouble(yArr[0]) - Double.parseDouble(yArr[1])) / itemHeight);
            pointList.get(i).y = (float) y;
        }
        for (int i = 0; i < pointArr.length; i++) {
            float x = (i + 1) * itemWidth - itemWidth / 2;
            pointList.get(i).x = x;
        }

        for (int i = 0; i < pointList.size(); i++) {
            canvas.drawCircle(pointList.get(i).x,
                    (float) (getHeight() - pointList.get(i).y + itemHeight / 2),
                    RADIUS_RING, pointStrokePaint);

            canvas.drawCircle(pointList.get(i).x,
                    (float) (getHeight() - pointList.get(i).y + itemHeight / 2),
                    RADIUS_INNER_CIRCLE, pointInnerPaint);
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
