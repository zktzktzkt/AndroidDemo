package demo.zkttestdemo.effect.keyboardbug;

import android.graphics.Color;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import demo.zkttestdemo.R;
import demo.zkttestdemo.kpswitch.util.KeyboardUtil;
import demo.zkttestdemo.utils.StatusBarUtil;


public class KeyBoardBugActivity extends AppCompatActivity {

    private static final String TAG = "KeyBoardBugActivity";
    private MyRelativeLayout mPanelRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_board_bug);
        StatusBarUtil.setColorNoTranslucent(this, Color.BLACK);
        //        AndroidBug5497Workaround.assistActivity(this);

        final EditText et_edit = (EditText) findViewById(R.id.et_edit);
        final NestedScrollView scrollview = (NestedScrollView) findViewById(R.id.scrollview);
        mPanelRoot = (MyRelativeLayout) findViewById(R.id.panel_root);

        // 实现监听的view必须实现 IPanelHeightTarget 接口
        KeyboardUtil.attach(this, mPanelRoot,
                new KeyboardUtil.OnKeyboardShowingListener() {
                    @Override
                    public void onKeyboardShowing(boolean isShowing) {
                        Log.d(TAG, String.format("Keyboard is %s", isShowing ? "showing" : "hiding"));
                    }
                });

        scrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    KeyboardUtil.hideKeyboard(et_edit);
                }

                return false;
            }
        });

            /*findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.baidu.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });*/
    }
}
