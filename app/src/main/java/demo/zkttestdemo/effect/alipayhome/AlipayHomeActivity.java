package demo.zkttestdemo.effect.alipayhome;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import demo.zkttestdemo.R;

public class AlipayHomeActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private final static String TAG = "AlipayActivity";
    private Toolbar toolbar;
    private AppBarLayout abl_bar;
    private CollapsingToolbarLayout ctl_top;

    private View tl_expand, tl_collapse;

    /**
     * 展开前的背景 - 搜索商品
     */
    private View v_expand_mask;
    /**
     * 收缩后显示的toolbar布局的 “蒙层”
     */
    private View v_collapse_mask;
    /**
     * 展开后的toolbar布局的 “蒙层”
     */
    private View v_pay_mask;

    private int mMaskColor;
    private RecyclerView rv_content;
    private FrameLayout container2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay_home);
        mMaskColor = getResources().getColor(R.color.blue_dark);
        rv_content = (RecyclerView) findViewById(R.id.rv_content);
        rv_content.setLayoutManager(new GridLayoutManager(this, 4));
        rv_content.setAdapter(new LifeAdapter(this, LifeItem.getDefault()));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        abl_bar = (AppBarLayout) findViewById(R.id.abl_bar);
        ctl_top = (CollapsingToolbarLayout) findViewById(R.id.ctl_top);
        tl_expand = (View) findViewById(R.id.tl_expand);
        tl_collapse = (View) findViewById(R.id.tl_collapse);
        v_expand_mask = (View) findViewById(R.id.v_expand_mask);
        v_collapse_mask = (View) findViewById(R.id.v_collapse_mask);
        v_pay_mask = (View) findViewById(R.id.v_pay_mask);
        container2 = findViewById(R.id.container2);
        abl_bar.addOnOffsetChangedListener(this);

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                ((ViewGroup.MarginLayoutParams) container2.getLayoutParams()).topMargin = toolbar.getBottom();
                container2.requestLayout();
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.e(TAG, "verticalOffset=" + verticalOffset + "  appBarLayout.getTotalScrollRange():" + appBarLayout.getTotalScrollRange());
        int offset = Math.abs(verticalOffset);
        int total = appBarLayout.getTotalScrollRange();

        int alpha = 255 * (offset / 2) / (total / 2);
        int expandMask = Color.argb(alpha, Color.red(mMaskColor), Color.green(mMaskColor), Color.blue(mMaskColor));
        int collapseMask = Color.argb(255 - alpha, Color.red(mMaskColor), Color.green(mMaskColor), Color.blue(mMaskColor));

        v_pay_mask.setBackgroundColor(expandMask);

        if (offset >= total / 2) {
            //显示隐藏的布局
            v_collapse_mask.setBackgroundColor(collapseMask);
            tl_collapse.setVisibility(View.VISIBLE);
            tl_expand.setVisibility(View.GONE);
        } else {
            //显示展开的布局
            v_expand_mask.setBackgroundColor(expandMask);
            tl_expand.setVisibility(View.VISIBLE);
            tl_collapse.setVisibility(View.GONE);
        }

    }
}
