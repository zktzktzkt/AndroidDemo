package demo.zkttestdemo.effect.stikyhead;

import android.os.Bundle;
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

        RecyclerView recycler = findViewById(R.id.recyclerView);
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
}
