package demo.zkttestdemo.effect.coordinator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import demo.zkttestdemo.R;

public class CoordinatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator2);
    }

    public void behaviorClick(View view) {
        startActivity(new Intent(this, CoordinatorBehaviorActivity.class));
    }

    public void stickyClick(View view) {
        startActivity(new Intent(this, StikyTabActivity.class));
    }
}
