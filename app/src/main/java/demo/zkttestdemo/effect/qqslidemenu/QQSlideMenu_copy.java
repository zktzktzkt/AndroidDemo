package demo.zkttestdemo.effect.qqslidemenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2018-1-28.
 */

public class QQSlideMenu_copy extends HorizontalScrollView{

    private float mMenuWidth;

    public QQSlideMenu_copy(Context context) {
        this(context, null);
    }

    public QQSlideMenu_copy(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQSlideMenu_copy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QQSlideMenu);
        float rightPadding = array.getDimension(R.styleable.QQSlideMenu_right_padding, ConvertUtils.dp2px(50f));
        array.recycle();

        mMenuWidth = ScreenUtils.getScreenWidth() - rightPadding;

        new GestureDetector(context, new Ges());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    private class Ges extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
