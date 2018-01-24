package demo.zkttestdemo.effect.verticaldrag;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by zkt on 2018-1-24.
 */

public class VerticalDragListView extends FrameLayout {
    private ViewDragHelper mDragHelper;
    private View mDragListView;
    //后面菜单的高度
    private int mMenuHeight;

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
                    open();
                } else {
                    close();
                }
            }
        }
    };

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 打开
     */
    public void open() {
        // mDragHelper.smoothSlideViewTo() //也可以用这个，但settleCapturedViewAt有个特点：你松手时的速度是多少，它平移的速度就是多少
        if (mDragHelper.settleCapturedViewAt(0, mMenuHeight)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 关闭
     */
    public void close() {
        if (mDragHelper.settleCapturedViewAt(0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }
}
