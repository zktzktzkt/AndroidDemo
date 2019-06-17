package demo.zkttestdemo.effect.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 19/06/17.
 * Description:
 */
public class CustomPicView extends View {

    private Bitmap tempBitmap;
    private Canvas tempBitmapCanvas;
    private int width;
    private int height;
    private Paint shapePaint;
    private Path shapePath;
    private Rect mSrcRect, mDestRect;
    private final Bitmap bitmap;
    private PorterDuffXfermode xfermode;

    {
        shapePaint = new Paint();
        shapePath = new Path();
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    public CustomPicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.dst)).getBitmap();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveSize(500, widthMeasureSpec), resolveSize(500, widthMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        mSrcRect = new Rect(0, 0, width, height);
        mDestRect = new Rect(0, 0, width, height);

        if (tempBitmap == null) {
            //创建图形
            tempBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            tempBitmapCanvas = new Canvas(tempBitmap);
            //  tempBitmapCanvas.drawColor(Color.WHITE); //默认是透明的（看起来是黑色）
            shapePath.addCircle(width / 2, height / 2, width / 2 - 50, Path.Direction.CW);
            //        shapePath.moveTo();
            tempBitmapCanvas.drawPath(shapePath, shapePaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //离屏缓冲（不设置的话背景会变黑）
        int saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        //Rect src: 是对图片进行裁截，若是空null则显示整个图片  Rect dst：是图片在Canvas画布中显示的区域，
        canvas.drawBitmap(bitmap, null, mDestRect, shapePaint);
        shapePaint.setXfermode(xfermode);
        canvas.drawBitmap(tempBitmap, 0, 0, shapePaint);
        shapePaint.setXfermode(null);
        canvas.restoreToCount(saved);

    }
}
