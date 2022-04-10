package demo.zkttestdemo.effect.nestedscroll.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewBehavior extends CoordinatorLayout.Behavior<RecyclerView> {

    private static final String TAG = "RecyclerViewBehavior";

    public RecyclerViewBehavior() {
    }

    public RecyclerViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RecyclerView child, View dependency) {
        return dependency instanceof TextView;
    }

    /**
     *
     * 知识点：当View树构建后会回调此方法，可以在里面进行上下布局
     *
     * @param child 写在了哪个View上，哪个就是child
     * @param dependency  layoutDependsOn所依赖的View
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RecyclerView child, View dependency) {
        //计算列表y坐标，最小为0
        float y = dependency.getHeight() + dependency.getTranslationY();
        if (y < 0) {
            y = 0;
        }
        Log.e(TAG, "onDependentViewChanged-> setY:" + y);
        child.setY(y);
        return true;
    }
}
