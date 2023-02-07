package demo.zkttestdemo.effect.coordinator.jdBehavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jd.lib.search.R;
import com.jd.lib.search.core.constants.Constant;
import com.jd.lib.search.core.constants.SortConstant;
import com.jd.lib.search.core.model.entity.productlist.SuperConfigEntity;
import com.jd.lib.search.core.utils.ViewUtil;
import com.jd.lib.search.view.Activity.ProductListActivity;
import com.jd.lib.search.view.baseview.PreLoadView;
import com.jd.lib.search.view.behavior.base.Utils;
import com.jingdong.app.mall.bundle.mobileConfig.JDMobileConfig;

import java.util.List;

/**
 * recyclerView behavior
 *
 * @author duanwenqiang1
 * @date 2018/6/5
 */
public class RecyclerViewBehavior extends BaseHeaderScrollingViewBehavior<RecyclerView> {

    /** 搜索框高度 **/
    private int mTitleViewHeight = 0;
    /** banner高度 **/
    private int mBannerViewHeight = 0;
    /** 排序栏高度 **/
    private int mSortViewHeight = 0;
    /** 新排序栏高度 **/
    protected int mSceneSortViewHeight = 0;
    /** 筛选外漏高度 **/
    private int mFilterButtonsViewHeight = 0;
    /** 内容场景tab漏高度 **/
    private int mContentSceneViewHeight = 0;

    /** 搜索框 **/
    private View mTitle;
    /** banner **/
    private View mBanner;
    /** 排序栏 **/
    private View mSort;
    /** 新排序栏 **/
    private View mScene;
    /** 筛选外漏 **/
    private View mFilterButtons;
    /** 内容tab **/
    private View mContentSceneTabButtons;

    /** 底部霸屏recyclerview高度 **/
    protected int mNewTopHeight = 0;

    /**初次加载结果页(二次搜索时不算)**/
    private boolean firstLoad = true;
    /** 预加载view **/
    private PreLoadView mPreLoadView;
    /** 头部临时容器*/
    private ViewGroup tempHeader;
    /** 是否需要重置各顶部布局 **/
    private static boolean isNeedRestHeader = true;
    /** 是否小时达feeds **/
    private static boolean isHourReachFeeds = false;
    /** banner是否要顶到状态栏。（比如新品专） **/
    private static boolean isNoTitleMode = false;
    /**是否是新型顶部**/
    public  boolean newTop;
    /**932是scene tabs style */
    protected boolean isSceneTabsStyle;
    /**V10.0 是否是场景tab */
    protected boolean isContentSceneTabStyle;

    /**是否显示顶部**/
    private boolean showInTilte;
    /**楼层entity*/
//    public SuperConfigEntity superConfigEntity;
    /**双层霸屏底部recycleview**/
    private RecyclerView underRecyclerView;
    /**双层霸屏顶部recycleview**/
    private RecyclerView recyclerView;
    /** 底部霸屏RecyclerView底部的Y坐标值*/
    private float underRvBottomY;

    private ProductListActivity productListActivity;

    /**
     * 构造方法(！！！必须重写，否则在布局中引用Behavior会抛异常)
     *
     * @param context context
     * @param attrs   attrs
     */
    public RecyclerViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 是否是小时达feeds流
     */
    public static void setIsHourReachFeeds(boolean isHourReachFeeds) {
        RecyclerViewBehavior.isHourReachFeeds = isHourReachFeeds;
    }

    /**
     * findbugs 修改 设置是否需要重置各顶部布局
     *
     * @param isNeedRestHeader
     */
    public static void setIsNeedRestHeader(boolean isNeedRestHeader) {
        RecyclerViewBehavior.isNeedRestHeader = isNeedRestHeader;
    }

    /**banner是否顶到状态栏之上**/
    public static void setIsNoTitleMode(Activity activity, boolean isNoTitleMode) {
        RecyclerViewBehavior.isNoTitleMode = isNoTitleMode;
        ProductListHeaderHelper.setNoTitleModes(activity, isNoTitleMode);
    }

    /**
     * banner是否通到状态栏之上
     */
    public static boolean isNoTitleMode() {
        return isNoTitleMode;
    }

