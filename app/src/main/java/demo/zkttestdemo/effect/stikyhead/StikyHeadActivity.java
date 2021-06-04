package demo.zkttestdemo.effect.stikyhead;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;

/**
 * CoordinatorLayout实现悬浮头
 */
public class StikyHeadActivity extends AppCompatActivity {

    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stiky_head);

        final List<String> list = new ArrayList<>();
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");

        recycler = findViewById(R.id.recyclerView);
        recycler.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_recorder, list) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StikyHeadActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                RecyclerView recyclerView = helper.getView(R.id.rv_horizontal);
                recyclerView.setLayoutManager(linearLayoutManager);

                recyclerView.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_image, list) {
                    @Override
                    protected void convert(BaseViewHolder helper, String item) {
                    }
                });

            }
        });
    }

    /**测试滚动*/
    public void onClickScroll(View view) {
        int        y        = findViewById(R.id.root).getHeight() - 1; //减一是因为刚好在边界值是没办法触发事件的
        final long downTime = SystemClock.currentThreadTimeMillis();
        final long distance = 300;

        //按下
        MotionEvent down = MotionEvent.obtain(downTime, SystemClock.currentThreadTimeMillis(),
                                              MotionEvent.ACTION_DOWN, 0, y, 0);
        dispatchTouchEvent(down);

        //移动
        MotionEvent move = MotionEvent.obtain(downTime, SystemClock.currentThreadTimeMillis() + 50,
                                              MotionEvent.ACTION_MOVE, 0, y - distance, 0);
        dispatchTouchEvent(move);

        //抬起
        MotionEvent up = MotionEvent.obtain(downTime, SystemClock.currentThreadTimeMillis() + 50,
                                            MotionEvent.ACTION_UP, 0, y - distance, 0);
        dispatchTouchEvent(up);
    }
}
