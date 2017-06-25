package demo.zkttestdemo.effect.bottomsheet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import demo.zkttestdemo.R;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by zkt on 2017/6/25.
 */

public class SlideFromBottomPopup extends BasePopupWindow {
    private View popupView;
    public int mHeight;
    Activity mActivity;

    public SlideFromBottomPopup(Activity context) {
        super(context);
        this.mActivity = context;
    }

    @Override
    protected Animation initShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    /**
     * 点击蒙板dismiss
     */
    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    /**
     * popupwindow的布局
     */
    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_slide_from_bottom, null);
        return popupView;
    }

    /**
     * 动画作用于哪个View
     */
    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

}
