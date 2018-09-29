package com.zkt.lottery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ypzn.basemodule.ARouterManager;

@Route(path = ARouterManager.LotteryActivity)
public class LotteryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);

        final LotteryViewGroup lvg = findViewById(R.id.lvg);
        lvg.setOnSelectedListener(new LotteryViewGroup.OnSelectedListener() {
            @Override
            public void onSelect(int position) {
                Toast.makeText(LotteryActivity.this, position + "", Toast.LENGTH_SHORT).show();
            }
        });

        final EditText et = findViewById(R.id.et);

        findViewById(R.id.btn_where).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvg.setSelect(Integer.parseInt(et.getText().toString()));
            }
        });
        findViewById(R.id.btn_fix).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvg.setSelect(-1);
            }
        });

    }
}
