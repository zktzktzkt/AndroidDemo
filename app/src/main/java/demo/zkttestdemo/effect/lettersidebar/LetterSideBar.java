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
 * Created by zkt on 2018-1-27.
 */

public class LetterSideBar extends View {
    private static final String TAG = "LetterSideBar";
    private Paint mPaint;
    private Paint mTouchPaint;
    //定义26个字母
    public static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private String mCurrTouchLetter;

    public LetterSideBar(Context context) {
        this(context, null);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(sp2px(12));
        mPaint.setColor(Color.BLUE);
        mPaint.setTextAlign(Paint.Align.CENTER);

        mTouchPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTouchPaint.setTextSize(sp2px(12));
        mTouchPaint.setColor(Color.RED);
        mTouchPaint.setTextAlign(Paint.Align.CENTER);
    }

    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //计算指定宽度 = 左右padding + 字母宽度(取决于画笔)
        int textWidth = (int) mPaint.measureText("A");
        int width = getPaddingLeft() + getPaddingRight() + textWidth;

        //高度可以直接获取
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画26个字母
        int x = getWidth() / 2;
        int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / letters.length;

        for (int i = 0, len = letters.length; i < len; i++) {
            // 单格高度的一半 + 前面格子的高度
            int letterCenterY = i * itemHeight + itemHeight / 2 + getPaddingTop();

            //基线往上为负，往下为正；由于文字是基于“基线”来画的，所以要把基线下面的“bottom”减掉
            // 如果不减掉bottom，则文字是对齐于bottom，而不是对齐于baseline
            Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
            int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
            int baseLine = letterCenterY + dy;

            //当前字母高亮，用两个画笔(最好)，改变字母颜色
            if (letters[i].equals(mCurrTouchLetter)) {
                canvas.drawText(letters[i], x, baseLine, mTouchPaint);
            } else {
                canvas.drawText(letters[i], x, baseLine, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //计算当前触摸的字母 获取当前的位置
                float currMoveY = event.getY();
                // currentMoveY / 单格的高度 = 位置
                int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / letters.length;
                int currPosition = (int) (currMoveY / itemHeight);
                if (currPosition < 0) {
                    currPosition = 0;
                }
                if (currPosition > letters.length - 1) {
                    currPosition = letters.length - 1;
                }

                mCurrTouchLetter = letters[currPosition];

                if (null != onTouchLetterListener) {
                    onTouchLetterListener.onTouch(mCurrTouchLetter, true);
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                if (null != onTouchLetterListener) {
                    onTouchLetterListener.onTouch(mCurrTouchLetter, false);
                }
                break;
        }
        return true;
    }

    private OnTouchLetterListener onTouchLetterListener;

    public void setOnTouchLetterListener(OnTouchLetterListener onTouchLetterListener) {
        this.onTouchLetterListener = onTouchLetterListener;
    }

    interface OnTouchLetterListener {
        void onTouch(String letter, boolean isTouch);
    }
}
