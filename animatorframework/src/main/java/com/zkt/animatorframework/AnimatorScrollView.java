package com.zkt.animatorframework;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;


/**
 * 套入滚动，滚动时调用执行动画
 */
public class AnimatorScrollView extends ScrollView {
    private AnimatorLinearLayout mContent;

    public AnimatorScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {//渲染完毕回掉的
        super.onFinishInflate();
        mContent = (AnimatorLinearLayout)getChildAt(0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        View first = mContent.getChildAt(0);
        first.getLayoutParams().height = getHeight();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

    }

    //求三个数的中间大小的一个数。
    public static float clamp(float value, float max, float min){
        return Math.max(Math.min(value, max), min);
    }
}
