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

public class DragActivity extends AppCompatActivity {

    LinearLayout linDraggable;
    View viewTouchListener;
    ImageView imgDarkOverlay;
    Button btnShowLayout;

    int linHeight;
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
        linDraggable = findViewById(R.id.linDraggable);
        viewTouchListener = findViewById(R.id.viewTouchListener);
        imgDarkOverlay = findViewById(R.id.imgDarkOverlay);
        btnShowLayout = findViewById(R.id.btnShowLayout);
    }

    private void getHeight() {
        linDraggable.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                linDraggable.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                linHeight = linDraggable.getHeight();
                listenForDrag();
            }
        });
    }

    private void listenForDrag() {
        viewTouchListener.setOnTouchListener(new View.OnTouchListener() {
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
                return true;
            }
        });
    }

    private void performCalculations(MotionEvent event) {
        deltaX = (int) (startX - event.getX());
        deltaY = (int) (startY - event.getY());

        float factorFromOne = (float) (linHeight - deltaY) / linHeight;

        if (deltaY >= 0) {
            linDraggable.setTranslationY(-(int) (deltaY * ((float) 4 / 5)));
            imgDarkOverlay.setAlpha(factorFromOne);
        }
    }

    private void checkWhatShouldHappen() {
        if (deltaY > linHeight / 3) {
            linDraggable.animate().translationY(-linHeight).setDuration(animationDuration).start();

            imgDarkOverlay.animate().alpha(0).setDuration(animationDuration).withEndAction(new Runnable() {
                @Override
                public void run() {
                    runFinishingCode();
                }
            });
        } else {
            linDraggable.animate().translationY(0).setDuration(animationDuration).start();
            imgDarkOverlay.animate().alpha(1).setDuration(animationDuration).start();
        }
    }

    private void runFinishingCode() {
        viewTouchListener.setVisibility(View.GONE);
        linDraggable.setVisibility(View.GONE);
        imgDarkOverlay.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#000000"));
        }
    }

    private void onClicks() {
        btnShowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewTouchListener.setVisibility(View.VISIBLE);
                linDraggable.setVisibility(View.VISIBLE);
                imgDarkOverlay.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(Color.parseColor("#E91E63"));
                }

                linDraggable.animate().translationY(0).setDuration(animationDuration).withLayer();
                imgDarkOverlay.animate().alpha(1).setDuration(animationDuration).withLayer();
            }
        });
    }
}
