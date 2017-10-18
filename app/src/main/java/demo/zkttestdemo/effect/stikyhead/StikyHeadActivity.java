package demo.zkttestdemo.effect.stikyhead;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;

public class StikyHeadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stiky_head);

        List<String> list = new ArrayList<>();
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

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recyclerView);
        recycler.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_recorder, list) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {

            }
        });
    }
}
