package demo.zkttestdemo.effect.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description: 瀑布流式布局
 * Created by zkt on 2018-7-19.
 */

public class WaterfallFlowLayout extends ViewGroup {
    public WaterfallFlowLayout(Context context) {
        super(context);
    }

    public WaterfallFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WaterfallFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //不规则控件进行流式布局

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 1.先完成自己的宽高测量
        //得到宽高模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //父容器宽高
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //当前控件宽高(自己)
        int measureWidth = 0;
        int measureHeight = 0;

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            measureWidth = widthSize;
            measureHeight = heightSize;
        } else {
            //确定两个事情，当前行高，当前行宽
            int iRowWidth = 0;
            int iRowHeight = 0;

            int iChildHeight = 0;
            int iChildWidth = 0;

            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                //先让子控件测量自己
                measureChild(child, widthMeasureSpec, heightMeasureSpec);

                // Margin 获取到当前子控件的LayoutParams
                MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
                //获取child实际宽高
                iChildWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                iChildHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

                //是否需要换行
                if (iChildWidth + iRowWidth > widthSize) {
                    //1. 保存当前行信息
                    measureWidth = Math.max(iChildWidth, measureWidth);
                    measureHeight += iRowHeight;
                    //更新行宽高 
                    iRowWidth = iChildWidth;
                    iRowHeight = iChildHeight;



                    //2. 记录新行信息

                } else {
                    iRowWidth += iChildWidth;
                    iRowHeight = Math.max(iChildHeight, iRowHeight);
                }
            }

        }

    }

    /**
     * 为了确保强转成功 (MarginLayoutParams) child.getLayoutParams();
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
