package demo.zkttestdemo.effect.filtermenu;

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
 * Created by zkt on 2018-1-28.
 */

public class FilterMenuView extends LinearLayout {
    private LinearLayout mMenuTabView;
    private FrameLayout mMenuMiddleView;
    private FrameLayout mMenuContainerView;
    private View mShadowView;
    private Context mContext;
    private BaseMenuAdapter mAdapter;
    private int mShadowColor = Color.parseColor("#999999");
    private int menuContainerHeight;


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
        //        mShadowView.setAlpha(0);
        //        mShadowView.setVisibility(GONE);
        mMenuMiddleView.addView(mShadowView);
        //创建菜单 存放菜单内容
        mMenuContainerView = new FrameLayout(mContext);
        mMenuContainerView.setBackgroundColor(Color.WHITE);
        mMenuMiddleView.addView(mMenuContainerView);

        //内容加进去
        addView(mMenuMiddleView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.e("onMeasure", "高度：" + height);
        menuContainerHeight = height * 75 / 100;

        ViewGroup.LayoutParams layoutParams = mMenuContainerView.getLayoutParams();
        layoutParams.height = menuContainerHeight;
        mMenuContainerView.setLayoutParams(layoutParams);

        //            mMenuContainerView.setTranslationY(-menuContainerHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("onSizeChanged", "高度：" + h);

        //        menuContainerHeight = h * 75 / 100;
        //
        //        ViewGroup.LayoutParams layoutParams = mMenuContainerView.getLayoutParams();
        //        layoutParams.height = menuContainerHeight;
        //        mMenuContainerView.setLayoutParams(layoutParams);
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

            //获取菜单的内容
            View menuView = mAdapter.getMenuView(i, mMenuContainerView);
            //            menuView.setVisibility(GONE);
            mMenuContainerView.addView(menuView);
        }
    }

}
