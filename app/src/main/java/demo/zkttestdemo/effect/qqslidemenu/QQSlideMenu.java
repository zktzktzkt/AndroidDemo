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
 * Created by zkt on 2017-11-6.
 */

public class QQSlideMenu extends HorizontalScrollView {

    /**
     * 内容
     */
    private View mContentView;
    /**
     * 菜单
     */
    private View mMenuView;

    /**
     * 菜单宽度
     */
    private int mMenuWidth;

    /**
     * 手势处理
     */
    private GestureDetector mGestrueDetector;

    /**
     * 手指快速移动 - 菜单是否打开
     */
    private boolean mMenuIsOpen = false;
    /**
     * 阴影
     */
    private ImageView mShadow;

    public QQSlideMenu(Context context) {
        this(context, null);
    }

    public QQSlideMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQSlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QQSlideMenu);
        float rightPadding = array.getDimension(R.styleable.QQSlideMenu_right_padding, ConvertUtils.dp2px(50f));
        //1.2.1 菜单宽度 = 屏幕宽度 - 距右边的宽度
        mMenuWidth = (int) (ScreenUtils.getScreenWidth() - rightPadding);
        array.recycle();

        //初始化手势处理类
        mGestrueDetector = new GestureDetector(context, new Gus());
    }

    /**
     * 整个xml布局加载完毕
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //1.用代码动态获取指定布局的宽度
        //1.1 获取菜单和内容的View
        //1.1.1 先获取根布局LinearLayout
        ViewGroup container = (ViewGroup) getChildAt(0);

        //1.1.2获取菜单View
        mMenuView = container.getChildAt(0);

        //处理内容的阴影效果
        //思路：在内容布局的外面加上一层阴影 ImageView
        // 1) 把原来的内容从根布局里移除
        View oldContentView = container.getChildAt(1);
        container.removeView(oldContentView);
        // 2) 新建一个容器布局 = 原来的内容 + 阴影
        FrameLayout newContentView = new FrameLayout(getContext());
        //把原来的内容加进新的容器
        newContentView.addView(oldContentView);
        //把阴影加入
        mShadow = new ImageView(getContext());
        mShadow.setBackgroundColor(Color.parseColor("#99000000"));
        newContentView.addView(mShadow);
        // 3) 把新布局加进去
        container.addView(newContentView, 1);

        //1.1.3获取内容View
        mContentView = container.getChildAt(1);
        //1.2 指定菜单和内容的宽度
        mMenuView.getLayoutParams().width = mMenuWidth;
        //1.2.2 内容宽度 = 屏幕宽度
        mContentView.getLayoutParams().width = ScreenUtils.getScreenWidth();

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            //1.3 默认关闭的状态，要让其自己滚动一段距离
            scrollTo(mMenuWidth, 0);
        }
    }

    /**
     * 处理菜单的抽屉效果
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // l = scrollX   t = scrollY

        //处理菜单的抽屉效果，让菜单移动一段距离
        mMenuView.setTranslationX(l * 0.8f);

        //滑动到不同位置改变阴影透明度
        //透明度肯定是一个梯度值 分子灵活变，分母是固定的最大值
        // 这个乘1f必须放前面，如果放后面，前面两个int类型相除会一直是0
        float scale = l * 1f / mMenuWidth; // 600/600=1 300/600=0.5 0/600=0  1 -> 0
        float alphaScale = 1 - scale; //透明度应该是从0->1，所以再用1去减，就能得到相反数
        mShadow.setAlpha(alphaScale);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //处理手指快速滑动 手势处理类使用 拦截
        if (mGestrueDetector.onTouchEvent(ev)) {
            return true;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                Log.e("QQSlideMenu", "抬起");
                int currentScrollX = getScrollX();
                //向左滚动的距离超过了菜单的一半，就关闭
                if (currentScrollX > mMenuWidth / 2) {
                    closeMenu();
                } else {
                    openMenu();
                }
                //为了防止up事件无效（被super处理），这里up由自己接手处理
                return true;
        }
        Log.e("QQSlideMenu", "super.onTouchEvent(ev):" + super.onTouchEvent(ev));
        return super.onTouchEvent(ev);
    }

    /**
     * 打开菜单
     */
    private void openMenu() {
        smoothScrollTo(0, 0);
        mMenuIsOpen = true;
    }

    /**
     * 关闭菜单
     */
    private void closeMenu() {
        smoothScrollTo(mMenuWidth, 0);
        mMenuIsOpen = false;
    }

    /**
     * 手势处理类的监听回调
     */
    private class Gus extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //向右快速滑动 velocityX值大于0
            //向左快速滑动 velocityX值小于0

            //逻辑  如果菜单打开 向左快速滑动的时候 应该切换菜单状态
            if (mMenuIsOpen) {
                if (velocityX < 0) {
                    toggleMenu();
                    return true;
                }
            } else {
                if (velocityX > 0) {
                    toggleMenu();
                }
                return true;
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    /**
     * 切换菜单状态
     */
    public void toggleMenu() {
        if (mMenuIsOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }
}
