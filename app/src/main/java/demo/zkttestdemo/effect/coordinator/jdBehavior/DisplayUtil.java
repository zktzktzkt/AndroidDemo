package demo.zkttestdemo.effect.coordinator.jdBehavior;

import android.app.Activity;
import android.os.Build;

import demo.zkttestdemo.privacy.JdSdk;


/**
 * 【系统展示，单位换算相关工具类】
 *
 * @author mengfanlei
 * @date 2018/12/27
 */
public class DisplayUtil {

    /**
     * 缓存width，防止每次计算。onConfigChange之后需要重新赋值
     */
    private static int width = -1;
    /**
     * 缓存height，防止每次计算。onConfigChange之后需要重新赋值
     */
    private static int height = -1;

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth() {
        return width;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight() {
        return height;
    }


    /**
     * 是否华为9及以下系统
     * */
    public static boolean isMateXLessThanAndQ() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.P;
    }

}

