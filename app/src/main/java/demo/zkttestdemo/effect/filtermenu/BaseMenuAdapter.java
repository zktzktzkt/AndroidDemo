package demo.zkttestdemo.effect.filtermenu;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zkt on 2018-1-28.
 */

public abstract class BaseMenuAdapter {
    //获取总共有多少条
    public abstract int getCount();

    //获取当前的tabView
    public abstract View getTabView(int position, ViewGroup parent);

    //获取菜单内容
    public abstract View getMenuView(int position, ViewGroup parent);

}
