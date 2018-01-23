package demo.zkttestdemo.effect.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 二阶贝塞尔曲线
 * Created by zkt on 2017-11-17.
 */

public class SecondBezierView extends View {

    private float mStartPointX;
    private float mStartPointY;
    private float mEndPointX;

    private float mEndPointY;
    private float mFlagPointX;

    private float mFlagPointY;
    private Path mPath;

    private Paint mPaintBezier;
    private Paint mPaintFlag;
    private Paint mPaintFlagTxt;

    public SecondBezierView(Context context) {
        super(context);
    }

    public SecondBezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaintBezier = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBezier.setStrokeWidth(8);
        mPaintBezier.setStyle(Paint.Style.STROKE);

        mPaintFlag = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFlag.setStrokeWidth(3);
        mPaintFlag.setStyle(Paint.Style.STROKE);

        mPaintFlagTxt = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFlagTxt.setTextSize(30);
    }

    public SecondBezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStartPointX = w / 4;
        mStartPointY = h / 2 - 200;

        mEndPointX = w * 3 / 4;
        mEndPointY = h / 2 - 200;

        mFlagPointX = w / 2;
        mFlagPointY = h / 2 - 300;

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(mStartPointX, mStartPointY);
        mPath.quadTo(mFlagPointX, mFlagPointY, mEndPointX, mEndPointY);
        canvas.drawPath(mPath, mPaintBezier);
        //
        canvas.drawPoint(mStartPointX, mStartPointY, mPaintFlag);
        canvas.drawText("起点", mStartPointX, mStartPointY, mPaintFlagTxt);
        canvas.drawPoint(mEndPointX, mEndPointY, mPaintFlag);
        canvas.drawText("终点", mEndPointX, mEndPointY, mPaintFlagTxt);
        canvas.drawPoint(mFlagPointX, mFlagPointY, mPaintFlag);
        canvas.drawText("控制点", mFlagPointX, mFlagPointY, mPaintFlagTxt);
        //
        canvas.drawLine(mStartPointX, mStartPointY, mFlagPointX, mFlagPointY, mPaintFlag);
        canvas.drawLine(mFlagPointX, mFlagPointY, mEndPointX, mEndPointY, mPaintFlag);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mFlagPointX = event.getX();
                mFlagPointY = event.getY();
                invalidate();
                break;
        }
        return true;
    }
}







