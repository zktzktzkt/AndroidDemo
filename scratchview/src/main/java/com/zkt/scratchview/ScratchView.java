package com.zkt.scratchview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by zkt on 2018-9-27.
 * Description:
 */

public class ScratchView extends View {

    private Paint mMaskPaint;
    private int mMaskColor;
    private int default_masker_color;
    private Canvas mMaskCanvas;
    private Bitmap mMaskBitmap;
    private Path mErasePath;
    private int mEraseSize;
    private int mTouchSlop;
    private Paint mErasePaint;
    private Paint mBitmapPaint;
    /**
     * 存放蒙层像素信息的数组
     */
    private int mPixels[];

    public ScratchView(Context context) {
        this(context, null);
    }

    public ScratchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScratchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ScratchView);
        init(array);
        array.recycle();
    }

    private void init(TypedArray typedArray) {
        default_masker_color = ContextCompat.getColor(getContext(), R.color.gray_9);
        mMaskColor = typedArray.getColor(R.styleable.ScratchView_maskColor, default_masker_color);

        //蒙层的画笔
        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMaskPaint.setDither(true);
        setMaskColor(mMaskColor);

        //擦除的画笔
        setEraseSize(dp2px(10));
        mErasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mErasePaint.setDither(true);
        mErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mErasePaint.setStyle(Paint.Style.STROKE);
        mErasePaint.setStrokeCap(Paint.Cap.ROUND);
        mErasePaint.setStrokeWidth(mEraseSize);

        mErasePath = new Path();

        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createMasker(w, h);
    }

    /**
     * 创建蒙层
     */
    private void createMasker(int width, int height) {
        mMaskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mMaskCanvas = new Canvas(mMaskBitmap);
        Rect rect = new Rect(0, 0, width, height);
        mMaskCanvas.drawRect(rect, mMaskPaint);

        mPixels = new int[width * height];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mMaskBitmap, 0, 0, null);//绘制图层遮罩
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float downX = 0, downY = 0;
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mErasePath.reset();
                mErasePath.moveTo(event.getX(), event.getY());
                downX = event.getX();
                downY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) Math.abs(downX - event.getX());
                int dy = (int) Math.abs(downY - event.getY());
                if (dx >= mTouchSlop || dy >= mTouchSlop) {
                    mErasePath.lineTo(event.getX(), event.getY());
                    mMaskCanvas.drawPath(mErasePath, mErasePaint);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                updateErasePercent();
                break;
        }
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private void updateErasePercent() {
        int width = getWidth();
        int height = getHeight();
        new AsyncTask<Integer, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Integer... params) {
                int width = params[0];
                int height = params[1];
                //stride用于表示一行的像素个数有多少
                mMaskBitmap.getPixels(mPixels, 0, width, 0, 0, width, height);

                return null;
            }
        }.execute(width, height);
    }

    /**
     * 清除蒙层
     */
    public void clear() {
        int width = getWidth();
        int height = getHeight();
        mMaskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mMaskCanvas = new Canvas(mMaskBitmap);
        Rect rect = new Rect();
        mMaskCanvas.drawRect(rect, mErasePaint);
        invalidate();
    }

    public void setMaskColor(int maskColor) {
        mMaskPaint.setColor(maskColor);
    }

    public void setEraseSize(int eraseSize) {
        this.mEraseSize = eraseSize;
    }
}
