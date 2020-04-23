package demo.zkttestdemo.effect.dashboard;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import demo.zkttestdemo.R;

/**
 * 仪表盘
 */
public class DashBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

//        ((DashBoardView) findViewById(R.id.dashBoard)).getAngleFromMark();
    }

}
