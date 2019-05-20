package com.zkt.scratchview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
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
    /**
     * 存放蒙层像素信息的数组
     */
    private int mPixels[];
    /**
     * 最大擦除比例
     */
    private int mMaxPercent = 70;
    /**
     * 当前擦除比例
     */
    private int mPercent = 0;
    /**
     * 完成擦除
     */
    private boolean mIsCompleted = false;
    /**
     * 水印
     */
    private BitmapDrawable mWatermark;

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

        if (mWatermark != null) {
            Rect bounds = new Rect(rect);
            mWatermark.setBounds(bounds);
            mWatermark.draw(mMaskCanvas);
        }

        mPixels = new int[width * height];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mMaskBitmap, 0, 0, null);//绘制图层遮罩
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float downX = 0;
        float downY = 0;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                mErasePath.reset();
                mErasePath.moveTo(event.getX(), event.getY());
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

                float erasePixelCount = 0; //擦出的像素个数
                float totalPixelCount = width * height;

                for (int i = 0; i < totalPixelCount; i++) {
                    if (mPixels[i] == 0) { //透明的像素值为0
                        erasePixelCount++;
                    }
                }

                int percent = 0;
                if (erasePixelCount >= 0 && totalPixelCount > 0) {
                    percent = Math.round(erasePixelCount * 100 / totalPixelCount);
                    publishProgress(percent);
                }

                return percent >= mMaxPercent;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                mPercent = values[0];
                if (mEraseStatusListener != null) {
                    mEraseStatusListener.onProgress(mPercent);
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (result && !mIsCompleted) {//标记擦除，并完成回调
                    mIsCompleted = true;
                    if (mEraseStatusListener != null) {
                        mEraseStatusListener.onCompleted(ScratchView.this);
                    }
                }
            }

        }.execute(width, height);
    }


    private EraseStatusListener mEraseStatusListener;

    /**
     * 设置擦除监听器
     */
    public void setEraseStatusListener(EraseStatusListener listener) {
        this.mEraseStatusListener = listener;
    }

    /**
     * 擦除状态监听器
     */
    public interface EraseStatusListener {

        /**
         * 擦除进度
         *
         * @param percent 进度值，大于0，小于等于100；
         */
        void onProgress(int percent);

        /**
         * 擦除完成回调函数
         */
        void onCompleted(View view);
    }

    /**
     * 水印图标
     */
    public void setWatermark(int resId) {
        if (resId == -1) {
            mWatermark = null;
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
            mWatermark = new BitmapDrawable(getResources(), bitmap);
            mWatermark.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        }
    }

    /**
     * 重置为初始状态
     */
    public void reset() {
        mIsCompleted = false;

        int width = getWidth();
        int height = getHeight();
        createMasker(width, height);
        invalidate();

        updateErasePercent();
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

        updateErasePercent();
    }

    public void setMaskColor(int maskColor) {
        mMaskPaint.setColor(maskColor);
    }

    public void setEraseSize(int eraseSize) {
        this.mEraseSize = eraseSize;
    }
}
