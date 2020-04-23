package demo.zkttestdemo.effect.city_58;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zkt on 2018-1-22.
 */

public class ShapeView extends View {
    private Shape mCurrentShape = Shape.Circle;
    Paint mPaint;
    private Path mPath;

    public ShapeView(Context context) {
        this(context, null);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //只保证是正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mCurrentShape) {
            case Circle:
                int center = getWidth() / 2;
                mPaint.setColor(Color.parseColor("#aa738ffe"));
                canvas.drawCircle(center, center, center, mPaint);
                break;
            case Square:
                mPaint.setColor(Color.parseColor("#aae84e40"));
                canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
                break;
            case Triangle:
                mPaint.setColor(Color.parseColor("#aa72d572"));
                if (mPath == null) {
                    mPath = new Path();
                    mPath.moveTo(getWidth() / 2, 0);
                    mPath.lineTo(0, ((float) (getWidth() / 2 * Math.sqrt(3))));
                    mPath.lineTo(getWidth(), ((float) (getWidth() / 2 * Math.sqrt(3))));
                    mPath.close();
                }
                canvas.drawPath(mPath, mPaint);
                break;
        }
    }

    /**
     * 改变形状
     */
    public void exchange() {
        switch (mCurrentShape) {
            case Circle:
                mCurrentShape = Shape.Square;
                break;
            case Square:
                mCurrentShape = Shape.Triangle;
                break;
            case Triangle:
                mCurrentShape = Shape.Circle;
                break;
        }
        invalidate();
    }


    public Shape getCurrentShape() {
        return mCurrentShape;
    }

    public enum Shape {
        Circle, Square, Triangle
    }
}
