package com.zkt.animatorframework;

/**
 * Created by zkt on 2018-10-9.
 * Description:
 */

public interface DiscrollInterface {
    /**
     * 滑动的时候做的具体的事情
     * 执行动画的操作我们都放在这里
     */
    void onDiscroll(float ratio);

    /**
     *
     */
    void onResetDiscroll();
}
