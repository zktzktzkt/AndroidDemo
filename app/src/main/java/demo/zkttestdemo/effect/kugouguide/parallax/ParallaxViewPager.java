package demo.zkttestdemo.effect.kugouguide.parallax;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2018-2-27.
 * 视差动画的viewpager
 */

public class ParallaxViewPager extends ViewPager {
    List<ParallaxFragment> mFragments;
    private List<View> parallaxViews;

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

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //滚动 position 当前位置 positionOffset 0-1  positionOffsetPixels 0-屏幕的宽度px
                Log.e("TAG", "position->" + position + " positionOffset->" + positionOffset + " positionOffsetPixels->" + positionOffsetPixels);

                //获取左out 右in
                try {
                    ParallaxFragment outFragment = mFragments.get(position);
                    parallaxViews = outFragment.getParallaxViews();
                    for (View parallaxView : parallaxViews) {
                        ParallaxTag tag = (ParallaxTag) parallaxView.getTag(R.id.parallax_tag);
                        parallaxView.setTranslationX((-positionOffsetPixels) * tag.translationXOut);
                        parallaxView.setTranslationY((-positionOffsetPixels) * tag.translationYOut);
                    }

                    ParallaxFragment inFragment = mFragments.get(position + 1);
                    parallaxViews = inFragment.getParallaxViews();
                    for (View parallaxView : parallaxViews) {
                        ParallaxTag tag = (ParallaxTag) parallaxView.getTag(R.id.parallax_tag);
                        parallaxView.setTranslationX((getMeasuredWidth() - positionOffsetPixels) * tag.translationXIn);
                        parallaxView.setTranslationY((getMeasuredWidth() - positionOffsetPixels) * tag.translationYIn);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
