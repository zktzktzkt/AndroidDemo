package demo.zkttestdemo.effect.verticaldrag;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ListViewCompat;
import androidx.customview.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * 仿汽车之家拖拽列表
 */

public class VerticalDragListView extends FrameLayout {
    private ViewDragHelper mDragHelper;
    private View mDragListView;
    //后面菜单的高度
    private int mMenuHeight;
    //菜单是否打开
    private boolean mMemuIsOpen = false;

    public VerticalDragListView(@NonNull Context context) {
        this(context, null);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDragHelper = ViewDragHelper.create(this, mDragHelperCallback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("VerticalDragListView 只能包含两个子布局！");
        }

        mDragListView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMenuHeight = getChildAt(0).getMeasuredHeight();
    }

    private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return mDragListView == child;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (top <= 0) {
                top = 0;
            }
            if (top >= mMenuHeight) {
                top = mMenuHeight;
            }
            return top;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (releasedChild == mDragListView) {
                if (releasedChild.getTop() > mMenuHeight / 2) {
                    mDragHelper.settleCapturedViewAt(0, mMenuHeight);
                    mMemuIsOpen = true;
                } else {
                    mDragHelper.settleCapturedViewAt(0, 0);
                    mMemuIsOpen = false;
                }
                postInvalidate();
            }
        }
    };

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private float mDownY;

    // because ACTION_DOWN was not received for this pointer before ACTION_MOVE //在move之前应该先执行down，因为down没执行
    // VDLV.onInterceptTouchEvent().DOWN -> LV.onTouchEvent ->
    // VDLV.onInterceptTouchEvent().MOVE -> onTouchEvent().MOVE
    //

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //菜单打开的话，全部拦截，交给自己处理
        if (mMemuIsOpen) {
            return true;
        }
        //主要考虑什么时候拦截，什么时候不拦截
        //向下滑动拦截,不要给ListView处理
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                //让 DragHelper拿一个完整的事件
                mDragHelper.processTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
                if (moveY - mDownY > 0 && !canChildScrollUp()) {
                    //向下滑动 && 滚动到了顶部，拦截不让ListView做处理
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    /**
     * 判断View是否滚动到了最顶部。SwipeRefreshLayout里的方法
     */
    public boolean canChildScrollUp() {
        if (mDragListView instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) mDragListView, -1);
        }
        return mDragListView.canScrollVertically(-1);
    }
}
