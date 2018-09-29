package com.zkt.circlelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;

/**
 * Created by zkt on 2018-9-29.
 * Description: 圆形布局
 */

public class CircleLayout extends ViewGroup {
    private int mRadius;
    private int mCenterX;
    private int mCenterY;

    public CircleLayout(Context context) {
        this(context, null);
    }

    public CircleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRadius = dp2px(120);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int size = Math.max(mRadius * 2, getChildAt(0).getMeasuredWidth() * 3);
        setMeasuredDimension(size + mRadius, size + mRadius);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        double angle = Math.PI * 2 / getChildCount();
        for (int i = 0; i < getChildCount(); i++) {
            int cx = (int) (mCenterX + mRadius * Math.cos(angle * i));
            int cy = (int) (mCenterY + mRadius * Math.sin(angle * i));

            int childSize = getChildAt(i).getMeasuredWidth();
            int left = cx - childSize / 2;
            int top = cy - childSize / 2;
            int right = cx + childSize / 2;
            int bottom = cy + childSize / 2;

            getChildAt(i).layout(left, top, right, bottom);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
