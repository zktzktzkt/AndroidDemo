package demo.zkttestdemo.effect.motionevent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by zkt on 2017/7/24.
 */

public class MyView extends View {
    public MyView(@NonNull Context context) {
        this(context, null);
    }

    public MyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // setClickable(true);

        setOnTouchListener(new OnTouchListener() {
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
                return false;
            }
        });
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
                Log.e("MyView", "onTouchEvent 按下 y->" + ev.getRawY());
                break;

            case MotionEvent.ACTION_MOVE:
                Log.e("MyView", "onTouchEvent 移动 距离屏幕 y->" + ev.getRawY() + "   距离View y->" + ev.getY());
                break;

            case MotionEvent.ACTION_UP:
                Log.e("MyView", "onTouchEvent 抬起 y->" + ev.getRawY());
                break;
        }
        //        return super.onTouchEvent(ev);
        return false;
    }

}
