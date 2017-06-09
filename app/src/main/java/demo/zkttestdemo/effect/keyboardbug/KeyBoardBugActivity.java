package demo.zkttestdemo.effect.keyboardbug;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import demo.zkttestdemo.R;


public class KeyBoardBugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_board_bug);
        //AndroidBug5497Workaround.assistActivity(this);

        EditText et_edit = (EditText) findViewById(R.id.et_edit);

    }

}
