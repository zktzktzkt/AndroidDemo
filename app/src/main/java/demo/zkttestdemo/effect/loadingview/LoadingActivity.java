package demo.zkttestdemo.effect.loadingview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import demo.zkttestdemo.R;

/**
 * 仿雅虎splash页
 */
public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        final LoadingView view = findViewById(R.id.loadingView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.disappear();
            }
        }, 3000);
    }
}
