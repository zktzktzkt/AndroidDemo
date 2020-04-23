package demo.zkttestdemo.recyclerview.header;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;

public class BannerRecyclerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> list;
    private HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_recycler);

        list = new ArrayList<>();
        list.add("123123");
        list.add("312312312");
        list.add("3123");
        list.add("1231");
        list.add("123123123");
        list.add("123123");
        list.add("2wqe");
        list.add("3123");
        list.add("wqeqw");
        list.add("32432");
        list.add("dsfsdf");
        list.add("6456");
        list.add("gregerger");
        list.add("rrtert123");
        list.add("32434ghrth");
        list.add("hdhdfh234234");
        list.add("hdhdfh234234");
        list.add("hdhdfh234234");
        list.add("hdhdfh234234");
        list.add("hdhdfh234234");
        list.add("hdhdfh234234");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        homeAdapter = new HomeAdapter(list);
        View header = LayoutInflater.from(this).inflate(R.layout.item_banner, null);
        Banner banner = (Banner) header.findViewById(R.id.banner);
        homeAdapter.addHeaderView(header);
        recyclerView.setAdapter(homeAdapter);

        List<Integer> list1 = new ArrayList<>();
        list1.add(R.mipmap.ic_launcher);
        list1.add(R.mipmap.ic_launcher);
        list1.add(R.mipmap.ic_launcher);
        list1.add(R.mipmap.ic_launcher);
        list1.add(R.mipmap.ic_launcher);
        list1.add(R.mipmap.ic_launcher);
        list1.add(R.mipmap.ic_launcher);
        list1.add(R.mipmap.ic_launcher);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(list1);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    /**
     * 删除最后一条
     */
    public void removeLastClick(View view) {
        list.remove(list.size() - 1);
        homeAdapter.notifyDataSetChanged();
    }


    public class HomeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public HomeAdapter(List<String> data) {
            super(R.layout.item_banner_recycler, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.tv_title, item);
        }
    }
}

