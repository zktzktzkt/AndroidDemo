package demo.zkttestdemo.effect.bottomsheet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import demo.zkttestdemo.R;

public class BottomSheetActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet2);

        findViewById(R.id.btn_zh).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_zh:
                startActivity(new Intent(this, ZHBottomSheetActivity.class));
                break;
        }
    }
}
