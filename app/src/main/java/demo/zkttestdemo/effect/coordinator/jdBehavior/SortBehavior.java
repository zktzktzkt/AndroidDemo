package demo.zkttestdemo.effect.coordinator.jdBehavior;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

import com.jd.lib.search.core.constants.Constant;
import com.jd.lib.search.view.Activity.ProductListActivity;
import com.jd.lib.search.view.baseview.sort.ProductListSortTab;


/**
 * 排序栏 behavior
 *
 *<LinearLayout
 *    android:id="@+id/sort_buttons_layout"
 *    android:layout_width="match_parent"
 *    android:layout_height="wrap_content"
 *    android:orientation="horizontal"
 *    app:layout_behavior="@string/lib_search_behavior_view_sort">
 *
 *    <com.jd.lib.search.view.baseview.sort.ProductListSortTab
 *        android:id="@+id/sort_buttons_layout_inner"
 *        android:layout_width="match_parent"
 *        android:layout_height="wrap_content"
 *        android:visibility="gone" />
 *</LinearLayout>
 *
 * @author duanwenqiang1
 * @date 2018/6/5
 */
public class SortBehavior extends BaseHeaderBehavior<ProductListSortTab> {


    /**
     * 构造方法
     * @param context context
     * @param attrs attrs
     */
    public SortBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getMinHeaderOffsetRange() {
        return Constant.STATUS_BAR_HEIGHT - mSortViewHeight;
    }

    @Override
    protected int getMaxHeaderOffsetRange() {
        if (mTargetView != null && !canFingerDown()) {
            return getTitleOpenOffset() + mBannerViewHeight;
        }
        return mTitleViewHeight;
    }



    @Override
    protected void handleNestedPreScroll(int dy, int[] consumed, float translationY) {

        //滑动
        translationYForChild(translationY - dy);
        //消耗掉当前垂直方向上的滑动距离
        consumed[1] = dy;

        /*if (dy > 0) {
            //滑动
            translationYForChild(translationY - dy);
            //消耗掉当前垂直方向上的滑动距离
            consumed[1] = dy;
        } else if (dy < 0) {
            if (!canFingerDown() && mBannerViewHeight != 0) {
                //滑动
                translationYForChild(translationY - dy);
                //消耗掉当前垂直方向上的滑动距离
                consumed[1] = dy;
            } else if (translationY <= mTitleViewHeight) {
                if (translationY != mTitleViewHeight) {
                    //滑动
                    float max = mTitleViewHeight;
                    if (translationY - dy > max) {
                        translationYForChild(max);
                    } else {
                        translationYForChild(translationY - dy);
                    }
                    //消耗掉当前垂直方向上的滑动距离
                    consumed[1] = dy;
                }
            } else {
                translationYForChild(mTitleViewHeight);
            }
        }*/
    }

    /**
     * 该方法可能调用两次，
     * 第一次是下滑抬手fling的时候
     * 第二次是监听rv滑动停止时，如果滑动到头了，还会触发头部展开的逻辑，{@link ProductListScrollHelper#handleScrollStopped}
     *
     * @param curTranslationY
     * @return
     */
    @Override
    protected float getOpenOffset(float curTranslationY) {
        if (mChild == null) {
            return 0;
        }
        //正常情况下，rv已经滑了一段距离了，不是处在顶部，所以展开的距离没有mBannerViewHeight这段
        float dy = mTitleViewHeight - curTranslationY;

        //rv滑动到顶了，再往下拖就有banner区域了，所以需要加上mBannerViewHeight
        if (!canFingerDown()) {
            dy = getTitleOpenOffset() + mBannerViewHeight - curTranslationY;
        }
        return dy;
    }

    @Override
    protected void translationYForChild(float translationY) {
        super.translationYForChild(translationY);
        /*if (translationY < (mTitleViewHeight + Constant.STATUS_BAR_HEIGHT) && mContext instanceof ProductListActivity) {
            ((ProductListActivity) mContext).stopVideoAndHide();
        }
        if (translationY == getMinHeaderOffsetRange()){
            ((ProductListActivity) mContext).showHourReachFeedsLine(true);
        }else{
            ((ProductListActivity) mContext).showHourReachFeedsLine(false);
        }*/
    }
}
