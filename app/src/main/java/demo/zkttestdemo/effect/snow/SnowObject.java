package demo.zkttestdemo.effect.snow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import java.util.Random;

/**
 * Created by zkt on 2017-12-23.
 */

public class SnowObject {

    private Random random;
    private int parentWidth;//父容器宽度
    private int parentHeight;//父容器高度
    private float objectWidth;//下落物体宽度
    private float objectHeight;//下落物体高度

    public int initSpeed;//初始下降速度

    public float presentX;//当前位置X坐标
    public float presentY;//当前位置Y坐标
    public float presentSpeed;//当前下降速度

    private Bitmap bitmap;
    public Builder builder;

    private boolean isSpeedRandom;//物体初始下降速度比例是否随机
    private boolean isSizeRandom;//物体初始大小比例是否随机

    private static final int defaultSpeed = 10;//默认下降速度

    public SnowObject(SnowObject snowObject, int parentWidth, int parentHeight) {
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;

        random = new Random();
        presentX = random.nextInt(parentWidth);//随机物体的X坐标
        presentY = random.nextInt(parentHeight) - parentHeight;//随机物体的Y坐标，并让物体一开始从屏幕顶部下落

        bitmap = snowObject.bitmap;
        initSpeed = snowObject.initSpeed;
        isSpeedRandom = snowObject.isSpeedRandom;
        isSizeRandom = snowObject.isSizeRandom;

        randomSpeed();
        randomSize();
    }

    private SnowObject(Builder builder) {
        this.builder = builder;
        initSpeed = builder.initSpeed;
        bitmap = builder.bitmap;
        isSpeedRandom = builder.isSpeedRandom;
        isSizeRandom = builder.isSizeRandom;
    }

    static class Builder {
        private int initSpeed;
        private Bitmap bitmap;
        private boolean isSpeedRandom;
        private boolean isSizeRandom;

        Builder(Drawable drawable) {
            this.initSpeed = defaultSpeed;
            this.bitmap = drawableToBitmap(drawable);
            this.isSpeedRandom = false;
            this.isSizeRandom = false;
        }

        /**
         * 设置物体的初始下落速度
         *
         * @param speed
         * @param isRandomSpeed 物体初始下降速度比例是否随机
         * @return
         */
        public Builder setSpeed(int speed, boolean isRandomSpeed) {
            this.initSpeed = speed;
            this.isSpeedRandom = isRandomSpeed;
            return this;
        }


        /**
         * 设置物体大小
         *
         * @param w
         * @param h
         * @param isRandomSize 物体初始大小比例是否随机
         * @return
         */
        public Builder setSize(int w, int h, boolean isRandomSize) {
            this.bitmap = changeBitmapSize(this.bitmap, w, h);
            this.isSizeRandom = isRandomSize;
            return this;
        }

        public SnowObject build() {
            return new SnowObject(this);
        }

    }

    /**
     * 绘制物体对象
     *
     * @param canvas
     */
    void drawObject(Canvas canvas) {
        moveObject();
        canvas.drawBitmap(bitmap, presentX, presentY, null);
    }

    /**
     * 移动物体对象
     */
    private void moveObject() {
        presentY += presentSpeed;
        if (presentY > parentHeight) {
            reset();
        }
    }


    /**
     * 重置object位置
     */
    private void reset() {
        presentY = -objectHeight;

        randomSpeed();//记得重置时速度也一起重置，这样效果会好很多
    }

    /**
     * 随机物体初始下落速度
     */
    private void randomSpeed() {
        if (isSpeedRandom) {
            presentSpeed = (float) ((random.nextInt(3) + 1) * 0.1 + 1) * initSpeed;//这些随机数大家可以按自己的需要进行调整
        } else {
            presentSpeed = initSpeed;
        }
    }

    /**
     * 随机物体初始大小比例
     */
    private void randomSize() {
        if (isSizeRandom) {
            float r = (random.nextInt(10) + 1) * 0.1f;
            float rW = r * bitmap.getWidth();
            float rH = r * bitmap.getHeight();
            bitmap = changeBitmapSize(bitmap, (int) rW, (int) rH);
        }
        objectWidth = bitmap.getWidth();
        objectHeight = bitmap.getHeight();
    }

    /**
     * drawable图片资源转bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 改变bitmap的大小
     *
     * @param bitmap 目标bitmap
     * @param newW   目标宽度
     * @param newH   目标高度
     * @return
     */
    public static Bitmap changeBitmapSize(Bitmap bitmap, int newW, int newH) {
        int oldW = bitmap.getWidth();
        int oldH = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newW) / oldW;
        float scaleHeight = ((float) newH) / oldH;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, oldW, oldH, matrix, true);
        return bitmap;
    }

}
