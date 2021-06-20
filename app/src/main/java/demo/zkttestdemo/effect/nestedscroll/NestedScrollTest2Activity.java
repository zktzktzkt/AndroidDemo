package demo.zkttestdemo.effect.nestedscroll;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;

/**
 * 嵌套滑动测试
 */
public class NestedScrollTest2Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scroll_test_2);

        List<String> list = new ArrayList<String>() {
            {
                add("");
                add("");
                add("");
                add("");
                add("");
            }
        };

        final NestedScrollView scrollView = findViewById(R.id.scrollView);
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_color_rect, list) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {

            }
        });


        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("嵌套滑动测试", "scrollY：" + scrollY);
                //-1代表顶部,返回true表示没到顶,还可以滑
                //1代表底部,返回true表示没到底部,还可以滑
                boolean rvIsTop = !recyclerView.canScrollVertically(-1);
                boolean svIsBottom = v.canScrollVertically(1);
                if (svIsBottom) {
                    if (!recyclerView.isNestedScrollingEnabled()) {
                        recyclerView.setNestedScrollingEnabled(true);
                    }
                } else {
                    if (recyclerView.isNestedScrollingEnabled()) {
                        recyclerView.setNestedScrollingEnabled(false);
                    }
                }
            }
        });
    }
}