    /**
     * 确定当前的RecyclerView依赖于哪一个头部视图，此方法在一次layout过程中至少会被调用一次
     * @param parent 外层容器CoordinatorLayout
     * @param child 当前的列表RecyclerView
     * @param dependency 和RecyclerView同级的几个头部兄弟视图
     * @return 当dependency是与RecyclerView紧挨着的头部视图时，说明当前的dependency为RecyclerView的依赖
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RecyclerView child, View dependency) {
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
    public boolean onDependentViewChanged(CoordinatorLayout parent, RecyclerView child, View dependency) {
        if (isDependency(dependency)) {
            child.setTranslationY(dependency.getTranslationY());
        }
        return false;
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, RecyclerView child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);
        measuredViewHeight(parent);
        if (isNeedSetTranslationY() && isSpecialTabNotSelect()) {
            handleHeaderTranslationY();
        }
        List<View> dependencies = parent.getDependencies(child);
        View header = findFirstDependency(dependencies);
        float translationY = header == null ? 0 : header.getTranslationY();
        float height = header == null ? 0 : header.getHeight();
        float tempHeaderHeight = ViewUtil.isVisible(tempHeader) ? ViewUtil.getViewHeight(tempHeader) : 0;
        child.setTranslationY(translationY);
        // 设置临时容器的位置，在header的下面
        if (ViewUtil.isVisible(tempHeader)) {
            float tempHeaderTransY = translationY + height;
            if (firstLoad) {
                tempHeader.setTranslationY(mTitleViewHeight);
            } else {
                tempHeader.setTranslationY((isNoTitleMode && tempHeaderTransY == 0) ? mTitleViewHeight : tempHeaderTransY);
            }
        }
        // 设置预加载视图的位置
        /*if (mPreLoadView != null) {
            // 设置mPreLoadView的位置，在header的下面(至少保证在搜索框的下面)
            float preloadViewY = Math.max((translationY + height + tempHeaderHeight), mTitleViewHeight);
            // 第一次打开搜索结果页时，默认就loading层在title下
            if (firstLoad) {
                mPreLoadView.setTranslationY(mTitleViewHeight);
            } else {
                // 新品专时，有时候拿到的preloadViewY为0，做一个兜底。
                mPreLoadView.setTranslationY((isNoTitleMode && preloadViewY == 0) ? mTitleViewHeight : preloadViewY);
            }
        }*/
        firstLoad = false;
    }

    /**
     * 顶部高度总和
     */
    public int getTotalHeightOfHeaders() {
        return mTitleViewHeight + mBannerViewHeight + mSortViewHeight + mSceneSortViewHeight + mFilterButtonsViewHeight + mContentSceneViewHeight;
    }

    /**
     * 场景tab顶部高度总和
     */
    public int getScenceTabTotalHeightOfHeaders() {
        return mTitleViewHeight + mSortViewHeight;
    }

    /**
     *
     * @return
     */
    private boolean isSpecialTabNotSelect() {
        if (isHourReachFeeds) { //小时达
            return !TextUtils.equals(productListActivity.getCurrentSelectTab(), SortConstant.SORT_REF_TYPE_HOUR_906);
        } else {
            return true;
        }
    }

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
        if (mBanner == null) {
            mBanner = parent.findViewById(R.id.product_list_top_banner_view);
        }
        if (mSort == null) {
            mSort = parent.findViewById(R.id.sort_buttons_layout);
        }
        if (mScene == null) {
            mScene = parent.findViewById(R.id.scene_sort_layout);
        }
        if (mFilterButtons == null) {
            mFilterButtons = parent.findViewById(R.id.filter_buttons_undertabs_layout);
        }
        if (mContentSceneTabButtons == null) {
            mContentSceneTabButtons = parent.findViewById(R.id.content_scene_buttons_undertabs_layout);
        }
        if (mPreLoadView == null) {
            mPreLoadView = parent.findViewById(R.id.preload);
        }
        if (tempHeader == null) {
            tempHeader = parent.findViewById(R.id.temp_header_container);
        }
        //一进入 页面动画过程中 使顶部的列表 滑动事件无效 ，
        // 否则会有 滑动时排序栏和外漏栏不随手指滑动的问题
        if (recyclerView == null) {
            recyclerView = parent.findViewById(R.id.product_list);
        }
        if (recyclerView != null) {
            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (canDoAnimation) {
                        return false;
                    } else {
                        return true;
                    }
                }
            });
        }
        if (underRecyclerView == null) {
            underRecyclerView = parent.findViewById(R.id.new_top);
        }
        if (underRecyclerView != null) {
            underRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (canDoAnimation) {
                        doTransByUnderRvTouch();
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 测量高度
     */
    private void measuredViewHeight(CoordinatorLayout parent) {
        mTitleViewHeight = Utils.getViewHeight(mTitle);
        mSceneSortViewHeight = Utils.getViewHeight(mScene);
        mNewTopHeight = Utils.getViewHeight(parent.findViewById(R.id.new_top));

        if (parent.getContext() != null) {
            if (parent.getContext() instanceof ProductListActivity) {
                productListActivity = ((ProductListActivity) parent.getContext());
                newTop = productListActivity.newtops;
                superConfigEntity = productListActivity.superEntity;
                isSceneTabsStyle = productListActivity.getActivityDataState().isSceneTabsStyle;
                isContentSceneTabStyle = productListActivity.getActivityDataState().isContentSceneTabStyle;
                showInTilte = productListActivity.showInTilte;
            }
        }
        if (showInTilte) {
            mSortViewHeight = 0;
        } else {
            mSortViewHeight = Utils.getViewHeight(parent.findViewById(R.id.sort_buttons_layout));
        }
        if (newTop) {
            mBannerViewHeight = 0;
            int topMargin = -1;
            topMargin = DisplayUtil.getScreenHeight() - mSortViewHeight;
            if (superConfigEntity != null && mNewTopHeight <= topMargin) {
                underRvBottomY = mNewTopHeight;
            } else {
                underRvBottomY = topMargin;
            }
        } else {
            mBannerViewHeight = Utils.getViewHeight(mBanner);
            underRvBottomY = 0;
        }
        mFilterButtonsViewHeight = Utils.getViewHeight(mFilterButtons);
        mContentSceneViewHeight = Utils.getViewHeight(mContentSceneTabButtons);
    }

    /**
     * 设置各控件位置
     */
    private void handleHeaderTranslationY() {
        if (isNoTitleMode) {
            handleHeaderNoTitleTranslationY();
        } else {
            setTranslationY(mTitle, 0);
            setTranslationY(mBanner, mTitleViewHeight);
            setTranslationY(mSort, mTitleViewHeight + mBannerViewHeight);
            setTranslationY(mScene, mTitleViewHeight + mBannerViewHeight + mSortViewHeight);
            setTranslationY(mFilterButtons, mTitleViewHeight + mBannerViewHeight + mSortViewHeight + mSceneSortViewHeight);
            setTranslationY(mContentSceneTabButtons, mTitleViewHeight + mBannerViewHeight + mSortViewHeight);
        }
    }

    /**
     * 设置各控件位置
     */
    private void handleHeaderNoTitleTranslationY() {
        setTranslationY(mTitle, 0);
        setTranslationY(mBanner, 0);
        setTranslationY(mSort, mBannerViewHeight + (newTop ? mTitleViewHeight : 0));
        setTranslationY(mScene, mBannerViewHeight + mSortViewHeight + (newTop ? mTitleViewHeight : 0));
        setTranslationY(mFilterButtons, mBannerViewHeight + mSortViewHeight + mSceneSortViewHeight + (newTop ? mTitleViewHeight : 0));
        setTranslationY(mContentSceneTabButtons, mBannerViewHeight + mSortViewHeight + (newTop ? mTitleViewHeight : 0));
    }

    /**
     * 是否需要重置位置
     *
     * @return 是否需要重置位置
     */
    private boolean isNeedSetTranslationY() {
        if (isSceneTabsStyle) {
            return isNeedRestHeader ||
                    (
                            getTranslationY(mTitle) == 0 &&
                                    getTranslationY(mBanner) == 0 &&
                                    getTranslationY(mSort) == 0 &&
                                    getTranslationY(mScene) == 0 &&
                                    getTranslationY(mFilterButtons) == 0 &&
                                    getTranslationY(mContentSceneTabButtons) == 0
                    );
        } else {
            return isNeedRestHeader ||
                    (
                            getTranslationY(mTitle) == 0 &&
                                    getTranslationY(mBanner) == 0 &&
                                    getTranslationY(mSort) == 0 &&
                                    getTranslationY(mFilterButtons) == 0 &&
                                    getTranslationY(mContentSceneTabButtons) == 0
                    );
        }

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

    @Override
    protected int getScrollRange(View dependency) {
        if (isDependency(dependency)) {
            if (isSceneTabsStyle) {
                if (dependency.getId() == R.id.filter_buttons_undertabs_layout) {
                     return super.getScrollRange(dependency) - Constant.STATUS_BAR_HEIGHT;
                }
            } else {
                if (dependency.getId() == R.id.filter_buttons_undertabs_layout || dependency.getId() == R.id.content_scene_buttons_undertabs_layout) {
                    // 外漏 需要吸顶
                    return -Constant.STATUS_BAR_HEIGHT;
                }
            }
        }
        if (isSceneTabsStyle) {
            if (isContentSceneTabStyle) {
                return super.getScrollRange(dependency) - mContentSceneViewHeight - Constant.STATUS_BAR_HEIGHT;
            } else {
                return super.getScrollRange(dependency) - mSceneSortViewHeight - Constant.STATUS_BAR_HEIGHT;
            }
        } else {
            if (isContentSceneTabStyle) {
                return super.getScrollRange(dependency) - mContentSceneViewHeight - Constant.STATUS_BAR_HEIGHT;
            } else {
                return super.getScrollRange(dependency) - mFilterButtonsViewHeight - Constant.STATUS_BAR_HEIGHT;
            }
        }
    }

    /**
     * 其实在我们当前依赖设置逻辑中，这里每次views里面是只有一个依赖的，就是紧挨着列表的头部视图
     */
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

    /**
     * 是否依赖
     *
     * @param dependency dependency
     * @return 是否依赖
     */
    private boolean isDependency(View dependency) {
        if (isSceneTabsStyle) {
            if (isContentSceneTabStyle) {
                return dependency != null && dependency.isShown()
                        && ((dependency.getId() == R.id.content_scene_buttons_undertabs_layout)
                        || (dependency.getId() == R.id.sort_buttons_layout && isViewGone(mContentSceneTabButtons))
                        || (dependency.getId() == R.id.product_list_top_banner_view && isViewGone(mSort) && isViewGone(mContentSceneTabButtons))
                        || (dependency.getId() == R.id.product_list_search_title && isViewGone(mBanner) && isViewGone(mSort) && isViewGone(mContentSceneTabButtons)));
            } else {
                return dependency != null && dependency.isShown() && isSpecialTabNotSelect()
                        && ((dependency.getId() == R.id.scene_sort_layout)
                        || (dependency.getId() == R.id.sort_buttons_layout && isViewGone(mScene))
                        || (dependency.getId() == R.id.product_list_top_banner_view && isViewGone(mSort) && isViewGone(mScene))
                        || (dependency.getId() == R.id.product_list_search_title && isViewGone(mBanner) && isViewGone(mSort) && isViewGone(mScene)));
            }
        } else {
            if (isContentSceneTabStyle) {
                return dependency != null && dependency.isShown()
                        && (dependency.getId() == R.id.content_scene_buttons_undertabs_layout
                        || (dependency.getId() == R.id.sort_buttons_layout && isViewGone(mContentSceneTabButtons))
                        || (dependency.getId() == R.id.product_list_top_banner_view && isViewGone(mSort) && isViewGone(mContentSceneTabButtons))
                        || (dependency.getId() == R.id.product_list_search_title && isViewGone(mBanner) && isViewGone(mSort) && isViewGone(mContentSceneTabButtons)));
            } else {
                return dependency != null && dependency.isShown() && isSpecialTabNotSelect()
                        && (dependency.getId() == R.id.filter_buttons_undertabs_layout
                        || (dependency.getId() == R.id.sort_buttons_layout && isViewGone(mFilterButtons))
                        || (dependency.getId() == R.id.product_list_top_banner_view && isViewGone(mSort) && isViewGone(mFilterButtons))
                        || (dependency.getId() == R.id.product_list_search_title && isViewGone(mBanner) && isViewGone(mSort) && isViewGone(mFilterButtons)));
            }
        }
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

    /** 是否正在滑动 **/
    private boolean mIsBeingDragged;
    /** 上次Down事件X方向值 **/
    private int mLastMotionDownX;
    /** 上次事件Y方向值 **/
    private int mLastMotionY;
    /** 滑动临界值 **/
    private int mTouchSlop = -1;

    /**
     * 手指按下的时候是否按在RecyclerView
     */
    private boolean isFromRecyclerViewDown = false;

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, RecyclerView child, MotionEvent ev) {
        if (touchOnUnderRv(ev.getY()) && underRecyclerViewCanScrollVertically()) {
            // 当手指触摸到底部霸屏RecyclerView上，并且RecyclerView可以上滑，则滑动底层RecyclerView区域
            return false;
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            isFromRecyclerViewDown = ev.getY() >= child.getY();
        }
        if (ev.getY() >= child.getY() && isFromRecyclerViewDown) {
            if (mIsBeingDragged) {
                mIsBeingDragged = false;
                if (canDragView(child) && parent.getContext() instanceof Activity) {
                    ProductListHeaderHelper.handleHeaderViewsStatus((Activity) parent.getContext());
                }
            }
        }
        if (mTouchSlop < 0) {
            mTouchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
        }
        // 处理头部事件
        if (!isFromRecyclerViewDown) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: {
                    mIsBeingDragged = false;
                    final int x = (int) ev.getX();
                    final int y = (int) ev.getY();
                    mLastMotionDownX = x;
                    mLastMotionY = y;
                    if (canDragView(child)) {
                        ev.setLocation(ev.getX(), ev.getY() - child.getY());
                        child.onTouchEvent(ev);
                        ev.setLocation(x, y);
                    }
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    final int y = (int) ev.getY();
                    final int yDiff = Math.abs(y - mLastMotionY);

                    if (yDiff > mTouchSlop) {
                        mIsBeingDragged = true;
                        mLastMotionY = y;
                    }
                    if (mIsBeingDragged && canDragView(child)) {
                        ev.setLocation(ev.getX(), ev.getY() - child.getY());
                        child.onTouchEvent(ev);
                        // 缓解 触发 品牌平铺横向滑动 问题
                        ev.setLocation(mLastMotionDownX, y);
                        /*String value = JDMobileConfig.getInstance().getConfig("JDSearch", "recyclerViewFullScreen", "fullScreen");
                        if (!TextUtils.equals(Constant.ZERO_STRING,value)){
                            parent.requestDisallowInterceptTouchEvent(false);
                        }*/
                        if (mIsBeingDragged) {
                            return false;
                        }
                    }
                    break;
                }
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP: {
                    if (mIsBeingDragged) {
                        mIsBeingDragged = false;
                        if (canDragView(child) && parent.getContext() instanceof Activity) {
                            ProductListHeaderHelper.handleHeaderViewsStatus((Activity) parent.getContext());
                        }
                        return true;
                    }
                    // UP CANCEL 事件也需要recyclerView执行，收尾前面的DOWN事件
                    final int y = (int) ev.getY();
                    ev.setLocation(ev.getX(), ev.getY() - child.getY());
                    child.onTouchEvent(ev);
                    ev.setLocation(ev.getX(), y);
                    mIsBeingDragged = false;
                    break;
                }
                default:
                    break;
            }
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    /**
     * 手指触摸区域是否在底部的霸屏RecyclerView上
     */
    private boolean touchOnUnderRv(float touchY) {
        if (!ViewUtil.isVisible(underRecyclerView)) {
            return false;
        }
        float sortTopY = 0;
        if (mSort != null) { // 霸屏底部区域，排序栏在最顶部
            int[] location = new int[2];
            mSort.getLocationOnScreen(location);
            sortTopY = location[1];
        }
        return touchY < sortTopY;
    }

    /** 判断底层RecyclerView是否还可以上滑*/
    private boolean underRecyclerViewCanScrollVertically() {
        return ViewUtil.isVisible(underRecyclerView) && underRecyclerView.canScrollVertically(1);
    }

    /** 当手指触摸到底部霸屏的RecyclerView时，执行排序栏的吸低动画*/
    private void doTransByUnderRvTouch() {
        // 当正在执行动画或者底部RecyclerView已经画到底时不再执行动画
        if (underRvBottomY == 0 || doingAnimation || !underRecyclerView.canScrollVertically(1)) {
            return;
        }
        if (mSort != null && mSort.getTranslationY() < underRvBottomY) {
            aniTranslateY(mSort, underRvBottomY);
            aniTranslateY(mScene, DisplayUtil.getScreenHeight());
            aniTranslateY(mFilterButtons, DisplayUtil.getScreenHeight());
            aniTranslateY(mContentSceneTabButtons, DisplayUtil.getScreenHeight());
        }
    }

    /** 是否可以执行动画*/
    public static boolean canDoAnimation = true;
    /** 是否正在执行吸低位移动画*/
    private boolean doingAnimation;
    /** 执行位移动画*/
    private void aniTranslateY(View view, float transY) {
        if (view == null) {
            return;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "y", transY);
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (!doingAnimation) {
                    doingAnimation = true;
                }
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (doingAnimation) {
                    doingAnimation = false;
                }
            }
        });
        animator.start();
    }

    /**
     * Return true if the view can be dragged.
     */
    private boolean canDragView(View view) {
        return view != null && view.isShown() && !canFingerDown(view);
    }

    /**
     * 手指可以下滑操作
     * @return 手指是否可以下滑动
     */
    protected boolean canFingerDown(View view) {
        return view.canScrollVertically(-1);
    }

}
