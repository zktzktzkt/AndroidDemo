package demo.zkttestdemo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by zkt on 2018-8-3.
 * Description: 渐变颜色textview
 */

public class LinearGradientTextView extends AppCompatTextView {

    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private int DELTA_X = 20;
    private int mTranslate;
    private LinearGradient mLinearGradient;
    private Matrix mMatrix;
    int curRow = 0;
    private int row;

    public LinearGradientTextView(Context context) {
        super(context);
        init();
    }

    public LinearGradientTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinearGradientTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPaint = getPaint();
        String text = getText().toString();
        float textWidth = mPaint.measureText(text);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float height = fontMetrics.ascent + fontMetrics.descent;
        float textSize = getTextSize();
        //行数
        row = (int) (height / textSize);

        //三个文字的宽度
        int gradientLenth = (int) (textWidth / text.length() * 3);

        //从左边距离文字gradientLenth开始渐变
        mLinearGradient = new LinearGradient(-gradientLenth, 0, 0, 0,
                new int[]{0x22ffffff, 0xffffffff, 0x22ffffff}, null, Shader.TileMode.CLAMP);
        mPaint.setShader(mLinearGradient);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mTranslate += DELTA_X;
        float textWidth = getPaint().measureText(getText().toString());
        //到底部进行返回
        if (mTranslate > textWidth + 1 || mTranslate < 1) {
            DELTA_X = -DELTA_X;
        }

        mMatrix = new Matrix();
        mMatrix.postTranslate(mTranslate, 0);
        mLinearGradient.setLocalMatrix(mMatrix);

        postInvalidateDelayed(50);
    }
}
