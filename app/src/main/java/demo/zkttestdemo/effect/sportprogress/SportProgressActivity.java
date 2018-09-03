package demo.zkttestdemo.effect.sportprogress;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import demo.zkttestdemo.R;

/**
 * 运动步数progress
 */
public class SportProgressActivity extends AppCompatActivity {

    private SportProgressView sportView;
    private EditText etStep;
    private EditText et_currentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_progress);

        sportView = findViewById(R.id.sport_view);
        etStep = findViewById(R.id.et_step);
        et_currentStep = findViewById(R.id.et_currentStep);
    }

    public void setData(View view) {
        String targetStep = etStep.getText().toString();
        String currentStep = et_currentStep.getText().toString();
        sportView.setCurrentAndTarget(Integer.parseInt(currentStep), Integer.parseInt(targetStep), true);

    }
}
