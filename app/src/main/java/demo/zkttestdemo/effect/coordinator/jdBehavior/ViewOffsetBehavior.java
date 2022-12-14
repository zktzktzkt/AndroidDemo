package demo.zkttestdemo.effect.coordinator.jdBehavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * this class code copy from android framework
 * @author android
 * @date 2018/6/5
 * Behavior will automatically sets up a {@link ViewOffsetHelper} on a {@link View}.
 */
public class ViewOffsetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    /** Helper **/
    private ViewOffsetHelper mViewOffsetHelper;

    /** TopBottomOffset **/
    private int mTempTopBottomOffset = 0;
    /** LeftRightOffset **/
    private int mTempLeftRightOffset = 0;

    /**
     * Constructor
     * @param context context
     * @param attrs attrs
     */
    public ViewOffsetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        // First let lay the child out
        layoutChild(parent, child, layoutDirection);

        if (mViewOffsetHelper == null) {
            mViewOffsetHelper = new ViewOffsetHelper(child);
        }
        mViewOffsetHelper.onViewLayout();

        if (mTempTopBottomOffset != 0) {
            mViewOffsetHelper.setTopAndBottomOffset(mTempTopBottomOffset);
            mTempTopBottomOffset = 0;
        }
        if (mTempLeftRightOffset != 0) {
            mViewOffsetHelper.setLeftAndRightOffset(mTempLeftRightOffset);
            mTempLeftRightOffset = 0;
        }

        return true;
    }

    /**
     * layoutChild
     * @param parent CoordinatorLayout
     * @param child View
     * @param layoutDirection layoutDirection
     */
    protected void layoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        // Let the parent lay it out by default
        parent.onLayoutChild(child, layoutDirection);
    }

    /**
     * set top and bottom offset
     * @param offset offset
     * @return success or fail
     */
    public boolean setTopAndBottomOffset(int offset) {
        if (mViewOffsetHelper != null) {
            return mViewOffsetHelper.setTopAndBottomOffset(offset);
        } else {
            mTempTopBottomOffset = offset;
        }
        return false;
    }

    /**
     * set left and right offset
     * @param offset offset
     * @return success or fail
     */
    public boolean setLeftAndRightOffset(int offset) {
        if (mViewOffsetHelper != null) {
            return mViewOffsetHelper.setLeftAndRightOffset(offset);
        } else {
            mTempLeftRightOffset = offset;
        }
        return false;
    }

    /**
     * get top and bottom offset
     * @return top and bottom offset
     */
    public int getTopAndBottomOffset() {
        return mViewOffsetHelper != null ? mViewOffsetHelper.getTopAndBottomOffset() : 0;
    }

    /**
     * get left and right offset
     * @return left and right offset
     */
    public int getLeftAndRightOffset() {
        return mViewOffsetHelper != null ? mViewOffsetHelper.getLeftAndRightOffset() : 0;
    }
}
