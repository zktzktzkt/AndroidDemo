package demo.zkttestdemo.effect.nestedscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by zkt on 19/01/16.
 * Description:
 */
public class ChildScrollView extends ScrollView {

    private int mTouchSlop;
    private int mLastMotionY;

    public ChildScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //反射获取mLastMotionY和mTouchSlop
        /*try {
            Class<?> clazz = this.getClass();
            Field mTouchSlopField = clazz.getDeclaredField("mTouchSlop");
            mTouchSlopField.setAccessible(true);
            mTouchSlop = (int) mTouchSlopField.get(this);

            Field mLastMotionYField = clazz.getDeclaredField("mLastMotionY");
            mLastMotionYField.setAccessible(true);
            mLastMotionY = (int) mLastMotionYField.get(this);

            Log.e("ChildScrollView", "mTouchSlop -> " + mTouchSlop + "  mLastMotionY -> " + mLastMotionY);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetY = (int) (ev.getY() - mLastMotionY);
                Log.e("ChildScrollView", "mTouchSlop -> " + mTouchSlop + "  mLastMotionY -> " + mLastMotionY
                        + "  ev.getY() -> " + ev.getY() + "  Math.abs(offsetY) -> " + Math.abs(offsetY));
                if (Math.abs(offsetY) > mTouchSlop) {
                    if (offsetY > 0 && isScrollToTop() || offsetY < 0 && isScrollToBottom()) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private boolean isScrollToTop() {
        return getScrollY() <= 0;
    }

    private boolean isScrollToBottom() {
        return getScrollY() + getHeight() - getPaddingTop() - getPaddingBottom() == getChildAt(0).getMeasuredHeight();
    }
}
