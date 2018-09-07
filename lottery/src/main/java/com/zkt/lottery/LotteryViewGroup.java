package com.zkt.lottery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by zkt on 2018-9-6.
 * Description: 抽奖大转盘
 */

public class LotteryViewGroup extends ViewGroup {

    private int mGap = 5;
    private int childWidth;
    private int childHeight;
    int viewsIndex = 0;
    int timeIndex = 0;
    private LotteryRule mRule;
    private ImageView ivLottery;
    private int mSelectPos;
    private View[] mViews;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mViews[viewsIndex % mViews.length].setSelected(false);
            viewsIndex++;
            mViews[viewsIndex % mViews.length].setSelected(true);

            //取时间，依次执行
            if (timeIndex < mRule.getmAllTimesList().size()) {
                sendEmptyMessageDelayed(0, mRule.getmAllTimesList().get(timeIndex));
                timeIndex++;
            } else {
                if (null != mOnSelectedListener) {
                    mOnSelectedListener.onSelect(viewsIndex % mViews.length);
                }
                //时间都跑完了，重置数据
                ivLottery.setClickable(true);
                timeIndex = 0;
                viewsIndex = 0;


            }
        }
    };

    public LotteryViewGroup(Context context) {
        this(context, null);
    }


    public LotteryViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LotteryViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化规则
        mRule = new LotteryRule();

        mGap = dp2px(mGap);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();

        ivLottery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "抽奖", Toast.LENGTH_SHORT).show();

                for (int i = 0; i < mViews.length; i++) {
                    mViews[i].setSelected(false);
                }
                if (ivLottery.isClickable()) {
                    //让第一个选中
                    mViews[0].setSelected(true);
                    //初始化转圈的速度时间
                    mRule.initTimes(mSelectPos);
                    //不可点击
                    ivLottery.setClickable(false);

                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //开始抽奖
                            handler.sendEmptyMessage(0);
                        }
                    }, 500);
                }

            }
        });
    }

    /**
     * 初始化所有的View
     */
    private void initViews() {
        View v1 = findViewById(R.id.view1);
        View v2 = findViewById(R.id.view2);
        View v3 = findViewById(R.id.view3);
        View v4 = findViewById(R.id.view4);
        View v5 = findViewById(R.id.view5);
        View v6 = findViewById(R.id.view6);
        View v7 = findViewById(R.id.view7);
        View v8 = findViewById(R.id.view8);
        ivLottery = findViewById(R.id.iv_lottery);

        mViews = new View[]{v1, v2, v3, v4, v5, v6, v7, v8};
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //先测量所有子View
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        View child = getChildAt(0);
        int childWidth = child.getMeasuredWidth();
        int width = childWidth * 3 + mGap * 4;
        int height = width;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        childWidth = getChildAt(0).getMeasuredWidth();
        childHeight = getChildAt(0).getMeasuredHeight();

        Log.e("LotteryViewGroup", "childWidth:" + childWidth + "childHeight:" + childHeight);

        //最上面三个
        for (int i = 0; i < 3; i++) {
            View childView = getChildAt(i);
            childView.layout((i + 1) * mGap + i * childWidth, mGap,
                    (i + 1) * mGap + (i + 1) * childWidth, mGap + childHeight);
        }

        //中间层三个
        {
            View childView3 = getChildAt(3);
            childView3.layout(mGap, mGap * 2 + childHeight,
                    mGap + childWidth, mGap * 2 + childHeight * 2);

            View childView4 = getChildAt(4);
            childView4.layout(mGap * 2 + childWidth, mGap * 2 + childHeight,
                    mGap * 2 + childWidth * 2, mGap * 2 + childHeight * 2);

            View childView5 = getChildAt(5);
            childView5.layout(mGap * 3 + childWidth * 2, mGap * 2 + childHeight,
                    mGap * 3 + childWidth * 3, mGap * 2 + childHeight * 2);
        }

        //最下层三个
        for (int i = 0; i < 3; i++) {
            View childView = getChildAt(i + 6);
            childView.layout((i + 1) * mGap + i * childWidth, mGap * 3 + childHeight * 2,
                    (i + 1) * mGap + (i + 1) * childWidth, mGap * 3 + childHeight * 3);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private OnSelectedListener mOnSelectedListener;

    public interface OnSelectedListener {
        void onSelect(int position);
    }

    /**
     * 设置最终选中的监听
     */
    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mOnSelectedListener = onSelectedListener;
    }

    /**
     * 设置哪个选中
     */
    public void setSelect(int position) {
        this.mSelectPos = position;
    }
}
