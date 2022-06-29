package demo.zkttestdemo.privacy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;
import demo.zkttestdemo.privacy.interfaces.PrivacyObservable;
import demo.zkttestdemo.privacy.interfaces.PrivacyObserver;

/**
 * 隐私弹窗管理类
 */
public class JDPrivacyManager implements PrivacyObservable {

    private static final String TAG = JDPrivacyManager.class.getSimpleName();
    private static final String PAGE_ID = "Privacy_Policy";
    private Activity mCurrentActivity;
    private static PrivacyCallback sPrivacyCallback;
    private Dialog mPrivacyDialog;
//    private JDDialog mJdOnceDialog;
//    private JDDialog mJdTwiceDialog;
    public static boolean privacyAgreed = false;
    private static boolean sFromOpenApp;
    // private static boolean sHasReportPvMta = false;
    private Activity mActivity;
    private final List<PrivacyObserver> observerList = new ArrayList<>();

    @Override
    public void addObserver(PrivacyObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(PrivacyObserver observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifiObservers() {
        for (PrivacyObserver abstractObserver : observerList) {
            abstractObserver.agreePrivacy();
        }
    }

    private void onError() {
        if (null == sPrivacyCallback) {
            return;
        }
        sPrivacyCallback.onClose(JDPrivacyHelper.isAcceptPrivacy(mCurrentActivity));
        release();
    }

    /**
     * 请求隐私弹窗
     */
    public void openPrivacyDialog(boolean fromOpenApp, Activity activity, PrivacyCallback callback) {
        try {
            this.mActivity = activity;
            openPrivacyDialogSafe(fromOpenApp, activity, callback);
        } catch (Exception e) {
            onError();
        }
    }

    private void openPrivacyDialogSafe(boolean fromOpenApp, Activity activity, PrivacyCallback callback) {
        if (null == activity || null == callback) {
            return;
        }
        sFromOpenApp = fromOpenApp;
        if (!JDPrivacyHelper.isAcceptPrivacy(activity)) {
            if (null != sPrivacyCallback) {
                sPrivacyCallback.onDismiss();
            }
            dismissDialog(mPrivacyDialog);
            sPrivacyCallback = callback;
            mCurrentActivity = activity;
            mPrivacyDialog = createPrivacyDialog(activity);
            Window window = mPrivacyDialog.getWindow();
            if (null == window) {
                onError();
                return;
            }
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = getDpi750Width(activity, 590);
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
            mPrivacyDialog.show();
            /*if (!sHasReportPvMta) {
                sHasReportPvMta = true;
                JDMtaUtils.sendPagePv(JdSdk.getInstance().getApplicationContext(), this, sFromOpenApp ? "1" : "0", "Privacy_Policy", "");
            }*/
        } else {
            sPrivacyCallback = callback;
            onClose(true);
        }
    }

    /**
     * 创建Dialog
     */
    private Dialog createPrivacyDialog(Activity activity) {
        Dialog dialog = new Dialog(activity, R.style.privacy_dialog);
        View contentView = generateDialogView(activity);

        ((TextView) contentView.findViewById(R.id.privacy_title)).setText(activity.getString(R.string.privacy_txt_title_new));
        ((TextView) contentView.findViewById(R.id.privacy_message)).setText(getPrivacyTopMessage(activity));
        ((TextView) contentView.findViewById(R.id.privacy_bottom_content)).setText(getPrivacyBottomMessage(activity));
        dialog.setContentView(contentView);
        dialog.setCancelable(false);
        setDialogCallback(dialog);
        return dialog;
    }

    private static View generateDialogView(Activity activity) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(getDpi750Width(activity, 23));
        drawable.setColor(Color.WHITE);

        RelativeLayout root = new RelativeLayout(activity);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setBackgroundDrawable(drawable);

        /* 标题 */
        TextView title = new TextView(activity);
        title.setId(R.id.privacy_title);
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDpi750Width(activity, 32));
        title.setTextColor(0xff2e2d2d);
        title.setGravity(Gravity.CENTER);
        title.getPaint().setFakeBoldText(true);
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getDpi750Width(activity, 144));
        root.addView(title, titleParams);

