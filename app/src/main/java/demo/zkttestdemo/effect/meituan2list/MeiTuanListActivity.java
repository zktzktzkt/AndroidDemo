package demo.zkttestdemo.effect.meituan2list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;
import demo.zkttestdemo.effect.meituan2list.mode.ProductType;
import demo.zkttestdemo.effect.meituan2list.mode.ShopProduct;
import demo.zkttestdemo.effect.meituan2list.stikylist.PinnedHeaderListView;

public class MeiTuanListActivity extends AppCompatActivity {

    private RecyclerView         left_list;
    private PinnedHeaderListView right_list;
    private boolean              isScroll = true;
    private List<String>         strings;
    private SectionAdapter       sectionAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mei_tuan_list);

        getData();

        initView();

        initData();
    }

    private void initView() {
        left_list = (RecyclerView) findViewById(R.id.left_list);
        right_list = (PinnedHeaderListView) findViewById(R.id.header_list);
    }

    private void initData() {
        //======================= 设置左边的列表 ==================================
        left_list.setAdapter(new QuickAdapter());
        left_list.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                isScroll = false;
                view.setSelected(true);

                for (int i = 0; i < left_list.getChildCount(); i++) {
                    view.setSelected(i == position);
                }

                //记录选中的item对应的右边section的位置
                int rightSection = 0;
                for (int i = 0; i < position; i++) {
                    rightSection += sectionAdapter.getCountForSection(i) + 1; //子item的数量加上title
                }
                right_list.setSelection(rightSection);
            }
        });

        //====================== 设置右边的列表 ================================
        sectionAdapter = new SectionAdapter(getApplicationContext(), getData());
        right_list.setAdapter(sectionAdapter);

        //监听右边list，动态改变左边状态
        right_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //这个boolean的作用是防止右边列表对应左边列表的item的bg覆盖了左边item点击后的bg
                if (isScroll) {
                    //判断右边第一个显示的position的Section
                    for (int i = 0; i < left_list.getChildCount(); i++) {
                        left_list.getChildAt(i).setSelected(
                                i == sectionAdapter.getSectionForPosition(firstVisibleItem)
                        );
                    }
                } else {
                    isScroll = true;
                }
            }
        });
    }

    /**
     * 左边列表的adapter
     */
    public class QuickAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public QuickAdapter() {
            super(R.layout.item_left_category, strings);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, String item) {
            viewHolder.setText(R.id.textItem, item);
        }
    }


    /**
     * 设置数据
     */
    private List<ProductType> getData() {
        List<ProductType> productList = new ArrayList<>();

        //先设置分类title，再设置子数据
        //  [{"type":"标题","childs":[{"信息1":"信息"},{"信息2":"信息"}]}, {"type":"标题","childs":[{"信息1":"信息"}]}]
        for (int i = 1; i < 10; i++) {
            //标题title
            ProductType productCategory = new ProductType();
            productCategory.setType("标题" + i);

            //子数据
            List<ShopProduct> shopProductAll = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                ShopProduct shopProduct = new ShopProduct();
                shopProduct.setGoods("商品" + j);
                shopProductAll.add(shopProduct);
            }
            productCategory.setProduct(shopProductAll);

            productList.add(productCategory);
        }

        //左边的title
        strings = new ArrayList<>();
        for (ProductType type : productList) {
            strings.add(type.getType());
        }

        return productList;
    }


}
