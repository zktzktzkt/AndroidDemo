package demo.zkttestdemo.effect.coordinator.jdBehavior;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

/**
 * this class code copy from android framework
 * @author android
 * @date 2018/6/5
 * The {@link CoordinatorLayout.Behavior} for a scrolling view that is positioned vertically below another view.
 */
public abstract class BaseHeaderScrollingViewBehavior<ChildView extends View> extends ViewOffsetBehavior<ChildView> {

    /** mTempRect1 **/
    final Rect mTempRect1 = new Rect();
    /** mTempRect2 **/
    final Rect mTempRect2 = new Rect();

    /** mVerticalLayoutGap **/
    private int mVerticalLayoutGap = 0;
    /** mOverlayTop **/
    private int mOverlayTop;


    /**
     * 构造方法
     * @param context
     * @param attrs
     */
    public BaseHeaderScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, ChildView child,
                                  int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec,
                                  int heightUsed) {
        final int childLpHeight = child.getLayoutParams().height;
        if (childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT
                || childLpHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            // If the menu's height is set to match_parent/wrap_content then measure it
            // with the maximum visible height

            final List<View> dependencies = parent.getDependencies(child);
            final View header = findFirstDependency(dependencies);
            if (header != null) {
                if (ViewCompat.getFitsSystemWindows(header)
                        && !ViewCompat.getFitsSystemWindows(child)) {
                    // If the header is fitting system windows then we need to also,
                    // otherwise we'll get CoL's compatible measuring
                    ViewCompat.setFitsSystemWindows(child, true);

                    if (ViewCompat.getFitsSystemWindows(child)) {
                        // If the set succeeded, trigger a new layout and return true
                        child.requestLayout();
                        return true;
                    }
                }

                int availableHeight = View.MeasureSpec.getSize(parentHeightMeasureSpec);
                if (availableHeight == 0) {
                    // If the measure spec doesn't specify a size, use the current height
                    availableHeight = parent.getHeight();
                }

                final int height = availableHeight - header.getMeasuredHeight()
                        + getScrollRange(header);
                final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height,
                        childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT
                                ? View.MeasureSpec.EXACTLY
                                : View.MeasureSpec.AT_MOST);

                // Now measure the scrolling view with the correct height
                parent.onMeasureChild(child, parentWidthMeasureSpec,
                        widthUsed, heightMeasureSpec, heightUsed);

                return true;
            }
        }
        return false;
    }

    @Override
    protected void layoutChild(final CoordinatorLayout parent, final ChildView child,
                               final int layoutDirection) {
        final List<View> dependencies = parent.getDependencies(child);
        final View header = findFirstDependency(dependencies);

        if (header != null) {
            CoordinatorLayout.LayoutParams lp = null;
            if (child.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
                lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            }
            if (lp == null) {
                return;
            }
            final Rect available = mTempRect1;
            available.set(parent.getPaddingLeft() + lp.leftMargin,
                    header.getBottom() + lp.topMargin,
                    parent.getWidth() - parent.getPaddingRight() - lp.rightMargin,
                    parent.getHeight() + header.getBottom()
                            - parent.getPaddingBottom() - lp.bottomMargin);

            final Rect out = mTempRect2;
            GravityCompat.apply(resolveGravity(lp.gravity), child.getMeasuredWidth(),
                    child.getMeasuredHeight(), available, out, layoutDirection);

            final int overlap = getOverlapPixelsForOffset(header);

            child.layout(out.left, out.top - overlap, out.right, out.bottom - overlap);
            mVerticalLayoutGap = out.top - header.getBottom();
        } else {
            // If we don't have a dependency, let super handle it
            super.layoutChild(parent, child, layoutDirection);
            mVerticalLayoutGap = 0;
        }
    }

    /**
     *
     * @param header
     * @return
     */
    protected float getOverlapRatioForOffset(final View header) {
        return 1f;
    }

    /**
     *
     * @param header
     * @return
     */
    protected final int getOverlapPixelsForOffset(final View header) {
        return mOverlayTop == 0 ? 0 : MathUtils.constrain(
                (int) (getOverlapRatioForOffset(header) * mOverlayTop), 0, mOverlayTop);
    }

    /**
     *
     * @param gravity
     * @return
     */
    private static int resolveGravity(int gravity) {
        return gravity == Gravity.NO_GRAVITY ? GravityCompat.START | Gravity.TOP : gravity;
    }

    /**
     * 获取第一个依赖的view
     * @param views
     * @return
     */
    public abstract View findFirstDependency(List<View> views);

    /**
     * 滚动范围
     * @param v
     * @return
     */
    protected int getScrollRange(View v) {
        return v.getMeasuredHeight();
    }

    /**
     * The gap between the top of the scrolling view and the bottom of the header layout in pixels.
     */
    protected final int getVerticalLayoutGap() {
        return mVerticalLayoutGap;
    }

    /**
     * Set the distance that this view should overlap any {@link AppBarLayout}.
     *
     * @param overlayTop the distance in px
     */
    public final void setOverlayTop(int overlayTop) {
        mOverlayTop = overlayTop;
    }

    /**
     * Returns the distance that this view should overlap any {@link AppBarLayout}.
     */
    public final int getOverlayTop() {
        return mOverlayTop;
    }
}
