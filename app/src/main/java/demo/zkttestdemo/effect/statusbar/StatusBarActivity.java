package demo.zkttestdemo.effect.statusbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import demo.zkttestdemo.R;

public class StatusBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_bar);

        StatusBarUtil.setActivityTranslucent(this);

        // QQ效果 1. 监听scrollview 判断滚动的位置跟头部的Imageview比较计算背景透明度
        // 2. 自定义Behavior
        
    }
}
