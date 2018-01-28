package demo.zkttestdemo.effect.lettersidebar;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zkt on 2018-1-28.
 */

public class LetterSideBar_Copy extends View {

    private Paint mPaint;
    //定义26个字母
    public static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private int mHeight;
    private int mWidth;
    private Paint mTouchPaint;
    private String mTouchletter;

    public LetterSideBar_Copy(Context context) {
        this(context, null);
    }

    public LetterSideBar_Copy(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBar_Copy(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(sp2px(12));
        mPaint.setTextAlign(Paint.Align.CENTER);

        mTouchPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTouchPaint.setColor(Color.RED);
        mTouchPaint.setTextSize(sp2px(12));
        mTouchPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = (int) (getPaddingLeft() + mPaint.measureText("A") + getPaddingRight());
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int x = mWidth / 2;
        //计算每一个格子的高度 = 总高度 / 数量
        int itemHeight = (mHeight - getPaddingTop() - getPaddingBottom()) / letters.length;

        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
        int fontCenterY = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        for (int i = 0, len = letters.length; i < len; i++) {

            int y = getPaddingTop() + i * itemHeight + itemHeight / 2 + fontCenterY;

            if (letters[i].equals(mTouchletter)) {
                canvas.drawText(letters[i], x, y, mTouchPaint);
            } else {
                canvas.drawText(letters[i], x, y, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int itemHeight = mHeight / letters.length + getPaddingTop();
                int index = (int) (event.getY() / itemHeight);
                if (index < 0) {
                    index = 0;
                }
                if (index > letters.length) {
                    index = letters.length - 1;
                }
                mTouchletter = letters[index];
                onTouchLetterListener.onTouchLetter(mTouchletter, true);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                onTouchLetterListener.onTouchLetter(mTouchletter, false);
                break;
        }
        return true;
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    OnTouchLetterListener onTouchLetterListener;

    public void setOnTouchLetterListener(OnTouchLetterListener onTouchLetterListener) {
        this.onTouchLetterListener = onTouchLetterListener;
    }

    interface OnTouchLetterListener {
        void onTouchLetter(String letter, boolean isTouch);
    }
}
