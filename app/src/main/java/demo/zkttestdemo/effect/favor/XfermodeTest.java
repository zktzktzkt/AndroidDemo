package demo.zkttestdemo.effect.favor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zkt on 2018-2-1.
 */

public class XfermodeTest extends View {

    private Paint mBluePaint;
    private int mHeight;
    private int mWidth;
    private int mRadius;
    private Paint mRedPaint;
    private Bitmap mSrcBitmap;
    private Bitmap mDstBitmap;
    private Paint mPaint;
    PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    {
        mBluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBluePaint.setColor(Color.BLUE);
        mBluePaint.setStyle(Paint.Style.FILL);

        mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRedPaint.setColor(Color.RED);
        mRedPaint.setStyle(Paint.Style.FILL);

        mPaint = new Paint();
    }

    public XfermodeTest(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public XfermodeTest(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 这次布局和上次布局相比有变化了，会回调
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = Math.min(w, h);
    }

    /**
     *
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //https://blog.csdn.net/cquwentao/article/details/51423371
        //离屏缓冲，创建一个新的透明图层。在restore之后，绘制到上一个图层。
        //为什么会需要一个新的图层？ 在处理xfermode的时候，原canvas上的图（包括背景）会影响src和dst的合成，这个时候，使用一个新的透明图层是一个很好的选择
        int saved = canvas.saveLayer(0, 0, getHeight(), getWidth(), mPaint);
        canvas.drawBitmap(createDstBitmap(), 0, 0, mPaint);
        mPaint.setXfermode(porterDuffXfermode);
        canvas.drawBitmap(createSrcBitmap(), 0, 0, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(saved);
    }

    public void drawDst(Canvas canvas) {
        canvas.translate(mRadius / 4, mRadius / 4);
        canvas.drawCircle(0, 0, mRadius / 4, mRedPaint);
    }

    public void drawSrc(Canvas canvas) {
        canvas.translate(mRadius / 4, mRadius / 4);
        canvas.drawRect(0, 0, mRadius - 600, mRadius - 600, mBluePaint);
    }

    public Bitmap createDstBitmap() {
        mDstBitmap = Bitmap.createBitmap(mRadius / 2, mRadius / 2, Bitmap.Config.ARGB_8888);
        Canvas mDstCanvas = new Canvas(mDstBitmap);
        mDstCanvas.translate(mRadius / 4, mRadius / 4);
        mDstCanvas.drawCircle(0, 0, mRadius / 4, mRedPaint);
        //        mDstBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.night1);
        return mDstBitmap;
    }

    public Bitmap createSrcBitmap() {
        mSrcBitmap = Bitmap.createBitmap(mRadius, mRadius, Bitmap.Config.ARGB_8888);
        Canvas mSrcCanvas = new Canvas(mSrcBitmap);
        mSrcCanvas.translate(mRadius / 4, mRadius / 4);
        mSrcCanvas.drawRect(0, 0, mRadius - 600, mRadius - 600, mBluePaint);
        //        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.night1);
        return mSrcBitmap;
    }
}
