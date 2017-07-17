package demo.zkttestdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zkt on 2017/6/23.
 */

public class DisplayUtil {
    private static Map<ScreenEnum, Integer> screenMap = new HashMap<>();

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = activity.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }


    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(float pxValue, Activity activity) {
        return (int) (pxValue / getScreenMsg(activity).get(ScreenEnum.Density) + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @return
     */
    public static int dip2px(float dipValue, Activity activity) {
        return (int) (dipValue * getScreenMsg(activity).get(ScreenEnum.Density) + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @return
     */
    public static int px2sp(float pxValue, Activity activity) {
        return (int) (pxValue / getScreenMsg(activity).get(ScreenEnum.ScaledDensity) + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @return
     */
    public static int sp2px(float spValue, Activity activity) {
        return (int) (spValue * getScreenMsg(activity).get(ScreenEnum.ScaledDensity) + 0.5f);
    }

    /**
     * 获取屏幕尺寸等信息
     */
    public static Map<ScreenEnum, Integer> getScreenMsg(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        float density = metric.density;///屏幕密度（0.75, 1.0 . 1.5）
        int densityDpi = metric.densityDpi;///屏幕密度DPI（120/160/240/320/480）
        float scaledDensity = metric.scaledDensity;
        if (screenMap == null)
            screenMap = new HashMap<>();

        screenMap.clear();
        screenMap.put(ScreenEnum.Width, width);
        screenMap.put(ScreenEnum.Height, height);
        screenMap.put(ScreenEnum.Density, (int) density);
        screenMap.put(ScreenEnum.DendityDpi, densityDpi);
        screenMap.put(ScreenEnum.ScaledDensity, (int) scaledDensity);
        return screenMap;
    }

    enum ScreenEnum {
        Width, Height, Density, DendityDpi, ScaledDensity
    }
}