        /* 一级文案 */
        TextView message = new TextView(activity);
        message.setTextColor(0xff1a1a1a);
        message.setId(R.id.privacy_message);
        //        message.setScrollbarFadingEnabled(false);
        message.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDpi750Width(activity, 26));
        message.setLineSpacing(10, 1);
        message.setMovementMethod(LinkMovementMethod.getInstance());
        RelativeLayout.LayoutParams messageParams = new RelativeLayout.LayoutParams(getDpi750Width(activity, 494), getDpi750Width(activity, 440));
        messageParams.topMargin = getDpi750Width(activity, 104);
        messageParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        root.addView(message, messageParams);


        View view = LayoutInflater.from(activity).inflate(R.layout.layout_privacy_btns, root, false);
        //LinearLayout ll_btmMessage = view.findViewById(R.id.ll_btmMessage);
        //CheckBox cb_protocal = view.findViewById(R.id.cb_protocal);
        TextView privacy_bottom_content = view.findViewById(R.id.privacy_bottom_content);
        TextView privacy_bottom_deny = view.findViewById(R.id.privacy_bottom_deny);
        TextView privacy_bottom_agree = view.findViewById(R.id.privacy_bottom_agree);

        LinearLayout.LayoutParams bottomParams = (LinearLayout.LayoutParams) privacy_bottom_content.getLayoutParams();
        bottomParams.width = getDpi750Width(activity, 494);
        bottomParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        privacy_bottom_content.setLayoutParams(bottomParams);

        /* 二级文案 */
        privacy_bottom_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDpi750Width(activity, 22));
        privacy_bottom_content.setLineSpacing(8, 1);
        privacy_bottom_content.setMovementMethod(LinkMovementMethod.getInstance());
        privacy_bottom_content.setPadding(0, getDpi750Width(activity, 18), 0, getDpi750Width(activity, 40));

        /* 俩按钮 */
        privacy_bottom_deny.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDpi750Width(activity, 28));
        privacy_bottom_agree.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDpi750Width(activity, 28));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.privacy_message);
        params.topMargin = getDpi750Width(activity, 10);

        root.addView(view, params);

        return root;
    }

    private SpannableString getPrivacyTopMessage(Activity activity) {
        String tipsStr = activity.getString(R.string.privacy_txt_content_new);
        SpannableString ss = new SpannableString(tipsStr);
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NotNull View widget) {
                startWebViewFragment("京东用户注册协议", "Configuration.getProtocalRegisterUrl()");
                //DeepLinkMHelper.startWebActivity(activity, Configuration.getProtocalRegisterUrl());
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.argb(0xff, 0xf0, 0x2b, 0x2b));
                ds.setFakeBoldText(true);
            }
        }, 73, 83, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private SpannableString getPrivacyBottomMessage(Activity activity) {
        String tipsStr = activity.getString(R.string.privacy_txt_bottom_new);
        SpannableString ss = new SpannableString(tipsStr);
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NotNull View widget) {
                startWebViewFragment("京东京车会隐私政策", "Configuration.getProtocalRegisterUrl()");
                //DeepLinkMHelper.startWebActivity(activity, Configuration.getProtocalPrivacyUrl());
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.argb(0xff, 0xf0, 0x2b, 0x2b));
                ds.setFakeBoldText(true);
            }
        }, 15, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /*private SpannableString getPrivacyOnceDetainMessage(Activity activity) {
        String tipsStr = activity.getString(R.string.privacy_txt_detain_once);
        SpannableString ss = new SpannableString(tipsStr);
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NotNull View widget) {
                ToastUtils.showShort("跳转webview查看隐私");
//                startWebViewFragment("京东京车会隐私政策", Configuration.getProtocalPrivacyUrl());
                //DeepLinkMHelper.startWebActivity(activity, Configuration.getProtocalPrivacyUrl());
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.argb(0xff, 0xf0, 0x2b, 0x2b));
                ds.setFakeBoldText(true);
            }
        }, 22, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // [22,33)
        return ss;
    }*/

    /*private SpannableString getPrivacyTwiceDetainMessage(Activity activity) {
        String tipsStr = activity.getString(R.string.privacy_txt_detain_twice);
        SpannableString ss = new SpannableString(tipsStr);
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NotNull View widget) {
                startWebViewFragment("京东京车会隐私政策", Configuration.getProtocalPrivacyUrl());
                //DeepLinkMHelper.startWebActivity(activity, Configuration.getProtocalPrivacyUrl());
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.argb(0xff, 0xf0, 0x2b, 0x2b));
                ds.setFakeBoldText(true);
            }
        }, 6, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }*/

    private void setDialogCallback(final Dialog dialog) {
        //同意回调
        dialog.findViewById(R.id.privacy_bottom_agree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog(dialog);
                onClose(true);
                //同意了隐私 发广播
                sendAgreeBroadcast();
                sendMta("PrivacyPolicy_Agree");
            }
        });
        //拒绝回调
        dialog.findViewById(R.id.privacy_bottom_deny).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog(dialog);
                onClose(false);
                //发不同意隐私的广播
                sendNotAgreeBroadcast();
