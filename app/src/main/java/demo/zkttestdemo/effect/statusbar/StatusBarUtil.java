package demo.zkttestdemo.effect.statusbar;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by zkt on 2018-1-31.
 */

public class StatusBarUtil {

    /**
     * 为activity的状态栏设置颜色
     *
     * @param activity
     * @param color
     */
    public static void setStatusBarColor(Activity activity, int color) {
        // 5.0 以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
        // 4.4 ~ 5.0之间,采用一个技巧，首先弄成全屏，在状态栏的部分加一个布局
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //这个会把状态栏隐藏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            View view = new View(activity);
            view.setBackgroundColor(color);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity)));

            //DecorView是FrameLayout
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(view);
            // 方式一
            ViewGroup contentView = activity.findViewById(android.R.id.content);
            contentView.setPadding(0, getStatusBarHeight(activity), 0, 0);

            // 方式二
            // View activityView = contentView.getChildAt(0);
            // activityView.setFitsSystemWindows(true);
            // activityView.setPadding(0, getStatusBarHeight(activity), 0, 0);
        }
    }

    /**
     * 设置Activity全屏
     *
     * @param activity
     */
    public static void setActivityTranslucent(Activity activity) {
        // 5.0 以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        // 4.4 ~ 5.0之间
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static int getStatusBarHeight(Activity activity) {
        //插件式换肤，怎么获取资源的，先获取资源id，根据id获取资源
        Resources resources = activity.getResources();
        int statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelOffset(statusBarHeightId);
    }

}
