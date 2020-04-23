package demo.zkttestdemo.effect.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import demo.zkttestdemo.R;

/**
 *
 */

public class ZoomImageView extends View {

    //放大倍数
    private static final int FACTOR = 2;
    //放大镜的半径
    private static final int RADIUS = 100;
    // 原图
    private Bitmap mBitmap;
    // 放大后的图
    private Bitmap mBitmapScale;
    // 制作的圆形的图片（放大的局部），盖在Canvas上面
    private ShapeDrawable mShapeDrawable;

    private Matrix mMatrix;

    public ZoomImageView(Context context) {
        super(context);
        init();
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //原图Bitmap
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sky);

        //设置放大镜
        mBitmapScale = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth() * FACTOR, mBitmap.getHeight() * FACTOR, true);
        BitmapShader bitmapShader = new BitmapShader(mBitmapScale, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mShapeDrawable = new ShapeDrawable(new OvalShape());
        mShapeDrawable.setBounds(0, 0, RADIUS * 2, RADIUS * 2);
        mShapeDrawable.getPaint().setShader(bitmapShader);

        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1、画原图
        canvas.drawBitmap(mBitmap, 0, 0, null);

        // 2、画放大镜的图
        mShapeDrawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        // 1. 先设置Drawable是什么样的
        // 1.1 图片放大了FACTOR倍，也就是说X和Y轴拉伸了FACTOR倍，所以需要找到手指按下的x点所对应的放大图片的x点在哪
        // 1.2 按下x=1 -> 对应放大的图x=-2
        mMatrix.setTranslate(-(FACTOR * x) + RADIUS, -(FACTOR * y) + RADIUS);
        mShapeDrawable.getPaint().getShader().setLocalMatrix(mMatrix);

        // 2. 切出手势区域点位置的圆
        mShapeDrawable.setBounds(x - RADIUS, y - RADIUS, x + RADIUS, y + RADIUS);

        invalidate();
        return true;
    }
}
