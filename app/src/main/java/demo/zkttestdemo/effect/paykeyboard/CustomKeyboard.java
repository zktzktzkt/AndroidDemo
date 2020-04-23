package demo.zkttestdemo.effect.paykeyboard;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2017/5/29.
 * 自定义键盘
 */

public class CustomKeyboard extends LinearLayout implements View.OnClickListener {
    public CustomKeyboard(Context context) {
        this(context, null);
    }

    public CustomKeyboard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomKeyboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //直接加载布局
        inflate(context, R.layout.ui_custom_keyboard, this);

        setItemClickListener(this);
    }

    /**
     * 设置子view的点击事件
     */
    private void setItemClickListener(View view) {
        //遍历，递归，给每个view设置点击事件。
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = viewGroup.getChildAt(i);
                setItemClickListener(childView);
            }
        } else {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            //点击是文本
            String number = ((TextView) v).getText().toString().trim();
            if (mListener != null && !TextUtils.isEmpty(number)) {
                mListener.click(number);
            }
        }

        if (v instanceof ImageView) {
            //点击的是删除
            if (mListener != null) {
                mListener.delete();
            }
        }
    }


    CustomKeyboardClickListener mListener;

    /**
     * 设置点击回调监听
     */
    public void setOnCustomKeyboardClickListener(CustomKeyboardClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 点击键盘的回调监听
     */
    public interface CustomKeyboardClickListener {
        void click(String number);

        void delete();
    }

}
