package demo.zkttestdemo.effect.kugoumenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.blankj.utilcode.util.ScreenUtils;

import demo.zkttestdemo.R;
import demo.zkttestdemo.utils.DensityUtil;

/**
 * Created by zkt on 2018-6-4.
 * Description:
 */

public class KuGouSlidingMenu extends HorizontalScrollView {

    private View mMenuView;
    private int mMenuWidth;
    private View mContentView;
    private boolean mMenuIsOpen;

    public KuGouSlidingMenu(Context context) {
        this(context, null);
    }

    public KuGouSlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KuGouSlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.KGSlidingMenu);
        float rightMargin = array.getDimension(R.styleable.KGSlidingMenu_menuRightMargin, DensityUtil.dip2px(context, 50));
        mMenuWidth = (int) (ScreenUtils.getScreenWidth() - rightMargin);

        array.recycle();
    }

    private class GestureDetectorListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (mMenuIsOpen) {

            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    /**
     * 布局加载完毕回调，这时候可以获取xml中定义的View
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ViewGroup container = (ViewGroup) getChildAt(0);

        int childCount = container.getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("只能放置两个子View！");
        }

        // 1.1 获取菜单 指定宽度
        mMenuView = container.getChildAt(0);
        ViewGroup.LayoutParams menuParams = mMenuView.getLayoutParams();
        menuParams.width = mMenuWidth;
        // 7.0以下，必须设置如下代码
        mMenuView.setLayoutParams(menuParams);

        // 1.2 获取主布局 指定宽度
        mContentView = container.getChildAt(1);
        ViewGroup.LayoutParams contentParams = mContentView.getLayoutParams();
        contentParams.width = ScreenUtils.getScreenWidth();
        mContentView.setLayoutParams(contentParams);

        // 不能在这调用，因为这是在onLayout之前执行的，就算滚动到指定位置，程序执行onLayout的时候又会重新摆放
        // scrollTo(mMenuWidth, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        // 2. 初始化进来是关闭的
        scrollTo(mMenuWidth, 0);
    }

    //3. 手指抬起来二选一，要么关闭 要么打开
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            //只需要管手指抬起，根据我们滚动的距离来判断
            int currentScrollX = getScrollX();
            if (currentScrollX > mMenuWidth / 2) {
                // 关闭
                closeMenu();
            } else {
                // 打开
                openMenu();
            }
            // DOWN 和 MOVE 不用管，但是 UP 的时候，事件由自己处理，不给super
            return true;
        }
        return super.onTouchEvent(ev);
    }

    //4. 处理右边的缩放、左边的缩放和透明度，需要不断的获取当前的滚动位置
    //技巧：找一个对照的View（菜单）和 找一个基准点状态（菜单打开）
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.e("TAG", "l -> " + l); //变化是mMenuWidth -> 0
        // 算一个梯度值
        float scale = 1f * l / mMenuWidth; // 1 -> 0 菜单从关到开

        //右边的缩放 最小是0.7f,最大是1f
        float rightScale = 0.7f + 0.3f * scale;
        //设置右边的缩放
        mContentView.setPivotX(0);
        mContentView.setPivotY(mContentView.getMeasuredHeight() / 2);
        mContentView.setScaleX(rightScale);
        mContentView.setScaleY(rightScale);

        //菜单打开时缩放和透明度
        //透明：半透明->完全透明 0.7f -> 1.0f
        float alpha = 0.5f + 0.5f * (1 - scale);//菜单打开时scale为0
        mMenuView.setAlpha(alpha);
        //缩放：0.7f -> 1.0f
        float leftScale = 0.7f + 0.3f * (1 - scale);
        mMenuView.setScaleX(leftScale);
        mMenuView.setScaleY(leftScale);
        //设置平移
        mMenuView.setTranslationX(l * 0.25f);
    }

    /**
     * 打开菜单 滚动到0的位置
     */
    private void openMenu() {
        smoothScrollTo(0, 0);
    }

    /**
     * 关闭菜单 滚动mMenuWidth的宽度
     */
    private void closeMenu() {
        smoothScrollTo(mMenuWidth, 0);
    }
}
