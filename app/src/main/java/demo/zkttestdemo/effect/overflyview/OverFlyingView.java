package demo.zkttestdemo.effect.overflyview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/**
 * 仿夸克浏览器底部拖拽
 * Created by zkt on 2018-1-31.
 */

public class OverFlyingView extends LinearLayout {

    private ViewDragHelper viewDragHelper;
    private View bottomView;
    private View topView;
    private int elevationHeight = 25;//层叠高度
    private boolean isExpand = true;
    private int mScaledTouchSlop;
    private int mDistance;

    public OverFlyingView(Context context) {
        this(context, null);
    }

    public OverFlyingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverFlyingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        viewDragHelper = ViewDragHelper.create(this, ViewDragHelperCallBack);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new RuntimeException("必须有2个View！");
        }
        topView = getChildAt(0);
        bottomView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDistance = h - topView.getMeasuredHeight() - getPaddingBottom() - elevationHeight;
    }

    ViewDragHelper.Callback ViewDragHelperCallBack = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == topView;
        }

        /**
         * viewDragHelper的shouldInterceptTouchEvent中会判断这个方法，只需要返回值大于0就可以
         * @param child
         * @return 返回值大于0就可以
         */
        @Override
        public int getViewVerticalDragRange(View child) {
            return 1;
        }

        //不断返回top的值，主要用这个值来控制目标位置的坐标
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int topBound = getPaddingTop();
            int bottomBound = getHeight() - child.getHeight() - elevationHeight - getPaddingBottom();
            return Math.min(Math.max(top, topBound), bottomBound);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            float percent = (float) top / mDistance;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                changedView.setElevation(percent * 10);
            }
            bottomView.setScaleX(1 - percent * 0.03f);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (releasedChild == topView) {
                if (releasedChild.getTop() > mDistance / 2) {
                    viewDragHelper.settleCapturedViewAt(0, mDistance);
                    isExpand = false;
                } else {
                    viewDragHelper.settleCapturedViewAt(0, 0);
                    isExpand = true;
                }
                invalidate();
            }
        }
    };

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return viewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

}
