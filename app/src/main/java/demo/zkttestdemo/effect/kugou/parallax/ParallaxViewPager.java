package demo.zkttestdemo.effect.kugou.parallax;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zkt on 2018-2-27.
 * 视差动画的viewpager
 */

public class ParallaxViewPager extends ViewPager {
    List<ParallaxFragment> mFragments;

    public ParallaxViewPager(Context context) {
        this(context, null);
    }

    public ParallaxViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        mFragments = new ArrayList<>();
    }


    /**
     * 设置布局数组
     *
     * @param layoutIds
     */
    public void setLayout(FragmentManager fm, int[] layoutIds) {
        mFragments.clear();
        for (int layoutId : layoutIds) {
            ParallaxFragment fragment = new ParallaxFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ParallaxFragment.LAYOUT_ID_KEY, layoutId);
            fragment.setArguments(bundle);

            mFragments.add(fragment);
        }
        setAdapter(new ParallaxPagerAdapter(fm));
    }

    private class ParallaxPagerAdapter extends FragmentPagerAdapter {

        public ParallaxPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
