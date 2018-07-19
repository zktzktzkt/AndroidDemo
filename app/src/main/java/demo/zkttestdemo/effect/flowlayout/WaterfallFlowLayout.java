package demo.zkttestdemo.effect.flowlayout;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局
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

    //思路，通过前面两节课我们知道了其实，绘制流程最终会调用到我门的OnMesure  和   onLayout,
    //而不同的布局，他们自己的实现不一样，所以才有了我们使用的这些基本布局组件
    //那么我们现在自己来开发一个瀑布式的流式布局
    //不规则控件进行流式

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int left, top, right, bottom;


        int curLeft = 0;
        int curTop = 0;

        //开始迭代
        for (int i = 0; i < lstLineView.size(); i++) {
            List<View> lineviews = lstLineView.get(i);

            for (int j = 0; j < lineviews.size(); j++) {
                View view = lineviews.get(j);

                MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();

                left = curLeft + layoutParams.leftMargin;
                top = curTop + layoutParams.topMargin;
                right = left + view.getMeasuredWidth();
                bottom = top + view.getMeasuredHeight();

                view.layout(left, top, right, bottom);

                curLeft += view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;

            }

            curLeft = 0;
            curTop += lstLineHegiht.get(i);

        }

        lstLineView.clear();
        lstLineHegiht.clear();
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }


    List<Integer> lstLineHegiht = new ArrayList<>();
    List<List<View>> lstLineView = new ArrayList<>();


    /**
     * 根据子View的摆放规则来确定自己的宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 7.0之后会进行两次onMeasure一次onLayout
        lstLineView.clear();
        lstLineHegiht.clear();

        //1.先完成自己的宽高测量
        //需要得到mode进行判断我的显示模式是怎样的
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

            //当前行高宽
            int iChildHegiht = 0;
            int iChildWidth = 0;

            int iCurWidth = 0;
            int iCurHeight = 0;

            //数量
            int childCount = getChildCount();

            List<View> viewList = new ArrayList<>();
            for (int i = 0; i < childCount; i++) {
                //确定两个事情，当前行高，当前行宽
                View child = getChildAt(i);
                //先让子控件测量自己
                measureChild(child, widthMeasureSpec, heightMeasureSpec);

                //MARGIN 获取到当前LayoutParams
                MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();

                //获取实际宽高
                iChildWidth = child.getMeasuredWidth() + layoutParams.rightMargin + layoutParams.leftMargin;
                iChildHegiht = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

                //是否需要开始换行
                if (iChildWidth + iCurWidth > widthSize) {
                    //1.保存当前行信息
                    measureWidth = Math.max(measureWidth, iCurWidth);
                    measureHeight += iCurHeight;
                    lstLineHegiht.add(iCurHeight);
                    lstLineView.add(viewList);

                    //更新的行信息
                    iCurWidth = iChildWidth;
                    iCurHeight = iChildHegiht;

                    viewList = new ArrayList<>();
                    viewList.add(child);

                    //2.记录新行信息
                } else {
                    iCurWidth += iChildWidth;
                    iCurHeight = Math.max(iCurHeight, iChildHegiht);

                    viewList.add(child);

                }

                //6.如果正好是最后一行需要换行
                if (i == childCount - 1) {
                    //6.1.记录当前行的最大宽度，高度累加
                    measureWidth = Math.max(measureWidth, iCurWidth);
                    measureHeight += iCurHeight;

                    //6.2.将当前行的viewList添加至总的mViewsList，将行高添加至总的行高List
                    lstLineView.add(viewList);
                    lstLineHegiht.add(iCurHeight);

                }
            }
        }

        setMeasuredDimension(measureWidth, measureHeight);
    }
}
