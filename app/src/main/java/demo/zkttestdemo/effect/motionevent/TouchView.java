package demo.zkttestdemo.effect.motionevent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 19/01/07.
 * Description:
 */
public class TouchView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap bitmap;
    float offsetX;
    float offsetY;
    float downX;
    float downY;
    float originalDownX;
    float originalDownY;
    int trackingPointerId;

    public TouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.night1);
        bitmap = setImgSize(this.bitmap, dp2px(200), dp2px(200));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                trackingPointerId = event.getPointerId(0);//作为一名程序员，你得知道是哪根手指按下去了
                downX = event.getX();
                downY = event.getY();
                originalDownX = offsetX;
                originalDownY = offsetY;
                break;
            case MotionEvent.ACTION_MOVE:
                int index = event.findPointerIndex(trackingPointerId);//哪根手指移动了
                offsetX = originalDownX + event.getX(index) - downX;
                offsetY = originalDownY + event.getY(index) - downY;
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                int actionIndex = event.getActionIndex();
                trackingPointerId = event.getPointerId(actionIndex);
                downX = event.getX(actionIndex);
                downY = event.getY(actionIndex);
                originalDownX = offsetX;
                originalDownY = offsetY;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                actionIndex = event.getActionIndex();
                int pointerId = event.getPointerId(actionIndex);
                if (pointerId == trackingPointerId) {
                    int newIndex;
                    if (actionIndex == event.getPointerCount() - 1) {
                        newIndex = event.getPointerCount() - 2;
                    } else {
                        newIndex = event.getPointerCount() - 1;
                    }
                    trackingPointerId = event.getPointerId(newIndex);
                    downX = event.getX(actionIndex);
                    downY = event.getY(actionIndex);
                    originalDownX = offsetX;
                    originalDownY = offsetY;
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
    }

    public Bitmap setImgSize(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
