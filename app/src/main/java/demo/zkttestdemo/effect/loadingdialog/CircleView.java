package demo.zkttestdemo.effect.loadingdialog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zkt on 2018-9-3.
 * Description:
 */

public class CircleView extends View {
    private Paint mPaint;
    private int mColor;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        canvas.drawCircle(cx, cy, cx, mPaint);

    }

    public void exchangeColor(int color) {
        mPaint.setColor(color);
        mColor = color;
        invalidate();
    }

    public int getcolor() {
        return mColor;
    }
}
