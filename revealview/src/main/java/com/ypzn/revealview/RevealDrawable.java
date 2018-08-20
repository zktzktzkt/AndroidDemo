package com.ypzn.revealview;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;

public class RevealDrawable extends Drawable {

    private static final String TAG = "zkt";
    private final Rect mTmpRect = new Rect();
    private Drawable mUnselectedDrawable;
    private Drawable mSelectedDrawable;
    private int mOrientation;
    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;

    public RevealDrawable(Drawable unselected, Drawable selected) {
        mUnselectedDrawable = unselected;
        mSelectedDrawable = selected;
    }

    @Override
    public void draw(Canvas canvas) {
        // 获取drawable的边界
        Rect r = getBounds();

        Rect temp = new Rect();
        //1. 从已有的bound矩形边界范围当中抠出一个我们想要的矩形
        Gravity.apply(
                //参1：从左边抠
                Gravity.LEFT,
                //参2：目标矩形宽
                r.width() / 2,
                //参3：目标矩形高
                r.height(),
                //参4：被抠的地方
                r,
                //参5：抠出来后，放到哪
                temp
        );
        canvas.save();
        canvas.clipRect(temp);
        mUnselectedDrawable.draw(canvas);
        canvas.restore();

        //2. 从已有的bound矩形边界范围当中抠出一个我们想要的矩形
        Gravity.apply(
                Gravity.RIGHT,
                r.width() / 2,
                r.height(),
                r,
                temp
        );
        canvas.save();
        canvas.clipRect(temp);
        mSelectedDrawable.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        // 定好两个Drawable图片的宽高---边界bounds
        mUnselectedDrawable.setBounds(bounds);
        mSelectedDrawable.setBounds(bounds);
        Log.d(TAG, "w = " + bounds.width());
    }

    @Override
    public int getIntrinsicWidth() {
        //得到Drawable的实际宽度
        return Math.max(mSelectedDrawable.getIntrinsicWidth(),
                mUnselectedDrawable.getIntrinsicWidth());
    }

    @Override
    public int getIntrinsicHeight() {
        //得到Drawable的实际高度
        return Math.max(mSelectedDrawable.getIntrinsicHeight(),
                mUnselectedDrawable.getIntrinsicHeight());
    }

    @Override
    protected boolean onLevelChange(int level) {
        // 当设置level的时候回调---提醒自己重新绘制
        invalidateSelf();
        return true;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

}
