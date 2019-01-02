package demo.zkttestdemo.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zkt on 18/12/11.
 * Description: 流式布局
 */
public class TagLayout extends ViewGroup {
    List<Rect> childrenBounds = new ArrayList<>();

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("TagLayout", "onMeasure");
        int widthUsed = 0; //总宽度
        int heightUsed = 0; //总高度
        int lineHeight = 0; //单行最高的高度
        int lineWidth = 0; //单行最宽的宽度
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);
            //换行
            if (lineWidth + child.getMeasuredWidth() > specWidth &&
                    specMode != MeasureSpec.UNSPECIFIED) {
                lineWidth = 0;
                heightUsed += lineHeight;
                lineHeight = 0;
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);
            }
            // 因onMeasure会执行两次，作如下处理
            Rect childBound;
            if (i >= childrenBounds.size()) {
                childBound = new Rect();
                childrenBounds.add(childBound);
            } else {
                childBound = childrenBounds.get(i);
            }
            childBound.set(lineWidth, heightUsed, lineWidth + child.getMeasuredWidth(), heightUsed + child.getMeasuredHeight());
            lineWidth += child.getMeasuredWidth();
            widthUsed = Math.max(lineWidth, widthUsed);
            lineHeight = Math.max(lineHeight, child.getMeasuredHeight());
        }

        int width = widthUsed;
        int height = heightUsed + lineHeight;
        setMeasuredDimension(width, height);
    }

    /** onLayout会执行一次 */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e("TagLayout", "onLayout");
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Rect childBound = childrenBounds.get(i);
            child.layout(childBound.left, childBound.top, childBound.right, childBound.bottom);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
