package demo.zkttestdemo.effect.coordinator.jdBehavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * 顶部布局behavior父布局
 *
 * @author duanwenqiang1
 * @date 2018/6/5
 */
public abstract class BaseHeaderBehavior<ChildView extends View> extends ViewOffsetBehavior<ChildView> {

    /** recyclerView **/
    protected View mTargetView;
    /** 当前behavior对应的布局 **/
    protected ChildView mChild;

    /** banner **/
    protected View mBanner;

    /** 搜索框高度 **/
    protected int mTitleViewHeight = 0;
    /** 底部霸屏recyclerview高度 **/
    protected int mNewTopHeight = 0;
    /** banner高度 **/
    protected int mBannerViewHeight = 0;
    /** 排序栏高度 **/
    protected int mSortViewHeight = 0;
    /** 新排序栏高度 **/
    protected int mSceneSortViewHeight = 0;
    /** 筛选外漏高度 **/
    protected int mFilterButtonsViewHeight = 0;
    /** 筛选外漏高度 **/
    protected int mContentSceneTabViewHeight = 0;

    /** OverScroller自动滚动 **/
    private OverScroller mOverScroller;
    /** 滚动时长 **/
    public static final int DURATION_SHORT = 400;
    /** 是否是向上滑动 **/
    public static boolean mIsFlingUp;

    /** context*/
    protected Context mContext;

