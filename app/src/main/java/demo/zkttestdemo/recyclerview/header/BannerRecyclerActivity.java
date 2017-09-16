package demo.zkttestdemo.recyclerview.header;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_recycler);

        list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        HomeAdapter homeAdapter = new HomeAdapter(list);
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


    public class HomeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public HomeAdapter(List<String> data) {
            super(R.layout.item_life, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
        }
    }
}

