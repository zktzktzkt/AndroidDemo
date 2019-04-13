package demo.zkttestdemo.effect.behavior.custom;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zkt on 19/02/11.
 * Description:
 */
public class Behavior {

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public void onSizeChanged(View parent, View child, int w, int h, int oldw, int oldh) {

    }

    public void onLayoutFinish(View parent, View child) {

    }

    public void onNestedScroll(View parent, View child, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }
}
