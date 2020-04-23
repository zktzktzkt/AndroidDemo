package demo.zkttestdemo.effect.keyboardbug;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import demo.zkttestdemo.kpswitch.IPanelHeightTarget;

/**
 * Created by zkt on 2017/9/19.
 */

public class MyRelativeLayout extends RelativeLayout implements IPanelHeightTarget{
    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void refreshHeight(int panelHeight) {

    }

    @Override
    public void onKeyboardShowing(boolean showing) {

    }
}
