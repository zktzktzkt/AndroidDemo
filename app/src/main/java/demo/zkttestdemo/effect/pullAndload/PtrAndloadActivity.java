package demo.zkttestdemo.effect.pullAndload;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import demo.zkttestdemo.R;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class PtrAndloadActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptr_load);
    }

    public void mt(View view) {
        Intent intent = new Intent(this, MTPtrActivity.class);
        startActivity(intent);
    }

    public void ali(View view) {
        Intent intent = new Intent(this, AliPtrActivity.class);
        startActivity(intent);
    }
}
