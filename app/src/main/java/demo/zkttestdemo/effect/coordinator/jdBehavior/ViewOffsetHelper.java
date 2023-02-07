package demo.zkttestdemo.effect.coordinator.jdBehavior;

import android.view.View;

import androidx.core.view.ViewCompat;

/**
 * this class code copy from android framework
 *
 * @author android
 * @date 2018/6/5
 * Utility helper for moving a {@link View} around using
 * {@link View#offsetLeftAndRight(int)} and
 * {@link View#offsetTopAndBottom(int)}.
 * <p>
 * Also the setting of absolute offsets (similar to translationX/Y), rather than additive
 * offsets.
 */
public class ViewOffsetHelper {

    /** 持有view **/
    private final View mView;

    /** 上 **/
    private int mLayoutTop;
    /** 左 **/
    private int mLayoutLeft;
    /** 上 **/
    private int mOffsetTop;
    /** 左 **/
    private int mOffsetLeft;

    /**
     * 构造方法
     * @param view
     */
    public ViewOffsetHelper(View view) {
        mView = view;
    }

    /**
     * onViewLayout
     */
    public void onViewLayout() {
        // Now grab the intended top
        mLayoutTop = mView.getTop();
        mLayoutLeft = mView.getLeft();

        // And offset it as needed
        updateOffsets();
    }

    /**
     * 更新位移
     */
    private void updateOffsets() {
        ViewCompat.offsetTopAndBottom(mView, mOffsetTop - (mView.getTop() - mLayoutTop));
        ViewCompat.offsetLeftAndRight(mView, mOffsetLeft - (mView.getLeft() - mLayoutLeft));
    }

    /**
     * Set the top and bottom offset for this {@link com.google.android.material.appbar.ViewOffsetHelper}'s view.
     *
     * @param offset the offset in px.
     * @return true if the offset has changed
     */
    public boolean setTopAndBottomOffset(int offset) {
        if (mOffsetTop != offset) {
            mOffsetTop = offset;
            updateOffsets();
            return true;
        }
        return false;
    }

    /**
     * Set the left and right offset for this {@link com.google.android.material.appbar.ViewOffsetHelper}'s view.
     *
     * @param offset the offset in px.
     * @return true if the offset has changed
     */
    public boolean setLeftAndRightOffset(int offset) {
        if (mOffsetLeft != offset) {
            mOffsetLeft = offset;
            updateOffsets();
            return true;
        }
        return false;
    }

    /**
     * 上下位移
     * @return
     */
    public int getTopAndBottomOffset() {
        return mOffsetTop;
    }

    /**
     * 左右位移
     * @return
     */
    public int getLeftAndRightOffset() {
        return mOffsetLeft;
    }

    /**
     * 上
     * @return
     */
    public int getLayoutTop() {
        return mLayoutTop;
    }

    /**
     * 下
     * @return
     */
    public int getLayoutLeft() {
        return mLayoutLeft;
    }
}
