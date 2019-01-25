package demo.zkttestdemo.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 18/12/24.
 * Description:
 */
public class ScalableImageView extends View {

    private final int IMAGE_WIDTH = dp2px(300);
    private final float OVER_SCALE_FACTOR = 1.5f;

    Bitmap bitmap;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    MyGestureListener myGestureListener = new MyGestureListener();
    MyFlingRunner myFlingRunner = new MyFlingRunner();
    ScaleGestureDetector scaleDetector;
    MyScaleListener myScaleListener = new MyScaleListener();
    float offsetX;
    float offsetY;
    float originalOffsetX;
    float originalOffsetY;
    float smallScale;
    float bigScale;
    boolean big;
    float currentScale;
    ObjectAnimator scaleAnimator;
    GestureDetectorCompat detector;
    OverScroller scroller;

    public ScalableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.night1);
        bitmap = setImgSize(this.bitmap, IMAGE_WIDTH, IMAGE_WIDTH);

        detector = new GestureDetectorCompat(context, myGestureListener);
        scroller = new OverScroller(context);
        scaleDetector = new ScaleGestureDetector(context, myScaleListener);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        originalOffsetX = (getWidth() - bitmap.getWidth()) / 2f;
        originalOffsetY = (getHeight() - bitmap.getHeight()) / 2f;

        if ((float) bitmap.getWidth() / bitmap.getHeight() > (float) getWidth() / getHeight()) {
            smallScale = (float) getWidth() / bitmap.getWidth();
            bigScale = (float) getHeight() / bitmap.getHeight() * OVER_SCALE_FACTOR;
        } else {
            smallScale = (float) getHeight() / bitmap.getHeight();
            bigScale = (float) getWidth() / bitmap.getWidth() * OVER_SCALE_FACTOR;
        }
        currentScale = smallScale;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float scaleFraction = (currentScale - smallScale) / (bigScale - smallScale);
        canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction);
        canvas.scale(currentScale, currentScale, getWidth() / 2f, getHeight() / 2f);
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint);
    }

    public float getCurrentScale() {
        return currentScale;
    }

    public void setCurrentScale(float currentScale) {
        this.currentScale = currentScale;
        invalidate();
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

    private ObjectAnimator getScaleAnimator() {
        if (scaleAnimator == null) {
            scaleAnimator = ObjectAnimator.ofFloat(this, "currentScale", 0);
        }
        scaleAnimator.setFloatValues(smallScale, bigScale);
        return scaleAnimator;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = scaleDetector.onTouchEvent(event);
        if (!scaleDetector.isInProgress()) {
            result = detector.onTouchEvent(event);
        }
        return result;
    }

    class MyScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        float initialScale;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            initialScale = currentScale;
            return true; //消费事件
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            currentScale = initialScale * detector.getScaleFactor();
            invalidate();
            return false;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    }

    //====================================================================================================
    //                                       GestureDetector
    //====================================================================================================

    class MyFlingRunner implements Runnable {
        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                offsetX = scroller.getCurrX();
                offsetY = scroller.getCurrY();
                invalidate(); //到了下一帧刷新界面
                postOnAnimation(this); //到了下一帧执行Runnable
            }
        }
    }

    private void fixOffsets() {
        //从左往右滑
        offsetX = Math.min(offsetX, (bitmap.getWidth() * bigScale - getWidth()) / 2);
        //从右往左滑
        offsetX = Math.max(offsetX, -(bitmap.getWidth() * bigScale - getWidth()) / 2);
        //从上往下滑
        offsetY = Math.min(offsetY, (bitmap.getHeight() * bigScale - getHeight()) / 2);
        //从下往上滑
        offsetY = Math.max(offsetY, -(bitmap.getHeight() * bigScale - getHeight()) / 2);
    }

    class MyGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

        /**
         * 一个很重要的回调，它的作用，相当于在onTouchEvent里，ACTION_DOWN事件的返回值
         */
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        /**
         * 点击抬起的时候出发（非长按）
         */
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        /**
         * 滑动
         *
         * @param distanceX 旧值减新值，位移差
         */
        @Override
        public boolean onScroll(MotionEvent down, MotionEvent event, float distanceX, float distanceY) {
            if (big) {
                offsetX -= distanceX;
                offsetY -= distanceY;
                fixOffsets();
                invalidate();
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        /**
         * 滑动后动手会回调
         *
         * @return 返回值没用
         */
        @Override
        public boolean onFling(MotionEvent down, MotionEvent event, float velocityX, float velocityY) {
            if (big) {
                scroller.fling((int) offsetX, (int) offsetY, (int) velocityX, (int) velocityY,
                        -(int) (bitmap.getWidth() * bigScale - getWidth()) / 2,
                        (int) (bitmap.getWidth() * bigScale - getWidth()) / 2,
                        -(int) (bitmap.getHeight() * bigScale - getHeight()) / 2,
                        (int) (bitmap.getHeight() * bigScale - getHeight()) / 2);

                //在下一帧执行run方法，防止像post()同一帧执行好几次run方法
                //相当于16ms后执行
                postOnAnimation(myFlingRunner);
            }
            return false;
        }

        /**
         * 和onSingleTapUp区别：
         * - 如果双击的话，手指抬起，onSingleTapUp也会被调用，会导致区分不出来单击还是双击。
         * - 但onSingleTapConfirmed，只有单击抬起才会执行，双击不会执行
         *
         * @return 没用
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        /**
         * 两次单击间隔不超过300ms
         *
         * @return 没用
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            big = !big;
            if (big) {
                offsetX = (e.getX() - getWidth() / 2f) - (e.getX() - getWidth() / 2f) * bigScale / smallScale;
                offsetY = (e.getY() - getHeight() / 2f) - (e.getY() - getHeight() / 2f) * bigScale / smallScale;
                getScaleAnimator().start();
            } else {
                getScaleAnimator().reverse();
            }
            return false;
        }

        /**
         * 双击后移动、抬起，会被触发
         *
         * @return 没用
         */
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

    }


}
