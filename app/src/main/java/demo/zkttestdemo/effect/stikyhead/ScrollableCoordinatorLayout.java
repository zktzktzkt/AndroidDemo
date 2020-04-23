package demo.zkttestdemo.effect.stikyhead;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Frank on 2016/11/24.
 */

public class ScrollableCoordinatorLayout extends CoordinatorLayout {

    private boolean allowForScroll = false;

    public ScrollableCoordinatorLayout(Context context) {
        super(context);
    }

    public ScrollableCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return allowForScroll && super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    public boolean isallowForScroll() {
        return allowForScroll;
    }

    public void setallowForScroll(boolean allowForScroll) {
        this.allowForScroll = allowForScroll;
    }
}
