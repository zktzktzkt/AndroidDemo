package demo.zkttestdemo.effect.loadingdialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import demo.zkttestdemo.R;

/**
 * Created by Administrator on 2016/12/30 0030.
 */
public class LoadingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_baozi);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                CustomProgressDialog customProgressDialog = new CustomProgressDialog(this, "啊哈哈哈我最帅", R.drawable.frame);
                customProgressDialog.show();
                break;
            case R.id.btn_hs:

                break;
        }

    }
}
