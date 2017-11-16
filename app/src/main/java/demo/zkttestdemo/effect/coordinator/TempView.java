package demo.zkttestdemo.effect.coordinator;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zkt on 2017-11-10.
 */

public class TempView extends View {

    private int lastX;
    private int lastY;

    public TempView(Context context) {
        this(context, null);
    }

    public TempView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TempView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取到手指处的横坐标和纵坐标
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                int offX = x - lastX;
                int offY = y - lastY;
                //方式一 改变了left
               // layout(getLeft() + offX, getTop() + offY, getRight() + offX, getBottom() + offY);

                //方式二  改变了left
                 /*offsetLeftAndRight(offX);
                 offsetTopAndBottom(offY);*/

                //方式三（推荐） 改变了left
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) getLayoutParams();
                mlp.leftMargin = getLeft() + offX;
                mlp.topMargin = getTop() + offY;
                setLayoutParams(mlp);

                //方式四  移动的是整个所在的ViewGroup，View本身的属性并不会改变 比如left、transitionX
                // ((View) getParent()).scrollBy(-offX,- offY);

                Log.e("getLeft()", getLeft() + "");
                Log.e("getTranslationX()", getTranslationX() + "");
                break;
        }
        return true;
    }

}
