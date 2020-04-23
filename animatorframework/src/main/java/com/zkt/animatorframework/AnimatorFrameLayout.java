package com.zkt.animatorframework;

import android.animation.ArgbEvaluator;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by zkt on 2018-10-7.
 * Description:
 */

public class AnimatorFrameLayout extends FrameLayout implements DiscrollInterface {
    /**
     * <attr name="discrollve_translation">
     *   <flag name="fromTop" value="0x01" />
     *   <flag name="fromBottom" value="0x02" />
     *   <flag name="fromLeft" value="0x04" />
     *   <flag name="fromRight" value="0x08" />
     * </attr>
     * 0000000001
     * 0000000010
     * 0000000100
     * 0000001000
     * top|left
     * 0000000001 top
     * 0000000100 left 或运算 |
     * 0000000101
     * 反过来就使用& 与运算
     */
    //保存自定义属性
    //定义很多的自定义属性
    private static final int TRANSLATION_TO_TOP = 0x01;
    private static final int TRANSLATION_TO_BOTTOM = 0x02;
    private static final int TRANSLATION_TO_LEFT = 0x04;
    private static final int TRANSLATION_TO_RIGHT = 0x08;

    //颜色估值器
    private static ArgbEvaluator sArgbEvaluator = new ArgbEvaluator();
    /**
     * 自定义属性的一些接收的变量
     */
    private int mDiscrollFromBgColor;//背景颜色变化开始值
    private int mDiscrollToBgColor;//背景颜色变化结束值
    private boolean mDiscrollAlpha;//是否需要透明度动画
    private int mDiscrollTranslation;//平移值
    private boolean mDiscrollScaleX;//是否需要x轴方向缩放
    private boolean mDiscrollScaleY;//是否需要y轴方向缩放
    private int mHeight;//本view的高度
    private int mWidth;//宽度

    public AnimatorFrameLayout(@NonNull Context context) {
        super(context);
    }

    public AnimatorFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatorFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public void onDiscroll(float ratio) {
        if (mDiscrollAlpha) {
            setAlpha(ratio);
        }
        if (mDiscrollScaleX) {
            setScaleX(ratio);
        }
        if (mDiscrollScaleY) {
            setScaleY(ratio);
        }
        //平移动画，有四个方向，这四个方向被我们整成了一个属性接收
        if (isTranslationFrom(TRANSLATION_TO_BOTTOM)) {
            setTranslationY(mHeight * (1 - ratio));
        }
        if (isTranslationFrom(TRANSLATION_TO_TOP)) {
            setTranslationY(-mHeight * (1 - ratio));
        }
        if (isTranslationFrom(TRANSLATION_TO_LEFT)) {
            setTranslationX(-mWidth * (1 - ratio));
        }
        if (isTranslationFrom(TRANSLATION_TO_RIGHT)) {
            setTranslationX(mWidth * (1 - ratio));
        }
        if (mDiscrollFromBgColor != -1 && mDiscrollToBgColor != -1) {
            setBackgroundColor((Integer) sArgbEvaluator.evaluate(ratio, mDiscrollFromBgColor, mDiscrollToBgColor));
        }
    }

    @Override
    public void onResetDiscroll() {
        if (mDiscrollAlpha) {
            setAlpha(0);
        }
        if (mDiscrollScaleX) {
            setScaleX(0);
        }
        if (mDiscrollScaleY) {
            setScaleY(0);
        }
        //平移动画，有四个方向，这四个方向被我们整成了一个属性接收
        if (isTranslationFrom(TRANSLATION_TO_BOTTOM)) {
            setTranslationY(mHeight);
        }
        if (isTranslationFrom(TRANSLATION_TO_TOP)) {
            setTranslationY(-mHeight);
        }
        if (isTranslationFrom(TRANSLATION_TO_LEFT)) {
            setTranslationX(-mWidth);
        }
        if (isTranslationFrom(TRANSLATION_TO_RIGHT)) {
            setTranslationX(mWidth);
        }
    }

    public boolean isTranslationFrom(int translationMask) {
        if (mDiscrollTranslation == -1) {
            return false;
        }
        return (mDiscrollTranslation & translationMask) == translationMask;
    }

    public void setDiscrollFromBgColor(int mDiscrollveFromBgColor) {
        this.mDiscrollFromBgColor = mDiscrollveFromBgColor;
    }

    public void setDiscrollToBgColor(int mDiscrollveToBgColor) {
        this.mDiscrollToBgColor = mDiscrollveToBgColor;
    }

    public void setDiscrollAlpha(boolean mDiscrollveAlpha) {
        this.mDiscrollAlpha = mDiscrollveAlpha;
    }

    public void setDiscrollTranslation(int mDisCrollveTranslation) {
        this.mDiscrollTranslation = mDisCrollveTranslation;
    }

    public void setDiscrollScaleX(boolean mDiscrollveScaleX) {
        this.mDiscrollScaleX = mDiscrollveScaleX;
    }

    public void setDiscrollScaleY(boolean mDiscrollveScaleY) {
        this.mDiscrollScaleY = mDiscrollveScaleY;
    }
}
