package demo.zkttestdemo.effect.elemebtn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;

public class ElemeShopBtnActivity extends AppCompatActivity {
    RecyclerView mRv;
    List<GoodsBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleme_shop_btn);

        mDatas = new ArrayList<>();
        mDatas.add(new GoodsBean("1", 2, 5));
        mDatas.add(new GoodsBean("2", 2, 5));
        mDatas.add(new GoodsBean("3", 2, 5));
        mDatas.add(new GoodsBean("4", 2, 5));
        mDatas.add(new GoodsBean("5", 2, 5));
        mDatas.add(new GoodsBean("6", 2, 5));
        mDatas.add(new GoodsBean("7", 2, 5));
        mDatas.add(new GoodsBean("8", 2, 5));
        mDatas.add(new GoodsBean("9", 2, 5));
        mDatas.add(new GoodsBean("10", 2, 5));
        mDatas.add(new GoodsBean("11", 2, 5));
        mDatas.add(new GoodsBean("12", 2, 5));
        mDatas.add(new GoodsBean("13", 2, 5));
        mDatas.add(new GoodsBean("14", 2, 5));
        mDatas.add(new GoodsBean("15", 2, 5));
        mDatas.add(new GoodsBean("16", 2, 5));
        mDatas.add(new GoodsBean("17", 2, 5));
        mDatas.add(new GoodsBean("18", 2, 5));
        mDatas.add(new GoodsBean("19", 2, 5));
        mDatas.add(new GoodsBean("20", 2, 5));

        mRv = findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(new MyAdapter(mDatas));
    }

    public class MyAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {
        private MyAdapter(List<GoodsBean> data) {
            super(R.layout.item_shop_btn, data);
        }

        @Override
        protected void convert(final BaseViewHolder helper, final GoodsBean item) {
            helper.setText(R.id.tv, item.getName());
            CustomAnimshopBtn animShopBtn = helper.getView(R.id.shopBtn);
            animShopBtn.setCount(item.getCount());
            animShopBtn.setMaxCount(item.getMaxCount());

            animShopBtn.setOnNumChangeListener(new CustomAnimshopBtn.OnNumChangeListener() {
                @Override
                public void onNumChanged(int count) {
                    item.setCount(count);
                }
            });
        }
    }
}
