package demo.zkttestdemo.effect.screenmatch;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by zkt on 2018-9-17.
 * Description: 屏幕适配工具类
 */

public class UIUtil {
    private static UIUtil utils;
    public final float STANDARD_WIDTH = 750;
    public final float STANDARD_HEIGHT = 1334;
    private Context mContext;
    private String DIMEN_CLASS = "com.android.internal.R$dimen";

    //实际设备宽高
    private float displayMetricsWidth;
    private float displayMetricsHeight;

    public static UIUtil getInstance(Context context) {
        if (utils == null) {
            utils = new UIUtil(context);
        }
        return utils;
    }

    private UIUtil(Context context) {
        this.mContext = context;

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //加载当前界面信息
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        if (displayMetricsWidth == 0.0f || displayMetricsHeight == 0.0f) {
            //获取状态栏信息
            int systemBarHeight = getValue(context, "system_bar_height", 48);

            if (displayMetrics.widthPixels > displayMetrics.heightPixels) {
                this.displayMetricsWidth = displayMetrics.heightPixels;
                this.displayMetricsHeight = displayMetrics.widthPixels - systemBarHeight;
            } else {
                this.displayMetricsWidth = displayMetrics.widthPixels;
                this.displayMetricsHeight = displayMetrics.heightPixels - systemBarHeight;
            }
        }
    }

    private int getValue(Context context, String systemId, int defValue) {
        try {
            Class<?> clazz = Class.forName(DIMEN_CLASS);
            Object r = clazz.newInstance();
            Field field = clazz.getField(systemId);
            int x = (int) field.get(r);
            return context.getResources().getDimensionPixelOffset(x);

        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }

    //对外提供系数
    public float getHorizontalScaleValue() {
        return displayMetricsWidth / STANDARD_WIDTH;
    }

    //对外提供系数
    public float getVerticalScaleValue() {
        return displayMetricsHeight / STANDARD_HEIGHT;
    }

}
