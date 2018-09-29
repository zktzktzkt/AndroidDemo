package com.zkt.circlelayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ypzn.basemodule.ARouterManager;

@Route(path = ARouterManager.CircleLayoutActivity)
public class CircleLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_layout);
    }
}
