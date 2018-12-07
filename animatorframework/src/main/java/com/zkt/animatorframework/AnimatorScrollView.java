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

    /**
     * xml解析完后回调
     * {@link android.view.LayoutInflater#rInflate}
     */
    @Override
    protected void onFinishInflate() {//渲染完毕回掉的
        super.onFinishInflate();
        mContent = (AnimatorLinearLayout) getChildAt(0);
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

        for (int i = 0; i < mContent.getChildCount(); i++) {
            View child = mContent.getChildAt(i);

            if (!(child instanceof DiscrollInterface)) {
                continue;
            }

            DiscrollInterface discrollInterface = (AnimatorFrameLayout) child;
            //得到滑动出来的距离
            int childTop = child.getTop();
            int absoluteTop = childTop - t;
            int scrollViewHeight = getHeight();

            if (absoluteTop <= scrollViewHeight) {
                int visibleHeight = scrollViewHeight - absoluteTop;
                float ratio = visibleHeight / (float) child.getHeight();
                discrollInterface.onDiscroll(clamp(ratio, 1, 0));
            } else {
                discrollInterface.onResetDiscroll();
            }

        }


    }

    //求三个数的中间大小的一个数。
    private float clamp(float value, float max, float min) {
        //最大不能大过max，最小不能小过min
        return Math.max(Math.min(value, max), min);
    }
}
