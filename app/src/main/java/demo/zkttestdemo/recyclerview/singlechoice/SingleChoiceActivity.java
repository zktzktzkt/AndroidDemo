package demo.zkttestdemo.recyclerview.singlechoice;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.zkttestdemo.R;

/**
 * Created by Administrator on 2016/12/28 0028.
 */

public class SingleChoiceActivity extends Activity {

    private RecyclerView recyclerView;
    private int mScreenWidth;
    private static final float MIN_SCALE = .95f;
    private static final float MAX_SCALE = 1.15f;
    private int mMinWidth;
    private int mMaxWidth;
    private TextView tv1;
    private TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_choice);
        recyclerView = findViewById(R.id.recyclerView);
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);

        /*mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mMinWidth = (int) (mScreenWidth * 0.28f);
        mMaxWidth = mScreenWidth - 2 * mMinWidth;*/

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new SingleChoiceAdapter(this));
        //取消item的动画，防止item刷新产生闪烁
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        // recyclerView.addOnScrollListener(mOnScrollListener);
    }


    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            final int childCount = recyclerView.getChildCount();
            Log.e("tag", childCount + "");
            for (int i = 0; i < childCount; i++) {
                RelativeLayout child = (RelativeLayout) recyclerView.getChildAt(i);
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();

                lp.rightMargin = 5;
                lp.height = 200;

                int left = child.getLeft();
                int right = mScreenWidth - child.getRight();
                tv1.setText("i：" + i
                        + "\n" + "child.getLeft()：" + left
                        + "\n" + "child.getRight()：" + child.getRight());

                final float percent = left < 0 || right < 0 ? 0 : Math.min(left, right) * 1f / Math.max(left, right);
                Log.e("tag", "percent = " + percent);
                float scaleFactor = MIN_SCALE + Math.abs(percent) * (MAX_SCALE - MIN_SCALE);
                int width = (int) (mMinWidth + Math.abs(percent) * (mMaxWidth - mMinWidth));
                lp.width = width;

                child.setLayoutParams(lp);
                child.setScaleY(scaleFactor);

                if (percent > 1f / 3) {
                    ((TextView) child.getChildAt(1)).setTextColor(Color.BLUE);
                } else {
                    ((TextView) child.getChildAt(1)).setTextColor(Color.BLACK);
                }
            }
        }
    };

}
