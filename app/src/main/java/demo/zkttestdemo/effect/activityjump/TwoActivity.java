package demo.zkttestdemo.effect.activityjump;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import demo.zkttestdemo.R;

public class TwoActivity extends AppCompatActivity {
    public static final int TWO_REQUEST = 2;
    public static final int THREE_RESULT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
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
        if (requestCode == TWO_REQUEST && resultCode == THREE_RESULT) {
            setResult(OneActivity.THREE_RESULT, data);
            finish();
        }
    }
}
