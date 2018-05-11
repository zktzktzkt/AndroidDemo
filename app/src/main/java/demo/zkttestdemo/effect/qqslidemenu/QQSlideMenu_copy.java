package demo.zkttestdemo.effect.qqslidemenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2018-1-28.
 */

public class QQSlideMenu_copy extends HorizontalScrollView {

    private int mMenuWidth;
    private GestureDetector mGestureDetector;
    private View mMenuView;
    private ImageView mShadowView;
    private boolean mMenuIsOpen = false;

    public QQSlideMenu_copy(Context context) {
        this(context, null);
    }

    public QQSlideMenu_copy(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQSlideMenu_copy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QQSlideMenu);
        float rightPadding = array.getDimension(R.styleable.QQSlideMenu_right_padding, ConvertUtils.dp2px(50f));
        array.recycle();

        mMenuWidth = (int) (ScreenUtils.getScreenWidth() - rightPadding);
        mGestureDetector = new GestureDetector(context, new Ges());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ViewGroup container = (ViewGroup) getChildAt(0);
        mMenuView = container.getChildAt(0);
        View oldContentView = container.getChildAt(1);
        container.removeView(oldContentView);
        FrameLayout newContent = new FrameLayout(getContext());
        mShadowView = new ImageView(getContext());
        mShadowView.setBackgroundColor(Color.parseColor("#99000000"));
        newContent.addView(oldContentView);
        newContent.addView(mShadowView);
        container.addView(newContent, 1);

        View contentView = container.getChildAt(1);
        mMenuView.getLayoutParams().width = mMenuWidth;
        contentView.getLayoutParams().width = ScreenUtils.getScreenWidth();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            scrollTo(mMenuWidth, 0);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.e("l", l + " getScrollX : " + getScrollX());
        mMenuView.setTranslationX(l * 0.8f);
        
        float scale = l * 1f / mMenuWidth;
        mShadowView.setAlpha(1 - scale);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mGestureDetector.onTouchEvent(ev)) {
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (getScrollX() > mMenuWidth / 2) {
                    closeMenu();
                } else {
                    openMenu();
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    private void closeMenu() {
        smoothScrollTo(mMenuWidth, 0);
        mMenuIsOpen = false;
    }

    private void openMenu() {
        smoothScrollTo(0, 0);
        mMenuIsOpen = true;
    }

    private class Ges extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (mMenuIsOpen) {
                if (velocityX < 0) {
                    toggleMenu();
                }
            } else {
                if (velocityX > 0) {
                    toggleMenu();
                }
            }

            return true;
        }
    }

    private void toggleMenu() {
        if (mMenuIsOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }
}
