package demo.zkttestdemo.effect.bottomsheet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import demo.zkttestdemo.R;

public class DialogOnTopActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_on_top);

        findViewById(R.id.promptly_buy).setOnClickListener(this);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.promptly_buy:
                ShopCartDialog dialog = new ShopCartDialog(this, R.style.cartdialog);
                Window window = dialog.getWindow();
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.show();
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params.gravity = Gravity.BOTTOM;
                params.y = ll_bottom.getMeasuredHeight();
                params.dimAmount = 0.5f; //0.0f完全不暗，即背景是可见的 ，1.0f时候，背景全部变黑暗
                window.setAttributes(params);
                break;
        }
    }
}
