package demo.zkttestdemo.effect.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by zkt on 2017/7/31.
 */

public class TranslationBehavior extends FloatingActionButton.Behavior {
    public TranslationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //关注垂直滚动，而且向上的时候是出来，向下是隐藏

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL; //判断是否是垂直
    }

    private boolean isOut = false;

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Log.e("TAG", "dyConsumed -> " + dyConsumed);
        if (dyConsumed > 0) {
            if (!isOut) {
                //手指往上滑动,加一个标志位，已经往下走了.不加标志位的话会出现延迟往下走
                int translationY = ((CoordinatorLayout.LayoutParams) child.getLayoutParams()).bottomMargin + child.getMeasuredHeight();
                child.animate().translationY(translationY).setDuration(500).start();
                isOut = true;
            }
        } else {
            if (isOut) {
                //手指往下滑
                child.animate().translationY(0).setDuration(500).start();
                isOut = false;
            }
        }
    }


    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }


}
