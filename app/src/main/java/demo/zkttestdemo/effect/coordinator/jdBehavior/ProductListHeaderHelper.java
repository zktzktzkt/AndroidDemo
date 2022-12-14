package demo.zkttestdemo.effect.coordinator.jdBehavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jd.lib.search.R;
import com.jd.lib.search.core.model.entity.productlist.SuperConfigEntity;
import com.jd.lib.search.view.Activity.ProductListActivity;
import com.jd.lib.search.view.behavior.FrameLayoutBehavior;
import com.jingdong.common.BaseActivity;
import com.jingdong.sdk.oklog.OKLog;

/**
 * 列表页顶部布局帮助类
 * 返回顶部按钮点击后，展开头部
 * 列表滚动停止，展开后收起头部
 * @author duanwenqiang1
 * @date 2018/7/12
 */
public class ProductListHeaderHelper {

    /**
     * 顶部高度总和
     * @param content view
     * @return 顶部高度
     */
    public static int getTopLayoutHeight(View content) {
        if (content != null
                && content.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) content.getLayoutParams();
            CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
            if (behavior instanceof RecyclerViewBehavior) {
                return ((RecyclerViewBehavior) behavior).getTotalHeightOfHeaders();
            }
        }
        return 0;
    }

    /**
     * 场景tab顶部高度总和
     * @param content view
     * @return 顶部高度
     */
    public static int getSceneTabTopLayoutHeight(View content) {
        if (content != null
                && content.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) content.getLayoutParams();
            CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
            if (behavior instanceof RecyclerViewBehavior) {
                return ((RecyclerViewBehavior) behavior).getScenceTabTotalHeightOfHeaders();
            }
        }
        return 0;
    }

    /**
     * 列表加载更多情况，头部不再重置位置；
     * 任何情况下如果出现头部闪动，一定要先尝试这个方法，设置needPost为false；
     * @param context BaseActivity
     */
    public static void setHeaderFixed(boolean needPost, Context context) {
        if (needPost) {
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).post(new Runnable() {
                    @Override
                    public void run() {
                        //第一页需要post，等待
                        // 数据加载后，滚动时，不再重置顶部view，
                        // 延后是为了等控件都根据数据加载完成展示
                        RecyclerViewBehavior.setIsNeedRestHeader(false);
                    }
                });
            }
        } else {
            //翻页时不会影响顶部试图，所以不用post
            //翻页post会在某些情况下，导致顶部闪动
            RecyclerViewBehavior.setIsNeedRestHeader(false);
        }
    }

    /**
     * 切换tab时调用；
     * @param context BaseActivity
     */
    public static void setIsHourReachFeeds(boolean needPost, Context context) {
        if (needPost) {
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).post(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerViewBehavior.setIsHourReachFeeds(true);
                    }
                });
            }
        } else {
            RecyclerViewBehavior.setIsHourReachFeeds(true);
        }
    }

    /**
     * 列表加载更多情况，头部不再重置位置；
     * 任何情况下如果出现头部闪动，一定要先尝试这个方法，设置needPost为false；
     * @param context BaseActivity
     */
    public static void setFrameLayoutHeaderFixed(boolean needPost, Context context) {
        if (needPost) {
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).post(new Runnable() {
                    @Override
                    public void run() {
                        //第一页需要post，等待
                        // 数据加载后，滚动时，不再重置顶部view，
                        // 延后是为了等控件都根据数据加载完成展示
                        FrameLayoutBehavior.setIsNeedRestHeader(false);
                    }
                });
            }
        } else {
            //翻页时不会影响顶部试图，所以不用post
            //翻页post会在某些情况下，导致顶部闪动
            FrameLayoutBehavior.setIsNeedRestHeader(false);
        }
    }


    /**
     * 处理顶部布局，特型词动效 & 顶部布局
     * @param activity Activity
     */
    public static void handleHeaderViewsStatus(final Activity activity) {
        if (activity == null) {
            return;
        }
        View recyclerView = activity.findViewById(R.id.product_list);
        if (recyclerView != null && ViewUtil.isVisible(recyclerView)) {
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    openOrCloseHeadersByActionUp(activity);
                }
            }, 10);
        }
    }

    /**
     * 搜索落地页 设置新旧广告样式 设置品专店铺是否通到title上面
     * @param activity activity
     */
    public static void setNoTitleModes(Activity activity, boolean isNoTitleMode) {
        if (activity == null) {
            return;
        }
        setNoTitleMode(activity.findViewById(R.id.product_list_search_title), isNoTitleMode);
        setNoTitleMode(activity.findViewById(R.id.product_list_top_banner_view), isNoTitleMode);
        setNoTitleMode(activity.findViewById(R.id.sort_buttons_layout), isNoTitleMode);
        setNoTitleMode(activity.findViewById(R.id.scene_sort_layout), isNoTitleMode);
        setNoTitleMode(activity.findViewById(R.id.filter_buttons_undertabs_layout), isNoTitleMode);
        setNoTitleMode(activity.findViewById(R.id.product_list_channel_tip), isNoTitleMode);
    }

    /**
     * 顶部升起动画
     * @param needJudge 需要判断播放视频时是否移动过，移动过就不重新调整位置
     */
    public static void translateFromBottom(final ProductListActivity activity, final int duration, SuperConfigEntity superConfigEntity, boolean needJudge) {
        if (activity == null || activity.findViewById(R.id.new_top) == null) {
            return;
        }
        activity.findViewById(R.id.new_top).postDelayed(new Runnable() {
            @Override
            public void run() {
                float sortHeight = ViewUtil.getViewHeight(activity.findViewById(R.id.sort_buttons_layout));
                float sceneHeight = ViewUtil.isVisible(activity.findViewById(R.id.scene_sort_layout)) ? DisplayUtil.dp2px(36) : 0;
                float filterHeight = ViewUtil.getViewHeight(activity.findViewById(R.id.filter_buttons_undertabs_layout));
                float mNewTopHeight = Utils.getViewHeight(activity.findViewById(R.id.new_top));
                if (OKLog.D) {
                    //用viewutl.getviewheight以后 再设置的话 就不起作用了
                    OKLog.d("testBasebehavior", "---mNewTopHeight==" + mNewTopHeight);
                }
                float sortEnd;
                if (needJudge) {
                    sortEnd = DisplayUtil.getScreenHeight() - (activity.showInTilte ? 0 : sortHeight);
                    if (sortEnd != activity.findViewById(R.id.sort_buttons_layout).getTranslationY()) {
                        return;
                    }
                }
                float midMargin = DisplayUtil.getScreenHeight() * 3 / 4;
                //京东金榜 只有一个楼层
                if (superConfigEntity != null && superConfigEntity.isGoldRankStyle()){
                    sortEnd = superConfigEntity.firstFloorHeight;
                }else {
                    if (mNewTopHeight<=midMargin){
                        sortEnd = mNewTopHeight;
                    }else {
                        sortEnd = midMargin;
                    }
                }
                final float channelEnd = sortEnd + sortHeight + sceneHeight + filterHeight;
                final float filterEnd = sortEnd + sortHeight + sceneHeight;
                final float sceneEnd = sortEnd + sortHeight;
                final float finalSortEnd = sortEnd;


                try {
                    ((RecyclerView)activity.findViewById(R.id.product_list)).scrollToPosition(0);
                } catch (Exception e) {

                }
                translateFromBottom(activity.findViewById(R.id.sort_buttons_layout), duration, finalSortEnd,activity);
                translateFromBottom(activity.findViewById(R.id.scene_sort_layout), duration, sceneEnd,activity);
                translateFromBottom(activity.findViewById(R.id.filter_buttons_undertabs_layout), duration, filterEnd,activity);
                translateFromBottom(activity.findViewById(R.id.product_list_channel_tip), duration, channelEnd,activity);
            }
        }, 1001);
    }

    /**
     * 顶部升起动画
     */
    public static void translateFromBottomHaveVideo(final ProductListActivity activity, final int duration) {
        if (activity == null) {
            return;
        }
        float sortHeight = ViewUtil.getViewHeight(activity.findViewById(R.id.sort_buttons_layout));
        float sceneHeight = ViewUtil.isVisible(activity.findViewById(R.id.scene_sort_layout))? DisplayUtil.dp2px(36) : 0;
        float filterHeight = ViewUtil.getViewHeight(activity.findViewById(R.id.filter_buttons_undertabs_layout));
        int mSortViewHeight = 0;
        if (activity.showInTilte) {
            mSortViewHeight = 0;
        } else {
            mSortViewHeight = (int) sortHeight;
        }

        float sortEnd = DisplayUtil.getScreenHeight() - mSortViewHeight;
        final float channelEnd = sortEnd + sortHeight + sceneHeight + filterHeight;
        final float filterEnd = sortEnd + sortHeight + sceneHeight;
        final float sceneEnd = sortEnd + sortHeight;
        final float finalSortEnd = sortEnd;
        activity.post(new Runnable() {
            @Override
            public void run() {
                translateFromBottom(activity.findViewById(R.id.sort_buttons_layout), duration, finalSortEnd,activity);
                translateFromBottom(activity.findViewById(R.id.scene_sort_layout), duration, sceneEnd,activity);
                translateFromBottom(activity.findViewById(R.id.filter_buttons_undertabs_layout), duration, filterEnd,activity);
                translateFromBottom(activity.findViewById(R.id.product_list_channel_tip), duration, channelEnd,activity);
            }
        }, 300);
    }

    /**
     * 获取margin值
     */
    public static int getCountDownMargin(ProductListActivity activity) {

        float sortHeight = ViewUtil.getViewHeight(activity.findViewById(R.id.sort_buttons_layout));
        int sceneHeight = ViewUtil.isVisible(activity.findViewById(R.id.scene_sort_layout))? DisplayUtil.dp2px(36) : 0;
        int mSortViewHeight;
        if (activity.showInTilte) {
            mSortViewHeight = 0;
        } else {
            mSortViewHeight = (int) sortHeight;
        }
        if (activity.getActivityDataState().isSceneTabsStyle) {
            return mSortViewHeight + sceneHeight + DisplayUtil.dp2px(10);
        } else {
            return mSortViewHeight + DisplayUtil.dp2px(44) + DisplayUtil.dp2px(10);
        }
    }

    /**
     * 主动打开或关闭顶部布局，取决于isOpen，区别于openOrCloseHeaderByActionUp
     * @param activity activity
     */
    public static void activeOpenOrCloseHeaders(Activity activity, boolean isOpen) {
        if (activity == null) {
            return;
        }
        activeOpenOrCloseHeader(activity.findViewById(R.id.product_list_search_title), isOpen);
        activeOpenOrCloseHeader(activity.findViewById(R.id.product_list_top_banner_view), isOpen);
        activeOpenOrCloseHeader(activity.findViewById(R.id.sort_buttons_layout), isOpen);
        activeOpenOrCloseHeader(activity.findViewById(R.id.scene_sort_layout), isOpen);
        activeOpenOrCloseHeader(activity.findViewById(R.id.filter_buttons_undertabs_layout), isOpen);
        activeOpenOrCloseHeader(activity.findViewById(R.id.product_list_channel_tip), isOpen);
        activeOpenOrCloseHeader(activity.findViewById(R.id.content_scene_buttons_undertabs_layout), isOpen);
    }

    /**
     * 主动打开或关闭顶部布局，取决于isOpen，区别于openOrCloseHeaderByActionUp
     *
     * @param header header
     */
    public static void activeOpenOrCloseHeader(View header,boolean isOpen) {
        if (header != null
                && header.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) header.getLayoutParams();
            CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
            if (behavior instanceof BaseHeaderBehavior) {
                ((BaseHeaderBehavior) behavior).scrollToOpenOrClose(header, isOpen);
            }
        }
    }

    /**
     * 展开或关闭头部，取决于mIsFlingUp变量（之前的滚动事件决定），区别于activeOpenOrCloseHeaders
     * @param activity activity
     */
    private static void openOrCloseHeadersByActionUp(Activity activity) {
        if (activity == null) {
            return;
        }
        openOrCloseHeaderByActionUp(activity.findViewById(R.id.product_list_search_title));
        openOrCloseHeaderByActionUp(activity.findViewById(R.id.product_list_top_banner_view));
        openOrCloseHeaderByActionUp(activity.findViewById(R.id.sort_buttons_layout));
        openOrCloseHeaderByActionUp(activity.findViewById(R.id.scene_sort_layout));
        openOrCloseHeaderByActionUp(activity.findViewById(R.id.filter_buttons_undertabs_layout));
        openOrCloseHeaderByActionUp(activity.findViewById(R.id.product_list_channel_tip));
    }

    /**
     * 展开或关闭头部，取决于mIsFlingUp变量（之前的滚动事件决定），区别于activeOpenOrCloseHeader
     * @param header header
     */
    private static void openOrCloseHeaderByActionUp(View header) {
        if (header != null
                && header.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) header.getLayoutParams();
            CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
            if (behavior instanceof BaseHeaderBehavior) {
                ((BaseHeaderBehavior) behavior).handleActionUp(header);
            }
        }
    }

    /**
     * 设置品专店铺是否通到title上面
     * @param header header
     */
    private static void setNoTitleMode(View header,boolean isNoTitleMode) {
        if (header != null
                && header.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) header.getLayoutParams();
            CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
            if (behavior instanceof BaseHeaderBehavior) {
                ((BaseHeaderBehavior) behavior).setNoTitleMode(isNoTitleMode);
            }
        }
    }
    /**
     * 动画平移
     */
    public static void translateFromBottom(final View view, int duration, float toY,ProductListActivity activity) {
        if (view == null) {
            return;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", toY);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                RecyclerViewBehavior.canDoAnimation = false;
                if (activity != null) {
                    //霸屏动画结束前 禁止用户点击触摸
                    activity.canTouch = false;
                }
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                RecyclerViewBehavior.canDoAnimation = true;
                if (activity != null) {
                    activity.canTouch = true;
                }

            }
        });
        animator.setDuration(duration);
        animator.start();
    }

}
