package demo.zkttestdemo.effect.qqslidemenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;
import demo.zkttestdemo.effect.elemebtn.CustomAnimshopBtn;
import demo.zkttestdemo.effect.elemebtn.GoodsBean;

public class QQSlideMenuActivity extends AppCompatActivity {

    private QQSlideMenu_copy sliding_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqslide_menu);

        sliding_menu = findViewById(R.id.sliding_menu);
/*
        findViewById(R.id.iv_avator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭菜单 切换
                sliding_menu.toggleMenu();
            }
        });*/
        List<GoodsBean> mDatas = new ArrayList<>();
        for (int i = 0, len = 50; i < len; i++) {
            mDatas.add(new GoodsBean(i + "", 2, 5));
        }

        RecyclerView mRv = findViewById(R.id.rv);
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
