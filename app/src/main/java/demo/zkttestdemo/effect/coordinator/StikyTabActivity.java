package demo.zkttestdemo.effect.coordinator;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;


public class StikyTabActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView3;
    private TabLayout tablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);

        final String[] titles = {"TAB1", "TAB2", "TAB3"};

        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        LayoutInflater lf = getLayoutInflater().from(this);
        View view1 = lf.inflate(R.layout.recyclerview, null);
        View view2 = lf.inflate(R.layout.recyclerview, null);
        View view3 = lf.inflate(R.layout.recyclerview, null);
        final List<View> viewContainter = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewContainter.add(view1);
        viewContainter.add(view2);
        viewContainter.add(view3);

        recyclerView = (RecyclerView) view1.findViewById(R.id.recycler);
        recyclerView.setAdapter(new StikyTabAdapter());

        recyclerView2 = (RecyclerView) view2.findViewById(R.id.recycler);
        recyclerView2.setAdapter(new StikyTabAdapter());

        recyclerView3 = (RecyclerView) view3.findViewById(R.id.recycler);
        recyclerView3.setAdapter(new StikyTabAdapter());


        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewContainter.get(position));
                return viewContainter.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewContainter.get(position));
            }
        });

        tablayout.setupWithViewPager(viewpager);

    }
}
