package demo.zkttestdemo.effect.baidusearch;

import android.animation.IntEvaluator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.zkttestdemo.R;

public class BDSearchActivity extends AppCompatActivity {

    private static final float ENDMARGINLEFT = 50;
    private static final float ENDMARGINTOP = 5;
    private static final float STARTMARGINLEFT = 20;
    private static final float STARTMARGINTOP = 140;
    private RelativeLayout rv_bar;
    private RelativeLayout rv_search;

    private ImageView iv_search;
    private int scrollLength;//顶部栏从透明变成不透明滑动的距离
    private NestedScrollView sv_search;
    private MyListview lv_searchview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bdsearch);

        rv_bar = findViewById(R.id.rv_bar);
        rv_search = findViewById(R.id.rv_search);
        sv_search = findViewById(R.id.sv_search);
        iv_search = findViewById(R.id.iv_search);
        lv_searchview = findViewById(R.id.lv_searchview);
        lv_searchview.setAdapter(new searchAdapter(BDSearchActivity.this));

        /***** 防止scrollview置顶 ***********/
        sv_search.setFocusable(true);
        sv_search.setFocusableInTouchMode(true);
        sv_search.requestFocus();
        /***********************************/

        sv_search.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private int evaluatemargin;
            private int evaluatetop;
            private FrameLayout.LayoutParams layoutParams;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //如果在变化的范围内
                if ((scrollLength - scrollY) > 0) {
                    // 算法：开始值 +（结束值 - 开始值）* 进度
                    IntEvaluator evaluator = new IntEvaluator();

                    // 总数-变动的值/总数 （ 变动的值范围：0~总数 ）
                    // 结果比例：1 ~ 0
                    float percent = (float) (scrollLength - scrollY) / scrollLength;

                    //标题栏透明度
                    int evaluate = evaluator.evaluate(percent, 255, 0);
                    rv_bar.getBackground().setAlpha(evaluate);

                    //搜索栏左右、上下的margin值
                    evaluatemargin = evaluator.evaluate(percent, DensityUtil.dip2px(BDSearchActivity.this, ENDMARGINLEFT),
                            DensityUtil.dip2px(BDSearchActivity.this, STARTMARGINLEFT));
                    evaluatetop = evaluator.evaluate(percent, DensityUtil.dip2px(BDSearchActivity.this, ENDMARGINTOP),
                            DensityUtil.dip2px(BDSearchActivity.this, STARTMARGINTOP));
                    layoutParams = (FrameLayout.LayoutParams) rv_search.getLayoutParams();
                    layoutParams.setMargins(evaluatemargin, evaluatetop, evaluatemargin, 0);

                } else {
                    rv_bar.getBackground().setAlpha(255);
                    if (layoutParams != null) {
                        layoutParams.setMargins(DensityUtil.dip2px(BDSearchActivity.this, ENDMARGINLEFT),
                                DensityUtil.dip2px(BDSearchActivity.this, 5),
                                DensityUtil.dip2px(BDSearchActivity.this, ENDMARGINLEFT), 0);
                    }
                }

                rv_search.setLayoutParams(layoutParams);

            }
        });

    }


    /**
     * 这个函数的含义是：view已经初始化完毕了，宽/高已经准备好了，这个时候去获取宽高是可以成功获取的。
     * 但是需要注意的是onWindowFocusChanged函数会被调用多次，当Activity的窗口得到焦点和失去焦点时均会被调用一次，
     * 如果频繁地进行onResume和onPause，那么onWindowFocusChanged也会被频繁地调用。
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            int height_rv = rv_bar.getHeight();
            int height_iv = iv_search.getHeight();

            scrollLength = Math.abs(height_iv - height_rv);

            //把顶部bar设置为透明
            rv_bar.getBackground().setAlpha(0);

        }

    }

    class searchAdapter extends BaseAdapter {
        Context context;
        private View view;
        private TextView tv_search;

        public searchAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 40;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                view = View.inflate(context, R.layout.recyclerview_item, null);
            } else {
                view = convertView;
            }

            return view;
        }
    }
}
