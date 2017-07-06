package demo.zkttestdemo.effect.multipage;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;
import demo.zkttestdemo.utils.DisplayUtil;

public class MultiPageActivity extends FragmentActivity {

    private ViewPager viewpager;
    private List<View> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_page);

        LayoutInflater lf = getLayoutInflater().from(this);

        View view1 = lf.inflate(R.layout.pager_view, null);
        View view2 = lf.inflate(R.layout.pager_view, null);
        View view3 = lf.inflate(R.layout.pager_view, null);
        View view4 = lf.inflate(R.layout.pager_view, null);
        View view5 = lf.inflate(R.layout.pager_view, null);
        View view6 = lf.inflate(R.layout.pager_view, null);

        list = new ArrayList<>();
        list.add(view1);
        list.add(view2);
        list.add(view3);
        list.add(view4);
        list.add(view5);
        list.add(view6);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setOffscreenPageLimit(2);
        ViewGroup.LayoutParams layoutParams = viewpager.getLayoutParams();
        layoutParams.width = DisplayUtil.getScreenWidth(this) - DisplayUtil.dip2px(80, this);
        viewpager.setLayoutParams(layoutParams);
        viewpager.setPageMargin(DisplayUtil.dip2px(10, this));

        ((ViewGroup) viewpager.getParent()).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewpager.dispatchTouchEvent(event);
            }
        });

        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(list.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(list.get(position));
                return list.get(position);
            }
        });
    }
}
