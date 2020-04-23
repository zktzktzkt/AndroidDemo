package demo.zkttestdemo.effect.activityjump;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import demo.zkttestdemo.R;

public class TwoActivity extends AppCompatActivity {
    public static final int TWO_REQUEST = 2;
    public static final int TWO_RESULT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("two_result", "我是从第二个Activity跳转过来的");
                setResult(TWO_RESULT, intent);
                finish();
            }
        });

        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TwoActivity.this, ThreeActivity.class);
                startActivityForResult(intent, TWO_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TWO_REQUEST && resultCode == ThreeActivity.THREE_RESULT) {
            setResult(ThreeActivity.THREE_RESULT, data);
            finish();
        }
    }
}
