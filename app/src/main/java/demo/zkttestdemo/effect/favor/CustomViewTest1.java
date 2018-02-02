package demo.zkttestdemo.effect.favor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2018-2-1.
 */

public class CustomViewTest1 extends View {
    private Paint mPaint;
    private Bitmap dstBmp, srcBmp;
    private RectF dstRect, srcRect;

    private Xfermode mXfermode;
    private PorterDuff.Mode mPorterDuffMode = PorterDuff.Mode.DST_OUT;

    public CustomViewTest1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        dstBmp = BitmapFactory.decodeResource(getResources(), R.drawable.dst);
        srcBmp = BitmapFactory.decodeResource(getResources(), R.drawable.src);
        mXfermode = new PorterDuffXfermode(mPorterDuffMode);
    }

    /*public CustomViewTest(Context context) {
        super(context);

    }*/

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = w <= h ? w : h;
        int centerX = w / 2;
        int centerY = h / 2;
        int quarterWidth = width / 4;
        srcRect = new RectF(0, 0, 500, 500);
        dstRect = new RectF(300, 300, 1000, 1000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //背景色设为白色，方便比较效果
        canvas.drawColor(Color.WHITE);
        //将绘制操作保存到新的图层，因为图像合成是很昂贵的操作，将用到硬件加速，这里将图像合成的处理放到离屏缓存中进行
        int saveCount = canvas.saveLayer(srcRect, mPaint, Canvas.ALL_SAVE_FLAG);
        //绘制目标图
        canvas.drawBitmap(dstBmp, null, dstRect, mPaint);
        //设置混合模式
        mPaint.setXfermode(mXfermode);
        //绘制源图
        canvas.drawBitmap(srcBmp, null, srcRect, mPaint);
        //清除混合模式
        mPaint.setXfermode(null);
        //还原画布
        canvas.restoreToCount(saveCount);
    }
}
