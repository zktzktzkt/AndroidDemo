package demo.zkttestdemo.effect.draglayout;

import android.os.Bundle;
import androidx.customview.widget.ViewDragHelper;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import demo.zkttestdemo.R;

public class DragActivity extends AppCompatActivity {

    LinearLayout ll_drag;
    ImageView ivDarkOverlay;
    Button btnShowLayout;

    int ll_drag_height;
    int startX, startY;
    int deltaX, deltaY;
    int animationDuration = 400;
    private ViewDragHelper mViewDragHelper;
    private FrameLayout fl_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
    }


    /*private void listenForDrag() {
        ll_drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        performCalculations(event);
                        break;

                    case MotionEvent.ACTION_UP:
                        checkWhatShouldHappen();
                        break;
                }
                return false;
            }
        });
    }

    private void performCalculations(MotionEvent event) {
        *//*偏移量，在ViewDragHelper中对应 onViewPositionChanged*//*
        deltaX = (int) (startX - event.getX());
        deltaY = (int) (startY - event.getY());

        float dragPercent = (float) (ll_drag_height - deltaY) / ll_drag_height;

        if (deltaY >= 0) {
            ll_drag.setTranslationY(-(int) (deltaY * ((float) 4 / 5))); //这里加了一个阻力的效果
            ivDarkOverlay.setAlpha(dragPercent);
        }
    }

    private void checkWhatShouldHappen() {
        if (deltaY > ll_drag_height / 3) {
            ll_drag.animate().translationY(-ll_drag_height).setDuration(animationDuration).start();
            ivDarkOverlay.animate().alpha(0).setDuration(animationDuration).withEndAction(new Runnable() {
                @Override
                public void run() {
                    runFinishingCode();
                }
            });
        } else {
            ll_drag.animate().translationY(0).setDuration(animationDuration).start();
            ivDarkOverlay.animate().alpha(1).setDuration(animationDuration).start();
        }
    }

    private void runFinishingCode() {
        ll_drag.setVisibility(View.GONE);
        ivDarkOverlay.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#000000"));
        }
    }

    private void onClicks() {
        btnShowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_drag.setVisibility(View.VISIBLE);
                ivDarkOverlay.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(Color.parseColor("#E91E63"));
                }

                ll_drag.animate().translationY(0).setDuration(animationDuration).withLayer();
                ivDarkOverlay.animate().alpha(1).setDuration(animationDuration).withLayer();
            }
        });
    }*/

}
