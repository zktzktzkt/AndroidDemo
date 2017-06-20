package demo.zkttestdemo.effect.activityjump;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import demo.zkttestdemo.R;

public class OneActivity extends AppCompatActivity {
    public static final int ONE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OneActivity.this, TwoActivity.class);
                startActivityForResult(intent, ONE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ONE_REQUEST && resultCode == ThreeActivity.THREE_RESULT) {
            ((TextView) findViewById(R.id.textView)).setText(data.getStringExtra("three_result"));

        } else if (requestCode == ONE_REQUEST && resultCode == TwoActivity.TWO_RESULT) {
            ((TextView) findViewById(R.id.textView)).setText(data.getStringExtra("two_result"));
        }
    }
}
