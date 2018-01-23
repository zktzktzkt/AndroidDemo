package demo.zkttestdemo.effect.draglayout;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import demo.zkttestdemo.R;

public class DragActivity1 extends AppCompatActivity {

    LinearLayout ll_drag;
    ImageView ivDarkOverlay;
    Button btnShowLayout;

    int ll_drag_height;
    int startX, startY;
    int deltaX, deltaY;
    int animationDuration = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);

        initializer();
        getHeight();
        onClicks();
    }

    private void initializer() {
//        ll_drag = findViewById(R.id.ll_drag);
//        ivDarkOverlay = findViewById(R.id.iv_darkOverlay);
//        btnShowLayout = findViewById(R.id.btnShowLayout);
    }

    private void getHeight() {
        ll_drag.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_drag.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                ll_drag_height = ll_drag.getHeight();
                listenForDrag();
            }
        });
    }

    private void listenForDrag() {
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
        /*偏移量，在ViewDragHelper中对应 onViewPositionChanged*/
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
    }
}
