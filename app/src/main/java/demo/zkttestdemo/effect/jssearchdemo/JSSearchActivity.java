package demo.zkttestdemo.effect.jssearchdemo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import demo.zkttestdemo.R;

public class JSSearchActivity extends AppCompatActivity {

    boolean isExpand = false;

    private ImageView ivImg;
    private ScrollView scrollView;
    private TextView tvSearch;
    private LinearLayout llSearch;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jssearch);

        ivImg = (ImageView) findViewById(R.id.iv_img);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        tvSearch = (TextView) findViewById(R.id.tv_search);
        llSearch = (LinearLayout) findViewById(R.id.ll_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //设置全屏透明状态栏
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            ViewCompat.setFitsSystemWindows(rootView, false);
            rootView.setClipToPadding(true);
        }*/

         /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/

        // 大于4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        // 大于5.0
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        toolbar.getBackground().mutate().setAlpha(0);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                //改变toolbar的透明度
                changeToolbarAlpha();

                if (scrollView.getScrollY() >= ivImg.getHeight() - toolbar.getHeight() && !isExpand) {
                    expand();
                    isExpand = true;
                } else if (scrollView.getScrollY() <= 0 && isExpand) {
                    reduce();
                    isExpand = false;
                }
            }
        });
    }

    /**
     * 改变Toolbar的透明度
     */
    private void changeToolbarAlpha() {
        int scrollY = scrollView.getScrollY();
        //快速下拉会引起瞬间scrollY < 0
        if (scrollY < 0) {
            toolbar.getBackground().mutate().setAlpha(0);
            return;
        }
        //计算当前的透明度
        float radio = Math.min(1, scrollY / (ivImg.getHeight() - toolbar.getHeight() * 1f));
        //设置透明度
        toolbar.getBackground().mutate().setAlpha((int) (radio * 0xFF));
    }

    private void expand() {
        //设置伸展状态时的布局
        tvSearch.setText("搜索简书的内容和朋友");
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams) llSearch.getLayoutParams();
        LayoutParams.width = LayoutParams.MATCH_PARENT;
        LayoutParams.setMargins(dip2px(10), dip2px(10), dip2px(10), dip2px(10));
        llSearch.setLayoutParams(LayoutParams);
        //开始动画
        beginDelayedTransition(llSearch);
    }


    private void reduce() {
        //设置收缩状态时的布局
        tvSearch.setText("搜索");
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llSearch.getLayoutParams();
        layoutParams.width = dip2px(80);
        layoutParams.setMargins(dip2px(10), dip2px(10), dip2px(10), dip2px(10));
        llSearch.setLayoutParams(layoutParams);
        //开始动画
        beginDelayedTransition(llSearch);
    }

    /**
     * 开始动画
     */
    private void beginDelayedTransition(ViewGroup view) {
        AutoTransition autoTransition = new AutoTransition();
        autoTransition.setDuration(300);
        TransitionManager.beginDelayedTransition(view, autoTransition);
    }

    private int dip2px(float dpVale) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }

}
