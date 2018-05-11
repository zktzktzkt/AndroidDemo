package demo.zkttestdemo.effect.draglayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2018-1-23.
 */

public class DragLayout extends FrameLayout {

    private ViewDragHelper mViewDragHelper;
    private Button btnShowLayout;
    private View mask;
    private LinearLayout ll_drag;
    private int ll_drag_height;
    private int dragHeight;


    public DragLayout(@NonNull Context context) {
        this(context, null);
    }

    public DragLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initLayout(context);
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    }

    private void initLayout(Context context) {
        inflate(context, R.layout.layout_drag, this);
        ll_drag = findViewById(R.id.ll_drag);
        mask = findViewById(R.id.mask);
        btnShowLayout = findViewById(R.id.btnShowLayout);

        btnShowLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ll_drag_height = ll_drag.getMeasuredHeight();
    }


    class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == ll_drag;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            Log.e("DragLayout", "top:" + top);
            int newHeight = Math.min(Math.max(-ll_drag_height, top), 0);
            return newHeight;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            Log.e("DragLayout", "dy:" + dy + "  top:" + top);
            dragHeight = Math.abs(top);
            float dragPercent = (float) (ll_drag_height - Math.abs(top)) / ll_drag_height;
            mask.setAlpha(dragPercent);
        }

        /**
         * 拖动结束后调用
         *
         * @param releasedChild
         * @param xvel          水平方向的速度 向右为正
         * @param yvel          竖直方向的速度 向下为正
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            Log.e("DragLayout", "dragHeight: " + dragHeight + "  ll_drag_height/2：" + ll_drag_height / 2);
            if (dragHeight >= ll_drag_height / 2) {
                open();
            } else {
                close();
            }
        }
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 打开
     */
    public void open() {
        if (mViewDragHelper.smoothSlideViewTo(ll_drag, 0, -ll_drag_height)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 关闭
     */
    public void close() {
        if (mViewDragHelper.smoothSlideViewTo(ll_drag, 0, 0)) {
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
}
