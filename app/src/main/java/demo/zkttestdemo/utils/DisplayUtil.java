package demo.zkttestdemo.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zkt on 2017/6/23.
 */

public class DisplayUtil {
    private static Map<ScreenEnum, Integer> screenMap = new HashMap<>();

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
