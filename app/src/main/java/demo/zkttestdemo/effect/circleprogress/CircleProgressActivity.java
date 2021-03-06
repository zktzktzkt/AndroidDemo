package demo.zkttestdemo.effect.circleprogress;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import demo.zkttestdemo.R;

/**
 * 圆形进度progress按钮
 */
public class CircleProgressActivity extends AppCompatActivity {
    int progress = 0;
    private CircleProgressBar progressBar;
    private CountdownProgress countdownProgress;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_progress);
        progressBar = findViewById(R.id.circleProgressBar);
        countdownProgress = findViewById(R.id.countdownProgress);

        countdownProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownProgress.startCountdown(1000);
            }
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                progress++;
                progressBar.update(progress, progress * 100 / 360 + "%");
                if (progress == 360) {
                    progress = 0;
                }
            }
        }, 500, 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != timer) {
            timer.cancel();
        }
    }
}
