package demo.zkttestdemo.effect.tablayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import demo.zkttestdemo.R;

public class MyTablayoutActivity extends AppCompatActivity {
    MyIndicator myIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tablayout);

        myIndicator = (MyIndicator) findViewById(R.id.my);
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIndicator.add("tab5", 10);
                myIndicator.setChanger(4);
                Toast.makeText(MyTablayoutActivity.this, "添加tab5", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_speed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIndicator.setAnimationTime(100);
                Toast.makeText(MyTablayoutActivity.this, "修改移动速度改为100", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_jinzhi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIndicator.setProhibitPositio(2);
                Toast.makeText(MyTablayoutActivity.this, "禁止tab3修改状态", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_length).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIndicator.setFull();
                Toast.makeText(MyTablayoutActivity.this, "修改下标线的长度铺满", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
