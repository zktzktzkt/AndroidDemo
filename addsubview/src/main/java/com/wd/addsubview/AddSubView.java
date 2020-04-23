package com.wd.addsubview;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zkt on 2017-12-18.
 */

public class AddSubView extends LinearLayout implements View.OnClickListener {
    private ImageView iv_sub;
    private ImageView iv_add;
    private TextView tv_value;
    private int value = 1; //默认值
    private int minValue = 1;
    private int maxValue = 10;

    public AddSubView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.add_sub_view, this);
        iv_sub = findViewById(R.id.iv_sub);
        tv_value = findViewById(R.id.tv_value);
        iv_add = findViewById(R.id.iv_add);

        //设置点击事件
        iv_sub.setOnClickListener(this);
        iv_add.setOnClickListener(this);

        int value = getValue();
        setValue(value);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_sub) { //减
            subNumber();
        } else if (v.getId() == R.id.iv_add) { //加
            addNumber();
        }
    }

    private void subNumber() {
        if (value > minValue) {
            value--;
        }
        setValue(value);

        if (null != onNumberChangeListener) {
            onNumberChangeListener.onNumberChange(value);
        }
    }

    private void addNumber() {
        if (value < maxValue) {
            value++;
        }
        setValue(value);

        if (null != onNumberChangeListener) {
            onNumberChangeListener.onNumberChange(value);
        }
    }

    public int getValue() {
        String valueStr = tv_value.getText().toString().trim();
        if (!TextUtils.isEmpty(valueStr)) {
            value = Integer.parseInt(valueStr);
        }
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        tv_value.setText(value + "");
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }


    public interface OnNumberChangeListener {
        /**
         * 当数据发生变化时回调
         *
         * @param value
         */
        void onNumberChange(int value);
    }

    OnNumberChangeListener onNumberChangeListener;

    public void setOnNumberChangeListener(OnNumberChangeListener onNumberChangeListener) {
        this.onNumberChangeListener = onNumberChangeListener;
    }
}
