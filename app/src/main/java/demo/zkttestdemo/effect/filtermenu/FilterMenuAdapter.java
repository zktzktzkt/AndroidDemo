package demo.zkttestdemo.effect.filtermenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2018-1-28.
 */

public class FilterMenuAdapter extends BaseMenuAdapter {
    private final Context context;
    private String[] mItems = {"类型", "品牌", "价格", "更多"};

    public FilterMenuAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public View getTabView(int position, ViewGroup parent) {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.filter_tab, parent, false);
        textView.setText(mItems[position]);
        return textView;
    }

    @Override
    public View getMenuView(int position, ViewGroup parent) {
        TextView menuView = (TextView) LayoutInflater.from(context).inflate(R.layout.filter_menu, parent, false);
        menuView.setText(mItems[position]);
        return menuView;
    }
}
