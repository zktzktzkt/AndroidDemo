package demo.zkttestdemo.effect.coordinator.jdBehavior;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.view.ViewConfiguration;


/**
 * recyclerview fling工具类
 * @author xuekai1
 * @date 2018/10/17
 */
public class FlingUtil {

    /**
     * 通过速度，计算recyclerview可fling的距离。通过源码逆推出来的公式。
     * @param velocityY 速度
     * @param context 上下文
     * @return 可滑行的距离
     */
    public static double getDis(int velocityY, Context context) {

        //TODO 注意隐私合规，先暂时这么写，如果没同意隐私，需要给density赋值1.0f 不要直接获取系统参数
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float density = displayMetrics.density;

        double g = Math.log(0.35f * Math.abs(velocityY) / (ViewConfiguration.getScrollFriction() * SensorManager.GRAVITY_EARTH
                * 39.37f // inch/meter
                * (density * 160.0f)
                * 0.84f));
        final double decelMinusOne = (float) (Math.log(0.78) / Math.log(0.9)) - 1.0;
        return ViewConfiguration.getScrollFriction() * (SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.37f // inch/meter
                * (density * 160.0f)
                * 0.84f) * Math.exp((float) (Math.log(0.78) / Math.log(0.9)) / decelMinusOne * g);
    }

}
