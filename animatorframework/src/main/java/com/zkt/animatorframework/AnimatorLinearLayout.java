package com.zkt.animatorframework;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by zkt on 2018-10-7.
 * Description:
 */

public class AnimatorLinearLayout extends LinearLayout {

    public AnimatorLinearLayout(Context context) {
        this(context, null);
    }

    public AnimatorLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimatorLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);
    }

    /**
     * 1. 解析属性
     * LayoutParams 的作用就是把xml中的属性解析到了LayoutParams中，这样就能在java代码中调用xml中的属性值
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new AnimatorLayoutParams(getContext(), attrs);
    }

    /**
     * 2. 偷天换日
     * 在addview的时候，换成自己的View
     */
    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (!isDiscroll(params)) {
            super.addView(child, params);
        } else {
            AnimatorFrameLayout frameLayout = new AnimatorFrameLayout(getContext());
            AnimatorLayoutParams layoutParams = (AnimatorLayoutParams) params;
            frameLayout.setmDiscrollveAlpha(layoutParams.mDiscrollveAlpha);
            frameLayout.setmDiscrollveFromBgColor(layoutParams.mDiscrollveFromBgColor);
            frameLayout.setmDiscrollveScaleX(layoutParams.mDiscrollveScaleX);
            frameLayout.setmDiscrollveScaleY(layoutParams.mDiscrollveScaleY);
            frameLayout.setmDisCrollveTranslation(layoutParams.mDisCrollveTranslation);
            frameLayout.setmDiscrollveToBgColor(layoutParams.mDiscrollveToBgColor);
            frameLayout.addView(child);

            //添加自己的view
            super.addView(frameLayout, layoutParams);
        }

    }

    private boolean isDiscroll(ViewGroup.LayoutParams params) {
        AnimatorLayoutParams layoutParams = (AnimatorLayoutParams) params;
        return layoutParams.mDiscrollveAlpha ||
                layoutParams.mDiscrollveScaleX ||
                layoutParams.mDiscrollveScaleY ||
                layoutParams.mDisCrollveTranslation != -1 ||
                (layoutParams.mDiscrollveFromBgColor != -1 && layoutParams.mDiscrollveToBgColor != -1);
    }

    public class AnimatorLayoutParams extends LinearLayout.LayoutParams {
        public boolean mDiscrollveAlpha;
        public boolean mDiscrollveScaleX;
        public boolean mDiscrollveScaleY;
        public int mDisCrollveTranslation;
        public int mDiscrollveFromBgColor;
        public int mDiscrollveToBgColor;

        public AnimatorLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.DiscrollView_LayoutParams);
            mDiscrollveAlpha = a.getBoolean(R.styleable.DiscrollView_LayoutParams_discrollve_alpha, false);
            mDiscrollveScaleX = a.getBoolean(R.styleable.DiscrollView_LayoutParams_discrollve_scaleX, false);
            mDiscrollveScaleY = a.getBoolean(R.styleable.DiscrollView_LayoutParams_discrollve_scaleY, false);
            mDisCrollveTranslation = a.getInt(R.styleable.DiscrollView_LayoutParams_discrollve_translation, -1);
            mDiscrollveFromBgColor = a.getColor(R.styleable.DiscrollView_LayoutParams_discrollve_fromBgColor, -1);
            mDiscrollveToBgColor = a.getColor(R.styleable.DiscrollView_LayoutParams_discrollve_toBgColor, -1);
            a.recycle();
        }

    }

}
