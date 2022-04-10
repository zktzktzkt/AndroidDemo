package demo.zkttestdemo.effect.nestedscroll;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;

/**
 * 嵌套滑动测试
 */
public class NestedScrollTest3Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scroll_test_3);

        List<String> list = new ArrayList<String>() {
            {
                add("");
                add("");
                add("");
                add("");
                add("");
                add("");
                add("");
                add("");
            }
        };

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_color_rect, list) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                Log.e("测试测试", "convert");
            }
        });
    }
}
