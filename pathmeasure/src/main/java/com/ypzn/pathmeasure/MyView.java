package com.ypzn.pathmeasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by zkt on 2018-9-5.
 * Description:
 */

public class MyView extends View {
    private static final String TAG = "MyView";
    private Paint mDefaultPaint;
    private int mViewWidth;
    private int mViewHeight;
    private Paint mPaint;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mDefaultPaint = new Paint();
        mDefaultPaint.setColor(Color.RED);
        mDefaultPaint.setStrokeWidth(5);
        mDefaultPaint.setStyle(Paint.Style.STROKE);

        mPaint = new Paint();
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //坐标系中心移动中间
        canvas.translate(mViewWidth / 2, mViewHeight / 2);
        //画坐标线
        canvas.drawLine(-canvas.getWidth(), 0, canvas.getWidth(), 0, mPaint);
        canvas.drawLine(0, -canvas.getHeight(), 0, canvas.getHeight(), mPaint);

        testNextContour(canvas);
        //testGetSegmentMoveTo(canvas);
        //testGetSegment(canvas);
        //testForceClosed(canvas);
    }

    private void testNextContour(Canvas canvas) {
        Path path = new Path();
        Path path1 = new Path();
        Path path2 = new Path();
        //添加小矩形
        path1.addRect(-100, -100, 100, 100, Path.Direction.CW);
        PathMeasure measure2 = new PathMeasure(path1, false);
        Log.i(TAG, "path1:" + measure2.getLength());
        //添加大矩形
        path2.addRect(-200, -200, 200, 200, Path.Direction.CW);
        PathMeasure measure3 = new PathMeasure(path2, false);
        Log.i(TAG, "path2:" + measure3.getLength());

        //path组合
        path.op(path2, path1, Path.Op.XOR);
        canvas.drawPath(path, mDefaultPaint);

        //-------------------------------------
        PathMeasure measure = new PathMeasure(path, false);

        float[] tan = new float[2];
        float[] pos = new float[2];

        measure.getPosTan(50f, pos, tan);
        canvas.drawLine(tan[0], tan[1], pos[0], pos[1], mPaint);
        Log.i(TAG, "----------------------pos[0] = " + pos[0] + "pos[1] = " + pos[1]);
        Log.i(TAG, "----------------------tan[0] = " + tan[0] + "tan[1] = " + tan[1]);

       /* float len1 = measure.getLength();
        Log.i(TAG, "len1 = " + len1);
        //跳转到下一条路径
        measure.nextContour();

        measure.getPosTan(0f, pos, tan);
        Log.i(TAG, "----------------------pos[0] = " + pos[0] + "pos[1] = " + pos[1]);
        Log.i(TAG, "----------------------tan[0] = " + tan[0] + "tan[1] = " + tan[1]);
        canvas.drawLine(tan[0], tan[1], pos[0], pos[1], mDefaultPaint);

        float len2 = measure.getLength();
        Log.i(TAG, "len2 = " + len2);*/
    }

}
