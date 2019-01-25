package demo.zkttestdemo.effect.draggridview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zkt on 19/01/21.
 * Description:
 */
public class DragHelperGridView extends ViewGroup {
    private static final int COLUMNS = 2;
    private static final int ROWS = 3;
    ViewDragHelper dragHelper;

    public DragHelperGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        dragHelper = ViewDragHelper.create(this, new DragCallback());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specHeight = MeasureSpec.getSize(heightMeasureSpec);

        int childWidth = specWidth / COLUMNS;
        int childHeight = specHeight / ROWS;
        measureChildren(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));

        setMeasuredDimension(specWidth, specHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int childLeft;
        int childTop;
        int childWidth = getWidth() / COLUMNS;
        int childHeight = getHeight() / ROWS;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            childLeft = i % 2 * childWidth; // 0 1 0 1 0 1
            childTop = i / 2 * childHeight; // 0(0) 1(0) 2(1) 3(1) 4(2) 5(2)
            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class DragCallback extends ViewDragHelper.Callback {
        float capturedLeft;
        float capturedTop;

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            return left;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return top;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            if (state == ViewDragHelper.STATE_IDLE) {
                View capturedView = dragHelper.getCapturedView();
                if (capturedView != null) {
                    capturedView.setElevation(capturedView.getElevation() - 1);
                }
            }
        }

        //做初始工作
        @Override
        public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
            capturedChild.setElevation(getElevation() + 1);//把view抬高，能盖住别的view
            capturedLeft = capturedChild.getLeft();
            capturedTop = capturedChild.getTop();
        }

        //移动过程中
        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
        }

        //松手
        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            //以松手前的滑动速度为初速动，让捕获到的View自动滚动到指定位置。只能在Callback的onViewReleased()中调用。
            dragHelper.settleCapturedViewAt((int) capturedLeft, (int) capturedTop);
            postInvalidateOnAnimation();
        }
    }
}
