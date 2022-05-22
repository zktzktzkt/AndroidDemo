package demo.zkttestdemo.effect.nestedscroll;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;

/**
 * 嵌套滑动测试
 */
public class SmartRefreshTestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_refresh_test);

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

        final SmartRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        refreshLayout.setEnableRefresh(false);
//        refreshLayout.setHeaderHeight(80);
//        refreshLayout.setHeaderInsetStart(80);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(500);
            }
        });

        recyclerView.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_color_rect, list) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                Log.e("测试测试", "convert");
            }
        });

    }
}
