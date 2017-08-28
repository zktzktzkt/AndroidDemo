package demo.zkttestdemo.effect.meituan2list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import demo.zkttestdemo.R;
import demo.zkttestdemo.effect.meituan2list.mode.ProductType;
import demo.zkttestdemo.effect.meituan2list.mode.ShopProduct;
import demo.zkttestdemo.effect.meituan2list.stikylist.SectionedBaseAdapter;

/**
 * Created by zkt on 2017/8/27.
 */

public class SectionAdapter extends SectionedBaseAdapter {

    private List<ProductType> productCagests;
    private Context context;
    private LayoutInflater mInflater;

    public SectionAdapter(Context context, List<ProductType> pruductCagests) {
        this.context = context;
        this.productCagests = pruductCagests;
        mInflater = LayoutInflater.from(context);
    }

    //获取子item
    @Override
    public Object getItem(int section, int position) {
        return productCagests.get(section).getProduct().get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    //获取总数量
    @Override
    public int getSectionCount() {
        return productCagests.size();
    }

    //获取头标题所对应的子数据的数量
    @Override
    public int getCountForSection(int section) {
        return productCagests.get(section).getProduct().size();
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_category, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        ((TextView) layout.findViewById(R.id.textItem)).setText(productCagests.get(section).getType());
        return layout;
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ShopProduct shopProduct = productCagests.get(section).getProduct().get(position);
        viewHolder.name.setText(shopProduct.getGoods());

        return convertView;
    }

    static class ViewHolder {
        public TextView name;
    }
}
