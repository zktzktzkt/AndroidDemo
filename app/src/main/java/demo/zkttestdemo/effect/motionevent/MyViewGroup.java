package demo.zkttestdemo.effect.motionevent;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by zkt on 2017/7/24.
 */

public class MyViewGroup extends LinearLayout {
    public MyViewGroup(@NonNull Context context) {
        super(context);
    }

    public MyViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("ViewGroup", "dispatchTouchEvent ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.e("ViewGroup", "dispatchTouchEvent ACTION_MOVE");
                break;

            case MotionEvent.ACTION_UP:
                Log.e("ViewGroup", "dispatchTouchEvent ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("ViewGroup", "onInterceptTouchEvent ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.e("ViewGroup", "onInterceptTouchEvent ACTION_MOVE");
                break;

            case MotionEvent.ACTION_UP:
                Log.e("ViewGroup", "onInterceptTouchEvent ACTION_UP");
                break;
        }
        Log.e("ViewGroup的onInterceptTouchEvent返回值", super.onInterceptTouchEvent(ev) + "");

        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("ViewGroup", "onTouchEvent ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.e("ViewGroup", "onTouchEvent ACTION_MOVE");
                break;

            case MotionEvent.ACTION_UP:
                Log.e("ViewGroup", "onTouchEvent ACTION_UP");
                break;
        }
        Log.e("ViewGroup的onTouchEvent返回值", super.onTouchEvent(ev) + "");
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Log.e("ViewGroup生命周期测试", "onFinishInflate");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.e("ViewGroup生命周期测试", "onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Log.e("ViewGroup生命周期测试", "onLayout");
    }
}
