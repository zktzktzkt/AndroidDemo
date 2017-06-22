package demo.zkttestdemo.effect.circleprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import demo.zkttestdemo.R;

public class CircleProgressActivity extends AppCompatActivity {
    int progress = 0;
    private CircleProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_progress);
        progressBar = (CircleProgressBar) findViewById(R.id.circleProgressBar);

        Timer timer = new Timer();
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
}
