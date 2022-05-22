package demo.zkttestdemo.effect.nestedscroll;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
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
                add("");
                add("");
                add("");
            }
        };

        final FixNestedScrollView scrollView = findViewById(R.id.scrollView);
        final LinearLayout ll_c1 = findViewById(R.id.ll_c1);
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //设置子RV
        scrollView.post(() -> {

            scrollView.scrollTo(0, 0);

            //getMeasuredHeight()或getHeight() 测量出来的高度是屏幕的高度
            Log.e("scrollView高度", "getMeasuredHeight->" + scrollView.getMeasuredHeight() + " getHeight->" + scrollView.getHeight());

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
            //不必纠结这个数值，就是为了效果一点点调出来的，具体阈值的根据业务处理
            params.height = scrollView.getMeasuredHeight() - (ll_c1.getLayoutParams().height / 2);
            recyclerView.setLayoutParams(params);

            //设置高度之后再绑定Rv
            scrollView.attachRV(recyclerView, ll_c1.getLayoutParams().height / 2);

        });


        recyclerView.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_color_rect, list) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                Log.e("测试测试", "convert");
            }
        });

        //recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
        //    @Override
        //    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        //        //sv滑到底，但是rv没到顶
        //        boolean rvIsTop = !recyclerView.canScrollVertically(-1);
        //        boolean svIsBottom = scrollView.canScrollVertically(1);
        //        if(svIsBottom && !rvIsTop){
        //            recyclerView.setNestedScrollingEnabled(true);
        //        }
        //    }
        //});


        /*scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("嵌套滑动测试", "scrollY：" + scrollY);
                //-1代表顶部,返回true表示没到顶,还可以滑
                //1代表底部,返回true表示没到底部,还可以滑
                boolean rvIsTop = !recyclerView.canScrollVertically(-1);
                boolean svIsBottom = !v.canScrollVertically(1);
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
        });*/
    }
}
