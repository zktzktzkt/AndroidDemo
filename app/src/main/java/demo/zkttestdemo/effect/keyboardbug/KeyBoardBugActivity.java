package demo.zkttestdemo.effect.keyboardbug;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import demo.zkttestdemo.R;
import demo.zkttestdemo.kpswitch.util.KeyboardUtil;


public class KeyBoardBugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_board_bug);
//        AndroidBug5497Workaround.assistActivity(this);

        final EditText et_edit = (EditText) findViewById(R.id.et_edit);
        final NestedScrollView scrollview = (NestedScrollView) findViewById(R.id.scrollview);
        final LinearLayout mPanelRoot = (LinearLayout) findViewById(R.id.panel_root);
        /*findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.baidu.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });*/

        scrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    KeyboardUtil.hideKeyboard(et_edit);
                }

                return false;
            }
        });
    }
}
