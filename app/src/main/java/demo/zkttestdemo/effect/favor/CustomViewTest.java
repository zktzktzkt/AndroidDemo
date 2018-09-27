package demo.zkttestdemo.effect.favor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zkt on 2018-2-1.
 */

public class CustomViewTest extends View {

    private Paint mBluePaint;
    private int mHeight;
    private int mWidth;
    private int mRadius;
    private Paint mRedPaint;
    private PorterDuffXfermode porterDuffXfermode;
    private Bitmap mSrcBitmap;
    private Bitmap mDstBitmap;
    private Paint mPaint;

    public CustomViewTest(Context context) {
        this(context, null);
    }

    public CustomViewTest(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomViewTest(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBluePaint.setColor(Color.BLUE);
        mBluePaint.setStyle(Paint.Style.FILL);

        mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRedPaint.setColor(Color.RED);
        mRedPaint.setStyle(Paint.Style.FILL);

        mPaint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = Math.min(w, h);

        mDstBitmap = Bitmap.createBitmap(mRadius / 2, mRadius / 2, Bitmap.Config.ARGB_8888);
        Canvas mDstCanvas = new Canvas(mDstBitmap);
        mDstCanvas.translate(mRadius / 4, mRadius / 4);
        mDstCanvas.drawCircle(0, 0, mRadius / 4, mRedPaint);
        //        mDstBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.night1);

        mSrcBitmap = Bitmap.createBitmap(mRadius, mRadius, Bitmap.Config.ARGB_8888);
        Canvas mSrcCanvas = new Canvas(mSrcBitmap);
        mSrcCanvas.translate(mRadius / 4, mRadius / 4);
        mSrcCanvas.drawRect(0, 0, mRadius - 600, mRadius - 600, mBluePaint);
        //        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.night1);

        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //使用xfermode需要新建一个画布，否则会不显示的
        int saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(mDstBitmap, 0, 0, mPaint);
        mPaint.setXfermode(porterDuffXfermode);
        //        canvas.drawBitmap(mSrcBitmap, 0, 0, mPaint);
        canvas.save();
        canvas.translate(mRadius / 4, mRadius / 4);
        canvas.drawRect(0, 0, mRadius - 600, mRadius - 600, mPaint);
        canvas.restore();
        mPaint.setXfermode(null);

        canvas.restoreToCount(saved);
    }
}
