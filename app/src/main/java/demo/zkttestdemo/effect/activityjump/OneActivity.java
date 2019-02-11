package demo.zkttestdemo.effect.activityjump;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import demo.zkttestdemo.R;
import demo.zkttestdemo.utils.SelectableTextHelper;

public class OneActivity extends AppCompatActivity {
    public static final int ONE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);


        SelectableTextHelper mSelectableTextHelper = new SelectableTextHelper.Builder((TextView) findViewById(R.id.textView))
                .setSelectedColor(getResources().getColor(R.color.pink))
                .setCursorHandleSizeInDp(20)
                .setCursorHandleColor(getResources().getColor(R.color.pink))
                .build();

        mSelectableTextHelper.setOnNotesClickListener(new SelectableTextHelper.OnNoteBookClickListener() {
            @Override
            public void onTextSelect(CharSequence charSequence) {
                String content = charSequence.toString();
                Toast.makeText(OneActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });

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
