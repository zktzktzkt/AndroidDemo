package demo.zkttestdemo.privacy;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 京东用户隐私权限处理
 * Add by: wanggang85
 * Date: 2020/6/19
 */
public class JDPrivacyHelper {

    private static final String SP_NAME = "privacy";
    volatile public static boolean sIsAcceptPrivacy;

    /**
     * 用户是否同意隐私协议
     *
     * @param context
     * @return true 已经同意  false 未同意
     */
    public static synchronized boolean isAcceptPrivacy(Context context) {
        if (context == null) return false;
        if (!sIsAcceptPrivacy) {
            SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            sIsAcceptPrivacy = sp.getBoolean("privacy_has_show", false);
        }
        return sIsAcceptPrivacy;
    }

    /**
     * 是否点击了同意或不同意的按钮
     */
    public static void setClickedPrivacyButton(boolean isClicked) {
        SharedPreferences sp = JdSdk.getInstance().getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean("privacy_has_clicked", isClicked).apply();
    }

    /**
     * 是否点击了同意或不同意的按钮
     */
    public static boolean isClickedPrivacyButton() {
        SharedPreferences sp = JdSdk.getInstance().getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        boolean isClicked = sp.getBoolean("privacy_has_clicked", false);
        return isClicked;
    }
}
