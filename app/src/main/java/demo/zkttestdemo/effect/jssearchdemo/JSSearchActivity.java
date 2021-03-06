package demo.zkttestdemo.effect.jssearchdemo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.transition.TransitionManager;

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

        llSearch.post(new Runnable() {
            @Override
            public void run() {
                int[] screenInt = new int[2];
                int[] windowInt = new int[2];
                llSearch.getLocationOnScreen(screenInt);
                llSearch.getLocationInWindow(windowInt);
                Log.e("TAG", "screenInt y->" + screenInt[1] + " windowInt y->" + windowInt[1]);
            }
        });

        // 大于5.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option =
                    //布局延伸到状态栏里
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            //隐藏导航栏
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            //隐藏状态栏，并且下拉状态栏，显示3秒后自动隐藏
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(option);
            //状态栏和导航栏设置为透明
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        // 大于4.4 小于5.0
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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
        //过渡动画
        TransitionManager.beginDelayedTransition(llSearch);
        //设置伸展状态时的布局
        tvSearch.setText("搜索简书的内容和朋友");
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams) llSearch.getLayoutParams();
        LayoutParams.width = LayoutParams.MATCH_PARENT;
        LayoutParams.setMargins(dip2px(10), dip2px(10), dip2px(10), dip2px(10));
        llSearch.setLayoutParams(LayoutParams);
    }


    private void reduce() {
        //过渡动画
        TransitionManager.beginDelayedTransition(llSearch);
        //设置收缩状态时的布局
        tvSearch.setText("搜索");
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llSearch.getLayoutParams();
        layoutParams.width = dip2px(80);
        layoutParams.setMargins(dip2px(10), dip2px(10), dip2px(10), dip2px(10));
        llSearch.setLayoutParams(layoutParams);
    }


    private int dip2px(float dpVale) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }

}
