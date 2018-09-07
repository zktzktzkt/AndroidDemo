package com.zkt.lottery;

import android.support.annotation.IntRange;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zkt on 2018-9-6.
 * Description:
 */

public class LotteryRule {

    private List<Integer> mAllTimesList = new ArrayList<>();


    public LotteryRule() {
        initTimes(-1);
    }


    public void initTimes(@IntRange(from = -1, to = 7) int position) {
        mAllTimesList.clear();

        List<Integer> startList = new ArrayList<>();
        startList.add(500);
        startList.add(400);
        startList.add(400);
        startList.add(300);
        startList.add(200);
        startList.add(200);
        startList.add(100);
        startList.add(100);

        int randomTimeCount = getRandomTimeCount();
        List<Integer> middleList = new ArrayList<>();

        if (position != -1) {
            while (randomTimeCount % 8 != 0) {
                randomTimeCount++;
            }
            for (int i = 0; i < randomTimeCount - 1 + position; i++) {
                middleList.add(80);
            }
        } else {
            for (int i = 0; i < randomTimeCount; i++) {
                middleList.add(80);
            }
        }

        List<Integer> endList = new ArrayList<>();
        endList.add(100);
        endList.add(100);
        endList.add(200);
        endList.add(200);
        endList.add(300);
        endList.add(400);
        endList.add(500);
        endList.add(500);

        mAllTimesList.addAll(startList);
        mAllTimesList.addAll(middleList);
        mAllTimesList.addAll(endList);
    }


    private int getRandomTimeCount() {
        int min = 40;
        int max = 50;

        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;

        return s;
    }


    public List<Integer> getmAllTimesList() {
        return mAllTimesList;
    }

}
