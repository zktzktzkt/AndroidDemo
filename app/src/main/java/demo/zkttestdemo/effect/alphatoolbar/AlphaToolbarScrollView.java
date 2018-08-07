package demo.zkttestdemo.effect.alphatoolbar;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/12/31 0031.
 */

public class AlphaToolbarScrollView extends NestedScrollView {
    private final Context mContext;
    private int mSlop;
    private ImageView headView;
    private Toolbar toolbar;
    public static final String TAG = "AlphaToolbarScrollView";
    private boolean mFitsSystemWindows;
    private int headHeight;

    public AlphaToolbarScrollView(Context context) {
        this(context, null);
    }

    public AlphaToolbarScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphaToolbarScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    /**
     * @param toolbar
     * @param headView
     */
    public void setTitleAndHead(final Toolbar toolbar, final ImageView headView, final boolean fitsSystemWindows) {
        this.toolbar = toolbar;
        this.headView = headView;

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                if (fitsSystemWindows) {
                    headHeight = headView.getMeasuredHeight() - toolbar.getMeasuredHeight() - getStatusBarHeight();
                } else {
                    headHeight = headView.getMeasuredHeight() - toolbar.getMeasuredHeight();
                }
            }
        });
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        int alpha = (int) (((float) t / headHeight) * 255);
        if (alpha >= 255)
            alpha = 255;
        if (alpha <= mSlop)
            alpha = 0;
        toolbar.getBackground().setAlpha(alpha);

        super.onScrollChanged(l, t, oldl, oldt);
    }


    private int getStatusBarHeight() {
        //插件式换肤，怎么获取资源的，先获取资源id，根据id获取资源
        Resources resources = mContext.getResources();
        int statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelOffset(statusBarHeightId);
    }

}
