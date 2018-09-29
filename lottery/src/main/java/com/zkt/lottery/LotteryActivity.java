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

    /**
     * 处理思路：
     * ->将森林水滴作为一个总体而不是单个的view，自定义一个ViewGroup容器
     * ->循环创建view
     * ->为view随机设置位置(在一些固定的集合中随机选取，尽量保证水滴不重合)
     * ->为view设置一个初始的运动方向（注：由于每个view的运动方向不同，所以我选择将方向绑定到view的tag中）
     * ->为view设置一个初始的速度（同理：将初始速度绑定到view的tag中）
     * ->添加view到容器中，并缩放伴随透明度显示
     * ->开启handler达到view上下位移动画（注意点：这里我们需要定一个临界值来改变view的速度，到达view时而快时而慢的目的）
     * ->点击view后，缩放、透明度伴随位移移除水滴
     * ->界面销毁时停止调用handler避免内存泄漏，空指针等异常
     **/
}