//                showDetainOnceDialog();
                sendMta("PrivacyPolicy_DoNotAgree");
            }
        });
    }

    /**
     * 拒绝后的第一次弹窗
     */
    /*private void showDetainOnceDialog() {
        try {
            showDetainOnceDialogSafe();
        } catch (Exception e) {
            onError();
        }
    }*/

    /**
     * 第一次拒绝后显示的弹窗
     */
    /*private void showDetainOnceDialogSafe() {
        dismissAll();
        mJdOnceDialog = JDDialogFactory.getInstance().createJdDialogWithStyle6(mCurrentActivity, "不同意隐私政策仅能使用部分功能", getPrivacyOnceDetainMessage(mCurrentActivity), "仍不同意", "同意");
        mJdOnceDialog.setOnLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog(mJdTwiceDialog);
                onClose(false);
                //发不同意隐私的广播
                sendNotAgreeBroadcast();
                sendMta("PrivacyPolicy_DoNotAgree1");
            }
        });

        mJdOnceDialog.setOnRightButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog(mJdOnceDialog);
                onClose(true);
                //同意了隐私 发广播
                sendAgreeBroadcast();
                sendMta("PrivacyPolicy_Agree1");
            }
        });
        mJdOnceDialog.messageView.setMovementMethod(new LinkMovementMethod());
        mJdOnceDialog.setCancelable(false);
        mJdOnceDialog.show();
    }*/

    /**
     * 发送广播（同意了隐私）
     */
    private void sendAgreeBroadcast() {
        Intent intent = new Intent(JDBroadcastConstant.ACTION_PRIVACY_AGREE);
        LocalBroadcastManager.getInstance(JdSdk.getInstance().getApplicationContext()).sendBroadcast(intent);
    }

    /**
     * 发送广播（不同意隐私）
     */
    private void sendNotAgreeBroadcast() {
        Intent intent = new Intent(JDBroadcastConstant.ACTION_PRIVACY_NOT_AGREE);
        LocalBroadcastManager.getInstance(JdSdk.getInstance().getApplicationContext()).sendBroadcast(intent);
    }

//    private void dismissAll() {
//        dismissDialog(mJdOnceDialog);
//        dismissDialog(mJdTwiceDialog);
//    }

    private void dismissDialog(Dialog dialog) {
        try {
            if (null != dialog && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否同意了隐私协议
     */
    private void onClose(boolean isAgree) {
//        JDMaInterface.setModeTag(isAgree ? "0" : "2");
        savePrivacy(isAgree);

        if (null != sPrivacyCallback) {
            sPrivacyCallback.onClose(isAgree);
        }
        if (isAgree) {
            notifiObservers();
        }
        if (!JDPrivacyHelper.isClickedPrivacyButton()) {
            JDPrivacyHelper.setClickedPrivacyButton(true);
        }
//        DeviceFingerUtil.fpInit(isAgree);
//        if(isAgree) {
//            JMA.forceRefresh(JdSdk.getInstance().getApplicationContext());
//        }
        release();
    }

    private void release() {
        try {
            releaseSafe();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void releaseSafe() {
//        dismissAll();
        dismissDialog(mPrivacyDialog);
        sPrivacyCallback = null;
        mPrivacyDialog = null;
//        mJdOnceDialog = null;
//        mJdTwiceDialog = null;
        mCurrentActivity = null;
    }

    private void startWebViewFragment(String title, String url) {
        if (null == mCurrentActivity) {
            return;
        }
       /* Intent intent = new Intent(mCurrentActivity, PrivacyWebActivity.class);
        Bundle b = new Bundle();
        //b.putString("title", mCurrentActivity.getString(R.string.privacy_txt_title_new));
        b.putString("title", title);
        b.putString("url", url);
        b.putBoolean("isIgnoreShare", true);
        b.putBoolean("isShowMoreBtn", false);
        intent.putExtras(b);
        mCurrentActivity.startActivity(intent);*/
    }

    private void sendMta(String eventId) {
        sendMta(eventId, "");
    }

    private void sendMta(String eventId, String jsonParam) {
        //FloorMaiDianCtrl.sendClickDataWithExt(eventId, "", jsonParam, PAGE_ID);
    }

    private static int getDpi750Width(Activity activity, int value) {
        int appWidth = Math.min(ScreenUtils.getScreenWidth(), 1600);
        return (int) ((float) (appWidth * value) / (float) 750 + 0.5F);
    }

    public void savePrivacy(boolean isAgree) {
        JDPrivacyHelper.sIsAcceptPrivacy = isAgree;
        privacyAgreed = isAgree;
//        JDMtaUtils.acceptPrivacyProtocol(isAgree);
        SharedPreferences sp = JdSdk.getInstance().getApplication().getSharedPreferences("privacy", Context.MODE_PRIVATE);
        sp.edit().putBoolean("privacy_has_show", isAgree).commit();
    }

    public interface PrivacyCallback {
        void onClose(boolean isAgree);

        void onDismiss();
    }
}
