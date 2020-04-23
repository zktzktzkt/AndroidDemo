package demo.zkttestdemo.effect.activityjump;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import demo.zkttestdemo.R;

public class ThreeActivity extends AppCompatActivity {
    public static final int THREE_RESULT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);

        findViewById(R.id.btn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("three_result", "我是从第三个Activity传过来的数据");
                setResult(THREE_RESULT, intent);
                finish();
            }
        });
    }
}
