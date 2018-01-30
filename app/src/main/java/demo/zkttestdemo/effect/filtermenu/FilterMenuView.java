package demo.zkttestdemo.effect.filtermenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * 仿美团下拉筛选菜单
 * Created by zkt on 2018-1-28.
 */

public class FilterMenuView extends LinearLayout implements View.OnClickListener {
    private LinearLayout mMenuTabView;
    private FrameLayout mMenuMiddleView;
    private FrameLayout mMenuContainerView;
    private View mShadowView;
    private Context mContext;
    private BaseMenuAdapter mAdapter;
    private int mShadowColor = 0x88888888;
    private int mMenuContainerHeight;
    //当前打开的位置
    private int mCurrentPosition = -1;
    private long DURATION_TIME = 350;
    //动画是否在执行
    private boolean mAnimatorExecute;


    public FilterMenuView(Context context) {
        this(context, null);
    }

    public FilterMenuView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initLayout();
    }

    /**
     * 1. 布局实例化 （组合控件）
     */
    private void initLayout() {
        setOrientation(VERTICAL);

        // 1.1 创建头部存放tab
        mMenuTabView = new LinearLayout(mContext);
        mMenuTabView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mMenuTabView.setOrientation(HORIZONTAL);
        addView(mMenuTabView);

        // 1.2 创建FrameLayout存放 阴影（View） + 菜单内容布局（FrameLayout）
        mMenuMiddleView = new FrameLayout(mContext);
        mMenuMiddleView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //阴影
        mShadowView = new View(mContext);
        mShadowView.setBackgroundColor(mShadowColor);
        mShadowView.setAlpha(0);
        mShadowView.setVisibility(GONE);
        mShadowView.setOnClickListener(this);
        mMenuMiddleView.addView(mShadowView);
        //创建菜单 存放菜单内容
        mMenuContainerView = new FrameLayout(mContext);
        mMenuContainerView.setBackgroundColor(Color.WHITE);
        mMenuMiddleView.addView(mMenuContainerView);

        //内容加进去
        addView(mMenuMiddleView);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("onSizeChanged", "高度：" + h);

        mMenuContainerHeight = (int) (h * 0.75);

        ViewGroup.LayoutParams layoutParams = mMenuContainerView.getLayoutParams();
        layoutParams.height = mMenuContainerHeight;
        mMenuContainerView.setLayoutParams(layoutParams);

        mMenuContainerView.setTranslationY(-mMenuContainerHeight);
    }


    /**
     * 设置adapter
     *
     * @param menuAdapter
     */
    public void setAdapter(BaseMenuAdapter menuAdapter) {
        this.mAdapter = menuAdapter;

        //获取有多少条
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            //获取tab
            View tabView = mAdapter.getTabView(i, mMenuTabView);
            LinearLayout.LayoutParams params = (LayoutParams) tabView.getLayoutParams();
            params.weight = 1;
            tabView.setLayoutParams(params);
            mMenuTabView.addView(tabView);

            setTabClick(tabView, i);

            //获取菜单的内容
            View menuView = mAdapter.getMenuView(i, mMenuContainerView);
            menuView.setVisibility(GONE);
            mMenuContainerView.addView(menuView);
        }
    }

    /**
     * 设置tab点击
     *
     * @param tabView
     * @param position
     */
    private void setTabClick(final View tabView, final int position) {
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPosition == -1) {
                    //没打开
                    openMenu(tabView, position);
                } else {
                    //打开了
                    if (mCurrentPosition == position) {
                        closeMenu();
                    } else {
                        //切换一下
                        mMenuContainerView.getChildAt(mCurrentPosition).setVisibility(GONE);
                        mAdapter.menuClose(mMenuTabView.getChildAt(mCurrentPosition));
                        mCurrentPosition = position;
                        mMenuContainerView.getChildAt(mCurrentPosition).setVisibility(VISIBLE);
                        mAdapter.menuOpen(mMenuTabView.getChildAt(mCurrentPosition));
                    }
                }
            }
        });
    }

    /**
     * 关闭菜单
     */
    private void closeMenu() {
        if (mAnimatorExecute) {
            return;
        }
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mMenuContainerView, "translationY", 0, -mMenuContainerHeight);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mShadowView, "alpha", 1f, 0);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(DURATION_TIME);
        set.playTogether(translationY, alpha);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimatorExecute = true;
                mAdapter.menuClose(mMenuTabView.getChildAt(mCurrentPosition));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorExecute = false;
                View menuView = mMenuContainerView.getChildAt(mCurrentPosition);
                menuView.setVisibility(GONE);
                mShadowView.setVisibility(GONE);
                mCurrentPosition = -1;
            }
        });
        set.start();
    }

    /**
     * 打开菜单
     *
     * @param tabView
     * @param position
     */
    private void openMenu(final View tabView, final int position) {
        if (mAnimatorExecute) {
            return;
        }
        //根据当前位置显示对应的菜单内容
        View menuView = mMenuContainerView.getChildAt(position);
        menuView.setVisibility(VISIBLE);
        mShadowView.setVisibility(VISIBLE);

        ObjectAnimator translationY = ObjectAnimator.ofFloat(mMenuContainerView, "translationY", -mMenuContainerHeight, 0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mShadowView, "alpha", 0, 1f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(DURATION_TIME);
        set.playTogether(translationY, alpha);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimatorExecute = true;
                mAdapter.menuOpen(tabView);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorExecute = false;
                mCurrentPosition = position;
            }
        });
        set.start();

    }

    @Override
    public void onClick(View v) {
        closeMenu();
    }
}
