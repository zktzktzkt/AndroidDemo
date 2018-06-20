package demo.zkttestdemo.effect.verticaldrag;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ListViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * 仿汽车之家垂直拖拽布局
 * Created by zkt on 2018-1-28.
 */

public class VerticalDrag_Copy extends FrameLayout {

    private View mListView;
    private int mBackViewHeight;
    private ViewDragHelper mViewDragHelper;
    private boolean isOpen = false;
    private float downY;

    public VerticalDrag_Copy(Context context) {
        this(context, null);
    }

    public VerticalDrag_Copy(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDrag_Copy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mViewDragHelper = ViewDragHelper.create(this, mDragCallback);
    }

    private ViewDragHelper.Callback mDragCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == getChildAt(1);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (top > mBackViewHeight) {
                top = mBackViewHeight;
            }
            if (top < 0) {
                top = 0;
            }
            return top;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (releasedChild.getTop() > mBackViewHeight / 2) {
                mViewDragHelper.settleCapturedViewAt(0, mBackViewHeight);
                isOpen = true;
            } else {
                mViewDragHelper.settleCapturedViewAt(0, 0);
                isOpen = false;
            }
            invalidate();
        }
    };

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new RuntimeException("只能包含有两个子View");
        }
        mListView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBackViewHeight = getChildAt(0).getMeasuredHeight();
    }


    /**
     * “拦截是一锤子买卖”，
     * return true拦截了，接下来所有的事件由ViewGroup自己的onTouchEvent处理
     * 默认是return false不拦截的
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 1.菜单打开时，拦截事件，由自己处理
        if (isOpen) {
            return true;
        }

        //2. 菜单关闭时，不拦截，交由子View处理
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                mViewDragHelper.processTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                //滑动到头的时候，拦截事件
                if (ev.getY() - downY > 0 && !canChildScrollUp()) {
                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 必须返回true，否则move事件不会触发，则ViewDragHelper就不会处理move事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    /**
     * 判断View是否滚动到了最顶部。SwipeRefreshLayout里的方法
     */
    public boolean canChildScrollUp() {
        if (mListView instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) mListView, -1);
        }
        return mListView.canScrollVertically(-1);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
