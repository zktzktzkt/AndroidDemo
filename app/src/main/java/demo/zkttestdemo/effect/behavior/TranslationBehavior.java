package demo.zkttestdemo.effect.behavior;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.view.ViewCompat;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by zkt on 2017/7/31.
 */

public class TranslationBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    public TranslationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton child, @NonNull View dependency) {
        Log.e("TAG", "layoutDependsOn dependency->" + dependency);
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton child, @NonNull View dependency) {
        Log.e("TAG", "onDependentViewChanged");
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton child, int layoutDirection) {
        Log.e("TAG", "onLayoutChild");
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    //关注垂直滚动，而且向上的时候是出来，向下是隐藏
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    private boolean isOut = false;

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        Log.e("TAG", "onNestedScroll dyConsumed -> " + dyConsumed);
        if (dyConsumed > 0) {
            //手指向上滑动，,加一个标志位，否则会出现延迟
            if (!isOut) {
                //手指往上滑动
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
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        Log.e("TAG", "onNestedPreScroll -> " + dy);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, float velocityX, float velocityY) {
        Log.e("TAG", "onNestedPreFling -> " + velocityY);
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }


}
