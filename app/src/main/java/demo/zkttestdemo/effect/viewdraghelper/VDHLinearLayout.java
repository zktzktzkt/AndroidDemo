package demo.zkttestdemo.effect.viewdraghelper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zkt on 2017/6/20.
 */

public class VDHLinearLayout extends LinearLayout {
    private ViewDragHelper mViewDragHelper = null;
    private TextView mDragView;
    private TextView mAutobackView;
    private TextView mEdgeView;

    public VDHLinearLayout(Context context) {
        this(context, null);
    }

    public VDHLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VDHLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new VDHCallback());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = (TextView) getChildAt(0);
        mAutobackView = (TextView) getChildAt(1);
        mEdgeView = (TextView) getChildAt(2);
    }

    private class VDHCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if (child == mEdgeView) {
                return false;
            }
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int leftBound = getPaddingLeft();
            int rightBound = getWidth() - child.getWidth() - getPaddingRight();
            //                if(left<leftBound){
            //                    left = leftBound;
            //                }else if(left>rightBound){
            //                    left = rightBound;
            //                }
            //更加简洁的写法
            return Math.min(Math.max(left, leftBound), rightBound);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int topBound = getPaddingTop();
            int bottomBound = getHeight() - child.getHeight() - getPaddingBottom();
            return Math.min(Math.max(top, topBound), bottomBound);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (releasedChild == mAutobackView) {
                mViewDragHelper.settleCapturedViewAt(mAutobackView.getLeft(), mAutobackView.getTop());
                ViewCompat.postInvalidateOnAnimation(VDHLinearLayout.this);
            }
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mViewDragHelper == null) {
            return super.onTouchEvent(event);
        }
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mViewDragHelper == null) {
            return super.onInterceptHoverEvent(ev);
        }

        if (ev.getAction() == MotionEvent.ACTION_CANCEL) {
            mViewDragHelper.cancel();
            return false;
        }
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }


    public ViewDragHelper getViewDragHelper() {
        return mViewDragHelper;
    }
}
