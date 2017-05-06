package demo.zkttestdemo.effect.alphatoolbar;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/12/31 0031.
 */

public class AlphaToolbarScrollView extends NestedScrollView {
    private int mSlop;
    private ImageView headView;
    private Toolbar toolbar;
    public static final String TAG = "AlphaToolbarScrollView";

    public AlphaToolbarScrollView(Context context) {
        super(context);
    }

    public AlphaToolbarScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlphaToolbarScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     *
     * @param headLayout
     *            头部布局
     * @param imageview
     *            标题
     */
    public void setTitleAndHead(Toolbar toolbar, ImageView headView) {
        this.toolbar = toolbar;
        this.headView = headView;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        float headHeight = headView.getMeasuredHeight()
                - toolbar.getMeasuredHeight();
        int alpha = (int) (((float) t / headHeight) * 255);
        if (alpha >= 255)
            alpha = 255;
        if (alpha <= mSlop)
            alpha = 0;
        toolbar.getBackground().setAlpha(alpha);

        super.onScrollChanged(l, t, oldl, oldt);
    }
}
