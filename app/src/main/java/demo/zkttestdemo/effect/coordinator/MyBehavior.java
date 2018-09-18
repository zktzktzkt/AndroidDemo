package demo.zkttestdemo.effect.coordinator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

/**
 * Created by zkt on 2017-11-10.
 */

public class MyBehavior extends CoordinatorLayout.Behavior<Button> {
    private final int widthPixels;

    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        widthPixels = displayMetrics.widthPixels;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, Button child, View dependency) {
        return dependency instanceof TempView;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, Button child, View dependency) {

        //根据dependency的位置，设置button的位置
        int top = dependency.getTop();
        int left = dependency.getLeft();

        int x = widthPixels - left - child.getWidth();
        int y = top;

        setPosition(child, x, y);

        return true;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                  @NonNull Button child, @NonNull View target,
                                  int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);


    }

    private void setPosition(View v, int x, int y) {
        CoordinatorLayout.MarginLayoutParams layoutParams = (CoordinatorLayout.MarginLayoutParams) v.getLayoutParams();
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        v.setLayoutParams(layoutParams);
    }
}
