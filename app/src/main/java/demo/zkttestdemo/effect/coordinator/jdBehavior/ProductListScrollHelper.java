package demo.zkttestdemo.effect.coordinator.jdBehavior;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.jd.lib.search.core.constants.Constant;
import com.jd.lib.search.core.utils.ProductListUtil;
import com.jd.lib.search.view.Activity.ProductListActivity;

/**
 * 列表滚动，帮助类
 *
 * @author duanwenqiang1
 * @date 2018/6/19
 */

public class ProductListScrollHelper {

    /** ProductListActivity **/
    private ProductListActivity mActivity;
    /**记录列表上次滑动状态**/
    private boolean lastState;

    /**
     * 构造方法
     * @param activity
     */
    public ProductListScrollHelper(ProductListActivity activity) {
        mActivity = activity;
    }


    /**
     * 滚动状态改变处理
     * @param recyclerView
     * @param newState
     */
    public void onScrollStateChanged(RecyclerView recyclerView, int newState, boolean isSameOtherTabClick) {
        if (mActivity == null) {
            return;
        }
        mActivity.onScrollStateChanged(recyclerView,newState,isSameOtherTabClick);
        switch (newState) {
            case RecyclerView.SCROLL_STATE_DRAGGING:// 滚动开始时
                lastState = true;
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:// 滚动滑行时/自动滚动开始
                lastState = false;
                break;
            case RecyclerView.SCROLL_STATE_IDLE:// 滚动停止时
                handleScrollStopped(recyclerView, lastState);
                break;
            default:
                break;
        }
    }

    /**
     * 处理滚动停止
     * @param recyclerView
     */
    private void handleScrollStopped(RecyclerView recyclerView, boolean state) {
        //手指不能向下滑动时，打开顶部
        if (!recyclerView.canScrollVertically(-1) && !state) {
            ProductListHeaderHelper.handleHeaderViewsStatus(mActivity);
        }
    }

    /**
     * 获取 RecyclerView 第一个可见条目的位置
     * @param layoutManager RecyclerView 布局管理器
     * @return 位置
     */
    public static int getFirstVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] firstVisibleItems = null;
            firstVisibleItems = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(firstVisibleItems);
            if (firstVisibleItems != null && firstVisibleItems.length >= 1) {
                return firstVisibleItems[0];
            }
        }
        return 0;
    }

    /**
     * 获取 RecyclerView 最后一个可见条目的位置
     *
     * @param layoutManager RecyclerView 布局管理器
     * @return 位置
     */
    public static int getLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItems = null;
            lastVisibleItems = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastVisibleItems);
            if (lastVisibleItems != null && lastVisibleItems.length >= 1) {
                return lastVisibleItems[0];
            }
        }
        return 0;
    }

    /**
     * 获取当前滑动到第几页
     * @param recyclerView
     * @param productSize
     * @return
     */
    public static int getCurrentPage(RecyclerView recyclerView, int productSize){
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int firstVisibleItem = ProductListScrollHelper.getFirstVisibleItemPosition(layoutManager);

        if (productSize > 0) {
            int currentNum = (firstVisibleItem + visibleItemCount);
            if (currentNum > productSize) {
                currentNum = productSize;
            }
            //页码精确计算
            int currentPage = ProductListUtil.ceil(currentNum,  Constant.PAGE_SIZE);
            return currentPage;
        }
        return  -1;
    }

}