    /**
     * 是否计算标题栏宽度
     */
    protected boolean isNoTitleMode = false;
    /** 顶部标题控件*/
    protected ProductListTitleView titleView;
    /**是否是新型顶部**/
    private   boolean newTop;
    /**是否显示顶部**/
    protected    boolean showInTitle;
    /**932是scene tabs style */
    protected boolean isSceneTabsStyle;
    /** 滑动临界值 **/
    private int mTouchSlop;
    /**
     * 构造方法
     * @param context context
     * @param attrs   attrs
     */
    public BaseHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mOverScroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, ChildView child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);
        initViewHeight(parent);
    }

    /**
     * 设置品专新模式
     * @param isNoTitleMode
     */
    public void setNoTitleMode(boolean isNoTitleMode) {
        this.isNoTitleMode = isNoTitleMode;
    }

    /**
     * 初始化view高度
     * @param parent parent
     */
    private void initViewHeight(CoordinatorLayout parent) {
        /*mBanner = parent.findViewById(R.id.product_list_top_banner_view);
        mTitleViewHeight = Utils.getViewHeight(parent.findViewById(R.id.product_list_search_title));
        mNewTopHeight = Utils.getViewHeight(parent.findViewById(R.id.new_top));
        ////用viewutl.getviewheight以后 再设置高度的话 就不起作用了
        //mNewTopHeight = ViewUtil.getViewHeight(parent.findViewById(R.id.new_top));
        mSceneSortViewHeight = Utils.getViewHeight(parent.findViewById(R.id.scene_sort_layout));
        if (parent.getContext() != null) {
            if (parent.getContext() instanceof ProductListActivity) {
                ProductListActivity productListActivity = ((ProductListActivity) parent.getContext());
                newTop = productListActivity.newtops;
                isSceneTabsStyle = productListActivity.getActivityDataState().isSceneTabsStyle;
                superEntity = productListActivity.superEntity;
                showInTitle = productListActivity.showInTilte;
            }
        }*/

        /*int topMargin = -1;

        if (showInTitle) {
            mSortViewHeight = 0;
        } else {
            mSortViewHeight = Utils.getViewHeight(parent.findViewById(R.id.sort_buttons_layout));
        }*/

        /*if (newTop) {
            topMargin = DisplayUtil.getScreenHeight() - mSortViewHeight;
            if (superEntity != null && mNewTopHeight <= topMargin) {
                mBannerViewHeight = mNewTopHeight;
            } else {
                mBannerViewHeight = topMargin;
            }
        } else {
            mBannerViewHeight = Utils.getViewHeight(mBanner);
        }*/

//        mFilterButtonsViewHeight = Utils.getViewHeight(parent.findViewById(R.id.filter_buttons_undertabs_layout));
//        mContentSceneTabViewHeight = Utils.getViewHeight(parent.findViewById(R.id.content_scene_buttons_undertabs_layout));
    }

    /**
     * 该方法的返回值决定是否执行嵌套滑动
     * @param child 当前Behavior关联的View
     * @param nestedScrollAxes 滑动方向轴
     * @return
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, ChildView child, View directTargetChild, View target, int nestedScrollAxes) {
        slideY = 0;
        mTargetView = target;
        mChild = child;
        // 开始滑动的条件，垂直方向滑动，滑动未结束
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL && canScroll(child.getTranslationY(), 0);
    }

    /**
     * 当前是否可以滑动，当Behavior关联的View的Y轴偏移量已经小于最小偏移量或者大于最大偏移量时，将不再响应嵌套滑动
     * @param translationY translationY
     * @param pendingDy Y轴方向滑动的translationY
     * @return 否可以滑动
     */
    private boolean canScroll(float translationY, float pendingDy) {
        float pendingTranslationY = translationY - pendingDy;
        if (pendingTranslationY >= getMinHeaderOffsetRange() &&
                pendingTranslationY <= getMaxHeaderOffsetRange()) {
            return true;
        }
        return false;
    }

    /**
     * 获取展开时顶部title的偏移量
     * @return
     */
    protected int getTitleOpenOffset() {
        return isNoTitleMode ? 0 : mTitleViewHeight;
    }

    /**
     * 向上滑动过程时translationY的最小值
     * @return
     */
    protected abstract int getMinHeaderOffsetRange();

    /**
     * 向下滑动过程时translationY的最大值
     * @return
     */
    protected abstract int getMaxHeaderOffsetRange();

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, ChildView child, View target, float velocityX, float velocityY) {
        handleActionUp(child);
        //头部收起了，即手指上滑操作可以导致列表内容移动了（而不是收起头）
        boolean isCloseHeader;
        //筛选外漏
        /*View filterOutView = coordinatorLayout.findViewById(R.id.filter_buttons_undertabs_layout);
        if (filterOutView != null && filterOutView.getVisibility() == View.VISIBLE) {
            //recyclerview的顶部和筛选外漏的底部挨住时（加了一个10px的误差），认为手指上滑操作可以导致列表内容移动了（而不是收起头）
            isCloseHeader = Math.abs(target.getY() - (Constant.STATUS_BAR_HEIGHT + filterOutView.getHeight())) < 10;
        } else {
            isCloseHeader = Math.abs(target.getY() - Constant.STATUS_BAR_HEIGHT) < 10;
        }*/
        //该次fling将会滚动的距离
        double dis = FlingUtil.getDis((int) velocityY, child.getContext());
        int helfHeight = DisplayUtil.getScreenHeight() / 2;
        //手指上滑&&header没有收回且&&会滑行的距离小于屏幕一半时，禁止rv的fling，return true.
        return velocityY > 0 && /*!isCloseHeader &&*/ dis < helfHeight;
    }


    /** 当前滑动距离 **/
    private int slideY = 0;

    @Override
    public void onNestedPreScroll(@NotNull CoordinatorLayout coordinatorLayout, @NotNull ChildView child, @NotNull View target, int dx, int dy, @NotNull int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        // 首次
        if (slideY == 0) {
            mIsFlingUp = dy > 0;
        }
        slideY += dy;
        if (Math.abs(slideY) >= mTouchSlop) {
            mIsFlingUp = slideY > 0;
            slideY = dy;
        }
        // dy>0 向上滑
        // dy<0 向下滑
        float translationY = child.getTranslationY();
        if (!canScroll(translationY, dy)) { // 滑动结束
            onScrollTop(dy > 0);
            if (dy > 0) {
                // 保证View的偏移量大于等于最小的偏移量
                if (translationY != getMinHeaderOffsetRange()) {
                    translationYForChild(getMinHeaderOffsetRange());
                }
            } else {
                // 保证View的偏移量小于等于最大的偏移量
                if (translationY != getMaxHeaderOffsetRange()) {
                    translationYForChild(getMaxHeaderOffsetRange());
                }
            }
        } else { // 滑动未结束
            handleNestedPreScroll(dy, consumed, translationY);
        }
    }

    /**
     * 处理滑动事件消费
     * @param dy       滑动距离  dy>0 手指向上滑 dy<0 手指向下滑
     * @param consumed 消费值
     */
    protected abstract void handleNestedPreScroll(int dy, int[] consumed, float translationY);

    /**
     * 处理起手后滚动，展开或关闭头部，取决于mIsFlingUp变量
     * @param child View
     */
    public void handleActionUp(ChildView child) {
        if (mFlingRunnable != null) {
            child.removeCallbacks(mFlingRunnable);
            mFlingRunnable = null;
        }

        if (titleView != null) {
            titleView.isOpeningOrClosing = true;
            titleView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (titleView != null) {
                        titleView.isOpeningOrClosing = false;
                    }
                }
            }, DURATION_SHORT + 50);
        }

        mFlingRunnable = new FlingRunnable(child, mIsFlingUp);
        mFlingRunnable.scrollToClosedOrOpen(DURATION_SHORT);

        /*if (mIsFlingUp && mContext instanceof ProductListActivity) {
            ((ProductListActivity) mContext).stopVideoAndHide();
        }*/
    }

    /**
     * 主动进行展开或者关闭顶部布局
     * @param child View
     */
    public void scrollToOpenOrClose(ChildView child, boolean isOpen) {
        if (mFlingRunnable != null) {
            child.removeCallbacks(mFlingRunnable);
            mFlingRunnable = null;
        }
        mFlingRunnable = new FlingRunnable(child, !isOpen);
        mFlingRunnable.scrollToClosedOrOpen(DURATION_SHORT);
    }


    /**
     * 自动展开时距离
     * @return 距离
     */
    protected abstract float getOpenOffset(float curTranslationY);

    /** 滑动起手后滚动执行任务 **/
    private FlingRunnable mFlingRunnable;

    private class FlingRunnable implements Runnable {
        /** 控件view **/
        private final ChildView mLayout;
        /** 是否向上滚动（关闭） **/
        private final boolean mScrollUp;

        /**
         * 构造方法
         * @param layout   layout
         * @param scrollUp scrollUp 是否为向上滑（关闭）
         */
        FlingRunnable(ChildView layout, boolean scrollUp) {
            mLayout = layout;
            mScrollUp = scrollUp;
        }

        /**
         * 合起或者关闭
         *
         * @param duration 时长
         */
        public void scrollToClosedOrOpen(int duration) {
            float curTranslationY = ViewCompat.getTranslationY(mLayout);
            if (mScrollUp) {
                float dy = getMinHeaderOffsetRange() - curTranslationY;
                mOverScroller.startScroll(0, Math.round(curTranslationY - 0.1f), 0, Math.round(dy + 0.1f), duration);

            } else {
                mOverScroller.startScroll(0, (int) curTranslationY, 0, Math.round(getOpenOffset(curTranslationY) + 0.1f), duration);
            }
            start(mScrollUp);
        }

        /**
         * 执行
         */
        private void start(boolean scrollUp) {
            if (mOverScroller.computeScrollOffset()) {
                mFlingRunnable = new FlingRunnable(mLayout, scrollUp);
                ViewCompat.postOnAnimation(mLayout, mFlingRunnable);
            }
        }


        @Override
        public void run() {
            if (mTargetView instanceof RecyclerView) {
                int childCount = ((RecyclerView) mTargetView).getChildCount();
                if (childCount == 0) {
                    //尝试解决 搜索奶粉（带banner），然后点中部标签词，偶尔会有 loading过程中屏幕header全部消失的bug。
                    //是因为loading过程中，应该recyclerview隐藏，header归位，但是mOverScroller动画还没走完，再次操作了
                    //mLayout（header），所以header全到了屏幕外，所以如果childCount=0时要中断动画
                    mOverScroller.abortAnimation();
                }
            }
            if (mLayout != null && mOverScroller != null) {
                if (mOverScroller.computeScrollOffset()) {
                    int max = getMaxHeaderOffsetRange();
                    int min = getMinHeaderOffsetRange();
                    int currY = mOverScroller.getCurrY();
                    int translationY = currY > max ? max : currY;
                    if (isNoTitleMode) {
                        if (currY < min) {
                            translationY = min;
                        }
                    }
                    translationYForChild(translationY, mLayout);
                    ViewCompat.postOnAnimation(mLayout, this);
                } else {
                    onScrollTop(mScrollUp);
                }
            }
        }
    }



    /**
     * 滚动回调
     * @param up 是否向上
     */
    protected void onScrollTop(boolean up) {

    }

    /**
     *  1.滚动到顶部  Banner由折叠状态逐步展开
     * or 2.滚动到顶部  Banner处于隐藏状态
     * @return 状态
     */
    protected boolean isScrolledTop() {
        return !canFingerDown()
                && (mBannerViewHeight != 0 || !mBanner.isShown());
    }

    /**
     * 修改childview的translationY
     * @param translationY translationY
     */
    protected void translationYForChild(float translationY) {
        translationYForChild(translationY, this.mChild);
    }

    /**
     * 修改childview的translationY
     * @param translationY translationY
     * @param childView    childView（behavior作用的view）
     */
    protected void translationYForChild(float translationY, ChildView childView) {
        childView.setTranslationY(translationY);
    }

    /**
     * 手指可以下滑操作
     * @return 手指是否可以下滑动
     */
    protected boolean canFingerDown(){
        if (mTargetView == null) {
            return true;
        }
        return mTargetView.canScrollVertically(-1);
    }
}


