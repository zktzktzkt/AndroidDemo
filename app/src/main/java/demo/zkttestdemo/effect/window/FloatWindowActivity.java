package demo.zkttestdemo.effect.window;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import demo.zkttestdemo.R;

public class FloatWindowActivity extends AppCompatActivity {

    private WindowManager mWm;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_window);
        startFloatingService();
    }

    private void startFloatingService() {
        if (Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "请授权", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "请授权", Toast.LENGTH_SHORT).show();
                //startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
            }
        }
    }

    /**
     * 添加
     */
    public void addWindow(View view) {
        mWm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        btn = new Button(this);
        btn.setAllCaps(false);
        btn.setText("window manager test!");
        final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        mParams.width = 800;
        mParams.height = 800;
        mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        //                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        //                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        mParams.format = PixelFormat.RGBA_8888;
        mWm.addView(btn, mParams);
        btn.setOnTouchListener(new View.OnTouchListener() {
            private int x;
            private int y;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        int diffX = moveX - x;
                        int diffY = moveY - y;
                        mParams.x = mParams.x + diffX;
                        mParams.y = mParams.y + diffY;

                        x = moveX;
                        y = moveY;

                        mWm.updateViewLayout(btn, mParams);
                        break;
                }
                return false;
            }
        });
    }


    /**
     * 删除
     */
    public void delWindow(View view) {
        if (null == mWm || null == btn) {
            Toast.makeText(this, "请先添加Window", Toast.LENGTH_SHORT).show();
            return;
        }
        mWm.removeView(btn);
    }
}
