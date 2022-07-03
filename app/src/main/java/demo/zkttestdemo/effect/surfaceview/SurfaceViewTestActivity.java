package demo.zkttestdemo.effect.surfaceview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ToastUtils;

import demo.zkttestdemo.R;

public class SurfaceViewTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view_test);

        DownTimeView downTimeView = findViewById(R.id.downTimeView);
        downTimeView.startTime(5000, new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort("倒计时结束");
            }
        });
    }
}
