package demo.zkttestdemo.effect.doodle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 涂鸦板
 * Created by zkt on 2017-10-30.
 */

public class DoodleView extends View {

    private Path path;
    private int downX, downY;
    private Paint paint;
    private Bitmap bufferBitmap;
    private Canvas bufferBitmapCanvas;
    private int width;
    private int height;

    public DoodleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initPaint();
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (bufferBitmap == null) {
            width = getMeasuredWidth();
            height = getMeasuredHeight();

            bufferBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bufferBitmapCanvas = new Canvas(bufferBitmap);
            bufferBitmapCanvas.drawColor(Color.WHITE); //默认是透明的（看起来是黑色）
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bufferBitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                path.reset();
                path.moveTo(downX, downY);
                break;

            case MotionEvent.ACTION_MOVE:
                int controlX = (x + downX) / 2;
                int controlY = (y + downY) / 2;
                path.quadTo(controlX, controlY, x, y);
                bufferBitmapCanvas.drawPath(path, paint);
                invalidate();
                downX = x;
                downY = y;
                break;
        }
        return true;
    }

    /**
     * 保存到文件
     *
     * @param fileName
     * @return
     */
    public String saveFile(String fileName) {
        File file = new File(getContext().getExternalCacheDir(), fileName + ".png");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bufferBitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getPath();
    }

    /**
     * 清除画布
     */
    public void clearDraw() {
        path.reset();
        bufferBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bufferBitmapCanvas.setBitmap(bufferBitmap);
        bufferBitmapCanvas.drawColor(Color.WHITE); //默认是透明的（看起来是黑色）
        invalidate();
    }


}
