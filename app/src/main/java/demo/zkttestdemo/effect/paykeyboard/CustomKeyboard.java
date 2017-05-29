package demo.zkttestdemo.effect.paykeyboard;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2017/5/29.
 * 自定义键盘
 */

public class CustomKeyboard extends LinearLayout {
    public CustomKeyboard(Context context) {
        this(context, null);
    }

    public CustomKeyboard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomKeyboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //直接加载布局
        inflate(context, R.layout.ui_custom_keyboard , this);
    }


}
