package demo.zkttestdemo.effect.doodle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
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
    private int preX, preY;
    private Paint paint;
    private Bitmap bitmapBuffer;
    private Canvas bitmapBufferCanvas;
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
        if (bitmapBuffer == null) {
            width = getMeasuredWidth();
            height = getMeasuredHeight();

            bitmapBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmapBufferCanvas = new Canvas(bitmapBuffer);
            bitmapBufferCanvas.drawColor(Color.WHITE); //默认是透明的（看起来是黑色）
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //存储历史轨迹。ACTION_UP之后，在缓存的bitmap上画历史轨迹，然后再画在canvas上
        canvas.drawBitmap(bitmapBuffer, 0, 0, null);

        //画当前的轨迹
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                preX = x;
                preY = y;
                path.moveTo(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                //手指移动过程中只显示绘制过程
                //使用贝塞尔曲线进行绘图，需要一个起点（preX,preY）
                //一个终点（x, y），一个控制点((preX + x) / 2, (preY + y) / 2))
                int controlX = (x + preX) / 2;
                int controlY = (y + preY) / 2;
                path.quadTo(controlX, controlY, x, y);
                invalidate();
                preX = x;
                preY = y;
                break;

            case MotionEvent.ACTION_UP:
                bitmapBufferCanvas.drawPath(path, paint);
                invalidate();
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
            bitmapBuffer.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
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
        bitmapBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmapBufferCanvas.setBitmap(bitmapBuffer);
        bitmapBufferCanvas.drawColor(Color.WHITE); //默认是透明的（看起来是黑色）
        invalidate();
    }
}