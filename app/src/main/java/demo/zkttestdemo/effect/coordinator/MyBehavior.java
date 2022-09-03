package demo.zkttestdemo.effect.coordinator;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Created by zkt on 2017-11-10.
 * <p>
 * 1. CoordinatorLayout根据子View的Attrs创建了Behavior，
 * 2. 当有触摸事件时，会把事件转发给Behavior
 * 3. Behavior的作用是 所依赖的View发生了变化，那设置Behavior的View该做些什么
 */

public class MyBehavior extends CoordinatorLayout.Behavior<Button> {
    private final int widthPixels;

    /**
     * 如果初始化，必须在两个参数的构造函数中实现，CoordinatorLayout源码中通过反射调用的两个参数的构造函数创建对象
     */
    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        widthPixels = displayMetrics.widthPixels;
    }

    /**
     * Button依赖TempView的改变
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, Button child, View dependency) {
        Log.e("TAG", "layoutDependsOn->" + dependency);
        return dependency instanceof TempView;
    }

    /**
     * 只有当layoutDependsOn返回true时, 才会回调.
     * 当依赖的TempView的大小或位置发生变化时，会回调
     * @param dependency 可以是CoordinatorLayout中的任意一个子view
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, Button child, View dependency) {
        Log.e("TAG", "onDependentViewChanged");

        if (dependency instanceof TempView) {
            //根据dependency的位置，设置button的位置
            int top = dependency.getTop();
            int left = dependency.getLeft();

            int x = widthPixels - left - child.getWidth();
            int y = top;

            setPosition(child, x, y);
        }

        return true;
    }

    private void setPosition(View v, int x, int y) {
        CoordinatorLayout.MarginLayoutParams layoutParams = (CoordinatorLayout.MarginLayoutParams) v.getLayoutParams();
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        v.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull Button child, int layoutDirection) {
        Log.e("TAG", "onLayoutChild");
        return super.onLayoutChild(parent, child, layoutDirection);
    }
}
