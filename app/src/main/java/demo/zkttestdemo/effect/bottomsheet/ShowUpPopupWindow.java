package demo.zkttestdemo.effect.bottomsheet;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by zkt on 2017/7/14.
 */

public class ShowUpPopupWindow extends PopupWindow {

    public ShowUpPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height, true);

        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
    }


    /**
     * 在指定控件上方显示，默认x座标与指定控件的中点x座标相同
     *
     * @param anchor
     * @param xoff
     * @param yoff
     */
    public void showAsPullUp(View anchor, int viewHeight, int xoff, int yoff) {
        //保存anchor在屏幕中的位置
        int[] location = new int[2];
        //保存anchor上部中点
        int[] anchorCenter = new int[2];
        //读取位置anchor座标
        anchor.getLocationOnScreen(location);
        //计算anchor中点
        anchorCenter[0] = location[0];
        anchorCenter[1] = location[1];
        super.showAtLocation(anchor,
                Gravity.TOP | Gravity.LEFT,
                anchorCenter[0] + xoff,
                anchorCenter[1] - viewHeight + yoff);
        //anchor.getContext().getResources().getDimensionPixelSize(R.dimen.popup_upload_height)

    }
}
