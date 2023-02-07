package demo.zkttestdemo.effect.coordinator.jdBehavior;

import android.view.View;
import android.widget.TextView;

/**
 * Header Log Utils
 *
 * @author duanwenqiang1
 * @date 2018/6/5
 */
public final class Utils {

    /**
     * 获取view高度
     * @param view
     * @return
     */
    public static int getViewHeight(View view) {
        if (view != null && view.isShown()) {
            return view.getMeasuredHeight();
        }
        return 0;
    }

    /**
     * log 获取view名字
     * @param view
     * @return
     */
    public static String viewName(View view) {
        if (view == null) {
            return "view is null !!";
        }
        if (view instanceof TextView) {
            return ((TextView)view).getText().toString().trim();
        }
        return view.getClass().getSimpleName();
    }

}
