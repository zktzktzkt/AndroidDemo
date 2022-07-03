package demo.zkttestdemo.overscroll;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * recyclerview 越界回弹
 */
public class OverScrollLayout extends LinearLayout {
    private static final int ANIM_TIME = 400;
    private RecyclerView childView;
    private Rect original = new Rect();
    private boolean isMoved = false;
    private float startYpos;
    private float startXpos;
    /**
     * 阻尼系数
     */
    private static final float DAMPING_COEFFICIENT = 0.3f;
    private boolean isSuccess = false;
    private ScrollListener mScrollListener;
    private ScrollOrientation scrollOrientation = ScrollOrientation.VERTICAL;

    /**
     * 滚动方向
     */
    public enum ScrollOrientation {
        VERTICAL, HORIZONTAL
    }

    public OverScrollLayout(Context context) {
        this(context, null);
    }

    public OverScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        childView = (RecyclerView) getChildAt(0);
        childView.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        original.set(childView.getLeft(), childView.getTop(), childView.getRight(), childView.getBottom());
    }

    public void setScrollListener(ScrollListener listener) {
        mScrollListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float touchYpos = ev.getY();
        if (touchYpos >= original.bottom || touchYpos <= original.top) {
            if (isMoved) {
                recoverLayout();
            }
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startXpos = ev.getX();
                startYpos = ev.getY();
            case MotionEvent.ACTION_MOVE:
                int scrollYpos = (int) (ev.getY() - startYpos);
                int scrollXpos = (int) (ev.getX() - startXpos);
                boolean pullLeft = scrollXpos < 0 && canPullLeft();
                boolean pullRight = scrollXpos > 0 && canPullRight();
                boolean pullDown = scrollYpos > 0 && canPullDown();
                boolean pullUp = scrollYpos < 0 && canPullUp();

                if (scrollOrientation == ScrollOrientation.VERTICAL && (pullDown || pullUp)) {
                    cancelChild(ev);
                    int offset = (int) (scrollYpos * DAMPING_COEFFICIENT);
                    childView.layout(original.left, original.top + offset, original.right, original.bottom + offset);
                    if (mScrollListener != null) {
                        mScrollListener.onScroll();
                    }
                    isMoved = true;
                    isSuccess = false;
                    return true;

                } else if (scrollOrientation == ScrollOrientation.HORIZONTAL && (pullRight || pullLeft)) {
                    cancelChild(ev);
                    int offset = (int) (scrollXpos * DAMPING_COEFFICIENT);
                    childView.layout(original.left + offset, original.top, original.right + offset, original.bottom);
                    if (mScrollListener != null) {
                        mScrollListener.onScroll();
                    }
                    isMoved = true;
                    isSuccess = false;
                    return true;

                } else {
                    startXpos = ev.getX();
                    startYpos = ev.getY();
                    isMoved = false;
                    isSuccess = true;
                    //没滑动到头，就继续往下分发事件
                    return super.dispatchTouchEvent(ev);
                }
            case MotionEvent.ACTION_UP:
                if (isMoved) {
                    recoverLayout();
                }
                //只要我在移动，就不往下分发事件
                return !isSuccess || super.dispatchTouchEvent(ev);
            default:
                return true;
        }
    }

    /**
     * 设置滚动的方向
     *
     * @param scrollOrientation Gravity.
     */
    public void setOrientation(ScrollOrientation scrollOrientation) {
        this.scrollOrientation = scrollOrientation;
    }

    /**
     * 取消子view已经处理的事件     *     * @param ev event
     */
    private void cancelChild(MotionEvent ev) {
        ev.setAction(MotionEvent.ACTION_CANCEL);
        super.dispatchTouchEvent(ev);
    }

    /**
     * 位置还原
     */
    private void recoverLayout() {
        TranslateAnimation anim;
        if (scrollOrientation == ScrollOrientation.VERTICAL) {
            anim = new TranslateAnimation(0, 0, childView.getTop() - original.top, 0);
        } else {
            anim = new TranslateAnimation(childView.getLeft() - original.left, 0, 0, 0);
        }
        anim.setDuration(ANIM_TIME);
        childView.startAnimation(anim);
        childView.layout(original.left, original.top, original.right, original.bottom);
        isMoved = false;
    }

    /**
     * 判断是否可以下拉
     *
     * @return true：可以，false:不可以
     */
    private boolean canPullDown() {
        final int firstVisiblePosition = ((LinearLayoutManager) childView.getLayoutManager()).findFirstVisibleItemPosition();
        if (firstVisiblePosition != 0 && childView.getAdapter().getItemCount() != 0) {
            return false;
        }
        int mostTop = (childView.getChildCount() > 0) ? childView.getChildAt(0).getTop() : 0;
        return mostTop >= 0;
    }

    /**
     * 判断是否可以上拉
     *
     * @return true：可以，false:不可以
     */
    private boolean canPullUp() {
        final int lastItemPosition = childView.getAdapter().getItemCount() - 1;
        final int lastVisiblePosition = ((LinearLayoutManager) childView.getLayoutManager()).findLastVisibleItemPosition();
        if (lastVisiblePosition >= lastItemPosition) {
            final int childIndex = lastVisiblePosition - ((LinearLayoutManager) childView.getLayoutManager()).findFirstVisibleItemPosition();
            final int childCount = childView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = childView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= childView.getBottom() - childView.getTop();
            }
        }
        return false;
    }

    /**
     * 判断是否可以右拉
     *
     * @return true：可以，false:不可以
     */
    private boolean canPullRight() {
        final int firstVisiblePosition = ((LinearLayoutManager) childView.getLayoutManager()).findFirstVisibleItemPosition();
        if (firstVisiblePosition != 0 && childView.getAdapter().getItemCount() != 0) {
            return false;
        }
        int mostTop = (childView.getChildCount() > 0) ? childView.getChildAt(0).getLeft() : 0;
        return mostTop >= 0;
    }

    /**
     * 判断是否可以左拉
     *
     * @return true：可以，false:不可以
     */
    private boolean canPullLeft() {
        final int lastItemPosition = childView.getAdapter().getItemCount() - 1;
        final int lastVisiblePosition = ((LinearLayoutManager) childView.getLayoutManager()).findLastVisibleItemPosition();
        if (lastVisiblePosition >= lastItemPosition) {
            final int childIndex = lastVisiblePosition - ((LinearLayoutManager) childView.getLayoutManager()).findFirstVisibleItemPosition();
            final int childCount = childView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = childView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getRight() <= childView.getRight() - childView.getLeft();
            }
        }
        return false;
    }

    public interface ScrollListener {
        /**
         * 滚动事件回调
         */
        void onScroll();
    }
}
