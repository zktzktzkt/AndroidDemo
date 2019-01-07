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
     * 流程：解析xml -> 创建View，并传入对应的属性 -> 添加View到ViewGroup
     * （1）先createViewFromTag；
     * （2）createViewFromTag中，会回调Factory2#onCreateView，创建View；
     * （3）把View添加进xml布局的根ViewGroup中；
     */

    /**
     * 1. 此时View已经创建完。
     * 在addView之前，会先回调generateLayoutParams，把xml中的属性转换成可调用的java对象
     * 注意：LayoutParams是给子View用的，不是给ViewGroup本身用
     *
     * @param attrs 子View的属性
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new AnimatorLayoutParams(getContext(), attrs);
    }

    /**
     * 2. generateLayoutParams后，ViewGroup会把view添加到其中，并把params传入，回调该方法
     * 在addview的时候，换成自己的View
     */
    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (!isDiscroll(params)) {
            super.addView(child, params);
        } else {
            AnimatorFrameLayout frameLayout = new AnimatorFrameLayout(getContext());
            AnimatorLayoutParams layoutParams = (AnimatorLayoutParams) params;
            frameLayout.setDiscrollAlpha(layoutParams.mDiscrollveAlpha);
            frameLayout.setDiscrollFromBgColor(layoutParams.mDiscrollveFromBgColor);
            frameLayout.setDiscrollScaleX(layoutParams.mDiscrollveScaleX);
            frameLayout.setDiscrollScaleY(layoutParams.mDiscrollveScaleY);
            frameLayout.setDiscrollTranslation(layoutParams.mDisCrollveTranslation);
            frameLayout.setDiscrollToBgColor(layoutParams.mDiscrollveToBgColor);
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
