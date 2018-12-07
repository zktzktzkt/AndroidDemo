package demo.zkttestdemo.effect.dashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by zkt on 18/12/04.
 * Description:仪表盘
 */
public class DashBoardView extends View {
    private final int ANGLE = 120;
    private final int RADIUS = dp2px(150);
    private final int DASH_WIDTH = dp2px(2);
    private final int POINTER_LENGTH = dp2px(100);
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Path dashPath = new Path();
    PathDashPathEffect effect;

    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(dp2px(2));
        dashPath.addRect(0, 0, DASH_WIDTH, dp2px(10), Path.Direction.CW);

        Path arc = new Path();
        arc.addArc(getWidth() / 2 - RADIUS, getHeight() / 2 - RADIUS,
                getWidth() / 2 + RADIUS, getHeight() / 2 + RADIUS,
                90 + ANGLE / 2, 360 - ANGLE);
        PathMeasure pathMeasure = new PathMeasure(arc, false);
        effect = new PathDashPathEffect(dashPath, (pathMeasure.getLength() - DASH_WIDTH) / 20, 0, PathDashPathEffect.Style.ROTATE);
    }

    public DashBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画线
        canvas.drawArc(getWidth() / 2 - RADIUS, getHeight() / 2 - RADIUS,
                getWidth() / 2 + RADIUS, getHeight() / 2 + RADIUS,
                90 + ANGLE / 2, 360 - ANGLE, false, paint);

        //画刻度
        paint.setPathEffect(effect);
        canvas.drawArc(getWidth() / 2 - RADIUS, getHeight() / 2 - RADIUS,
                getWidth() / 2 + RADIUS, getHeight() / 2 + RADIUS,
                90 + ANGLE / 2, 360 - ANGLE, false, paint);
        paint.setPathEffect(null);

        //画指针
        canvas.drawLine(getWidth() / 2, getHeight() / 2,
                (float) Math.cos(Math.toRadians(getAngleFromMark(5))) * POINTER_LENGTH + getWidth() / 2,
                (float) Math.sin(Math.toRadians(getAngleFromMark(5))) * POINTER_LENGTH + getHeight() / 2,
                paint);

        //中间的点
        paint.setColor(Color.parseColor("#000000"));
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, dp2px(5), paint);

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public int getAngleFromMark(int mark) {
        return (int) (90 + (float) ANGLE / 2 + (360 - (float) ANGLE) / 20 * mark);
    }
}
