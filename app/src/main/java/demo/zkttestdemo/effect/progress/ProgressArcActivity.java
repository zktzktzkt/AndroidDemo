package demo.zkttestdemo.effect.progress;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import demo.zkttestdemo.R;


public class ProgressArcActivity extends AppCompatActivity {

    private ProgressArcView progress_arc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_arc);

        progress_arc = (ProgressArcView) findViewById(R.id.progress_arc);
        progress_arc.setCurrentCount(200, 200);
    }
}
