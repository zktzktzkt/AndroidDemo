package demo.zkttestdemo.recyclerview.nested;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by zkt on 2018-10-11.
 * Description:
 */

public class MyRecyclerView extends RecyclerView {
    private int mScrollPointerId;
    private int mInitialTouchX;
    private int mInitialTouchY;
    private int mTouchSlop;

    public MyRecyclerView(Context context) {
        super(context, null);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        final int action = e.getActionMasked();
        final int actionIndex = e.getActionIndex();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScrollPointerId = e.getPointerId(0);
                mInitialTouchX = (int) (e.getX() + 0.5f);
                mInitialTouchY = (int) (e.getY() + 0.5f);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mScrollPointerId = e.getPointerId(actionIndex);
                mInitialTouchX = (int) (e.getX(actionIndex) + 0.5f);
                mInitialTouchY = (int) (e.getY(actionIndex) + 0.5f);
                break;

            case MotionEvent.ACTION_MOVE:
                final int index = e.findPointerIndex(mScrollPointerId);

                final int x = (int) (e.getX(index) + 0.5f);
                final int y = (int) (e.getY(index) + 0.5f);
                final int dx = x - mInitialTouchX;
                final int dy = y - mInitialTouchY;
                boolean startScroll = false;
                if (getLayoutManager().canScrollHorizontally() && Math.abs(dx) > mTouchSlop
                        && Math.abs(dx) > Math.abs(dy)) {
                    startScroll = true;
                }
                if (getLayoutManager().canScrollVertically() && Math.abs(dy) > mTouchSlop
                        && Math.abs(dy) > Math.abs(dx)) {
                    startScroll = true;
                }
                return startScroll && super.onInterceptTouchEvent(e);
        }
        return super.onInterceptTouchEvent(e);
    }
}
