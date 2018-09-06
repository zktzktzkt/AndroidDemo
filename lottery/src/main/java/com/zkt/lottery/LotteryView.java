package com.zkt.lottery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by zkt on 2018-9-6.
 * Description: 抽奖转盘效果
 */

public class LotteryView extends FrameLayout {

    private TextView tvLottery;
    private View[] views;
    private List<Integer> mAllTimesList;

    int timeIndex = 0;
    int viewsIndex = 0;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (viewsIndex == 0) {
                views[viewsIndex % views.length].setSelected(true);
            }
            views[viewsIndex % views.length].setSelected(false);
            views[++viewsIndex % views.length].setSelected(true);

            if (timeIndex <= mAllTimesList.size() - 1) {
                sendEmptyMessageDelayed(0, mAllTimesList.get(timeIndex));
                timeIndex++;
            } else {
                tvLottery.setClickable(true);
                timeIndex = 0;
                viewsIndex = 0;
            }
        }
    };


    public LotteryView(@NonNull Context context) {
        this(context, null);
    }

    public LotteryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LotteryView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(getContext(), R.layout.lottery, this);

        initView();
        initListener();

        initTimes();
    }

    private void initView() {
        tvLottery = findViewById(R.id.tv_lottery);

        TextView tv1 = findViewById(R.id.tv_1);
        TextView tv2 = findViewById(R.id.tv_2);
        TextView tv3 = findViewById(R.id.tv_3);
        TextView tv4 = findViewById(R.id.tv_4);
        TextView tv5 = findViewById(R.id.tv_5);
        TextView tv6 = findViewById(R.id.tv_6);
        TextView tv7 = findViewById(R.id.tv_7);
        TextView tv8 = findViewById(R.id.tv_8);

        views = new View[]{tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8};
    }

    /**
     * 初始化时间
     */
    private void initTimes() {
        List<Integer> startList = new ArrayList<>();
        startList.add(500);
        startList.add(500);
        startList.add(400);
        startList.add(400);
        startList.add(300);
        startList.add(300);
        startList.add(200);
        startList.add(200);
        startList.add(100);
        startList.add(100);

        List<Integer> middleList = new ArrayList<>();
        for (int i = 0; i < getRandomTimeCount(); i++) {
            middleList.add(50);
        }

        List<Integer> endList = new ArrayList<>();
        endList.add(100);
        endList.add(100);
        endList.add(200);
        endList.add(200);
        endList.add(300);
        endList.add(300);
        endList.add(400);
        endList.add(400);
        endList.add(500);
        endList.add(500);

        mAllTimesList = new ArrayList<>();
        mAllTimesList.addAll(startList);
        mAllTimesList.addAll(middleList);
        mAllTimesList.addAll(endList);
    }

    private void initListener() {
        tvLottery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < views.length; i++) {
                    views[i].setSelected(false);
                }
                if (tvLottery.isClickable()) {
                    initTimes();
                    startLottery();
                    tvLottery.setClickable(false);
                }
            }
        });
    }

    private int getRandomTimeCount() {
        int min = 100;
        int max = 150;

        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;

        return s;
    }

    /**
     * 开始抽奖
     */
    private void startLottery() {
        handler.sendEmptyMessage(0);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }


}







