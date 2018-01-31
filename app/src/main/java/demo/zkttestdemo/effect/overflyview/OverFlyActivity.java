package demo.zkttestdemo.effect.overflyview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import demo.zkttestdemo.R;

public class OverFlyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_fly);
    }

    public void fourClick(View view) {
        Toast.makeText(this, "4点击", Toast.LENGTH_SHORT).show();
    }

    public void oneClick(View view) {
        Toast.makeText(this, "1点击", Toast.LENGTH_SHORT).show();
    }
}
