package demo.zkttestdemo.effect.screenmatch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by zkt on 2018-9-17.
 * Description:
 */

public class ScreenMatchRelativeLayout extends RelativeLayout {

    static boolean isFlag = true;

    public ScreenMatchRelativeLayout(Context context) {
        this(context, null);
    }

    public ScreenMatchRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScreenMatchRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isFlag) {
            int childCount = getChildCount();
            float scaleX = UIUtil.getInstance(getContext()).getHorizontalScaleValue();
            float scaleY = UIUtil.getInstance(getContext()).getVerticalScaleValue();

            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);

                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                layoutParams.width = (int) (layoutParams.width * scaleX);
                layoutParams.height = (int) (layoutParams.height * scaleY);
                layoutParams.rightMargin = (int) (layoutParams.rightMargin * scaleX);
                layoutParams.leftMargin = (int) (layoutParams.leftMargin * scaleX);
                layoutParams.topMargin = (int) (layoutParams.topMargin * scaleY);
                layoutParams.bottomMargin = (int) (layoutParams.bottomMargin * scaleY);
            }

            isFlag = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }


}
