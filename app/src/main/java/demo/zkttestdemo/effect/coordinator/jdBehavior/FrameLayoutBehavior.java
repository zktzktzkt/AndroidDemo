package demo.zkttestdemo.effect.coordinator.jdBehavior;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.jd.lib.search.R;
import com.jd.lib.search.core.constants.SortConstant;
import com.jd.lib.search.core.listener.ITranslationYCallBack;
import com.jd.lib.search.view.Activity.ProductListActivity;

import java.util.List;

public class FrameLayoutBehavior extends BaseHeaderScrollingViewBehavior<FrameLayout> {

    private ProductListActivity productListActivity;
    /**932是scene tabs style */
    protected boolean isSceneTabsStyle;
    /** 回调滑动数值 */
    private ITranslationYCallBack positionCallBack;
    /**
     * 构造方法
     *
     * @param context
     * @param attrs
     */
    public FrameLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public View findFirstDependency(List<View> views) {
        if (views != null) {
            for (int i = 0; i < views.size(); i++) {
                if (isDependency(views.get(i))) {
                    return views.get(i);
                }
            }
        }
        return null;
    }

    /** 搜索框 **/
    private View mTitle;
    /** 排序栏 **/
    private View mSort;
    /**
     * 初始化view
     */
    private void initView(View parent) {
        if (parent == null) {
            return;
        }
        if (mTitle == null) {
            mTitle = parent.findViewById(R.id.product_list_search_title);
        }
        if (mSort == null) {
            mSort = parent.findViewById(R.id.sort_buttons_layout);
        }
    }

    /** 搜索框高度 **/
    private int mTitleViewHeight = 0;
    /**是否显示顶部**/
    private boolean showInTilte;
    /** 排序栏高度 **/
    private int mSortViewHeight = 0;
    /**
     * 测量高度
     */
    /**是否是新型顶部**/
    public  boolean newTop;
    private void measuredViewHeight(CoordinatorLayout parent) {
        mTitleViewHeight = Utils.getViewHeight(mTitle);
        if (parent.getContext() != null) {
            if (parent.getContext() instanceof ProductListActivity) {
                productListActivity = ((ProductListActivity) parent.getContext());
                newTop = productListActivity.newtops;
                showInTilte = productListActivity.showInTilte;
            }
        }
        if (showInTilte) {
            mSortViewHeight = 0;
        } else {
            mSortViewHeight = Utils.getViewHeight(parent.findViewById(R.id.sort_buttons_layout));
        }
    }

    /**
     * 是否依赖
     *
     * @param dependency dependency
     * @return 是否依赖
     */
    private boolean isDependency(View dependency) {
        return dependency != null && dependency.isShown()
                && isSpecialTabSelect()
                && ((dependency.getId() == R.id.sort_buttons_layout) || (dependency.getId() == R.id.product_list_search_title && isViewGone(mSort)));
    }

    /**
     * 1.小时达Tab 2.酒旅Tab 3、超值Tab 4.导航场景化tab
     * @return 
     */
    private boolean isSpecialTabSelect(){
        return TextUtils.equals(productListActivity.getCurrentSelectTab(), SortConstant.SORT_REF_TYPE_HOUR_906)
                || TextUtils.equals(productListActivity.getCurrentSelectTab(), SortConstant.SORT_REF_TYPE_HOTEL_TRAVEL)
                || TextUtils.equals(productListActivity.getCurrentSelectTab(), SortConstant.SORT_REF_TYPE_SUPERVALUE)
                || TextUtils.equals(productListActivity.getCurrentSelectTab(), SortConstant.SORT_REF_TAB_GUIDESCENETAB)
                || TextUtils.equals(productListActivity.getCurrentSelectTab(), SortConstant.SORT_REF_TAB_SUITTAB)
                || productListActivity.IsSpecialTabSelect();
    }

    /**
     * view是否显示
     *
     * @param view view
     * @return view是否显示
     */
    private boolean isViewGone(View view) {
        return view == null || !view.isShown();
    }

    /**
     * 确定当前的RecyclerView依赖于哪一个头部视图，此方法在一次layout过程中至少会被调用一次
     * @param parent 外层容器CoordinatorLayout
     * @param child 当前的列表RecyclerView
     * @param dependency 和RecyclerView同级的几个头部兄弟视图
     * @return 当dependency是与RecyclerView紧挨着的头部视图时，说明当前的dependency为RecyclerView的依赖
     */
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull FrameLayout child, @NonNull View dependency) {
        initView(parent);
        measuredViewHeight(parent);
        return isDependency(dependency);
    }

    /**
     * 当RecyclerView的依赖视图的大小或位置发生改变时，当前方法被调用
     * @param parent 外层容器CoordinatorLayout
     * @param child 当前的列表RecyclerView
     * @param dependency 由上面layoutDependsOn方法确定的RecyclerView所依赖的头部视图
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FrameLayout child, View dependency) {
        if (isDependency(dependency)) {
            child.setTranslationY(dependency.getTranslationY());
            if (positionCallBack!=null){
                positionCallBack.setTranslationY(dependency.getTranslationY());
            }
        }
        return false;
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, FrameLayout child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);

        measuredViewHeight(parent);
        if (isNeedSetTranslationY()) {
            handleHeaderTranslationY();
        }
        List<View> dependencies = parent.getDependencies(child);
        View header = findFirstDependency(dependencies);
        float translationY = header == null ? 0 : header.getTranslationY();
        if (positionCallBack!=null){
            positionCallBack.setTranslationY(translationY);
        }
        child.setTranslationY(translationY);
    }

    /**
     * 顶部高度总和
     */
    public int getTotalHeightOfHeaders() {
        return mTitleViewHeight +  mSortViewHeight ;
    }


    /**
     * 设置各控件位置
     */
    private void handleHeaderTranslationY() {
        setTranslationY(mTitle, 0);
        setTranslationY(mSort, mTitleViewHeight);
    }

    /**
     * 设置位移
     *
     * @param view         view
     * @param translationY translationY
     */
    private void setTranslationY(View view, float translationY) {
        if (view != null) {
            view.setTranslationY(translationY);
        }
    }

    /**
     * findbugs 修改 设置是否需要重置各顶部布局
     *
     * @param isNeedRestHeader
     */
    public static void setIsNeedRestHeader(boolean isNeedRestHeader) {
        FrameLayoutBehavior.isFLBNeedRestHeader = isNeedRestHeader;
    }

    /** 是否需要重置各顶部布局 **/
    private static boolean isFLBNeedRestHeader = true;
    /**
     * 是否需要重置位置
     *
     * @return 是否需要重置位置
     */
    private boolean isNeedSetTranslationY() {
        return isFLBNeedRestHeader || (getTranslationY(mTitle) == 0 && getTranslationY(mSort) == 0);
    }

    /**
     * 获取位移长度
     *
     * @param view view
     * @return 获取位移长度
     */
    private float getTranslationY(View view) {
        if (view == null) {
            return 0;
        }
        return view.getTranslationY();
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, FrameLayout child, MotionEvent ev) {

        return super.onInterceptTouchEvent(parent, child, ev);
    }

    public void setPositionCallBack(ITranslationYCallBack positionCallBack){
        this.positionCallBack = positionCallBack;
    }


}
