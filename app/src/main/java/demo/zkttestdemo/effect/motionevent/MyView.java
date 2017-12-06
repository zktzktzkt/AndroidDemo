package demo.zkttestdemo.effect.motionevent;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zkt on 2017/7/24.
 */

public class MyView extends View implements View.OnTouchListener {
    public MyView(@NonNull Context context) {
        this(context, null);
    }

    public MyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       // setClickable(true);

        setOnTouchListener(this);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("MyView", "dispatchTouchEvent ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.e("MyView", "dispatchTouchEvent ACTION_MOVE");

                break;

            case MotionEvent.ACTION_UP:
                Log.e("MyView", "dispatchTouchEvent ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                getParent().requestDisallowInterceptTouchEvent(true);
                Log.e("MyView", "onTouchEvent ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.e("MyView", "onTouchEvent ACTION_MOVE");
                break;

            case MotionEvent.ACTION_UP:
                Log.e("MyView", "onTouchEvent ACTION_UP");
                break;
        }
        return super.onTouchEvent(ev);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //                getParent().requestDisallowInterceptTouchEvent(true);
                Log.e("MyView", "onTouch ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.e("MyView", "onTouch ACTION_MOVE");
                break;

            case MotionEvent.ACTION_UP:
                Log.e("MyView", "onTouch ACTION_UP");
                break;
        }
        return true;
    }
}
