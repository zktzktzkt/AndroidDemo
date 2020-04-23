package com.zkt.mylibrary;

import android.graphics.Canvas;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;

public class MessageItemTouchCallback extends ItemTouchHelper.Callback {

    private static final String TAG = "ItemTouchCallback";
    private ItemTouchHelperAdapterCallback adapterCallback;

    public MessageItemTouchCallback(ItemTouchHelperAdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, ViewHolder holder) {
        //callback回调监听哪些动作？---判断方向

        //makeMovementFlags(UP | DOWN, LEFT);
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * 上下滑动监听（水平方向、垂直方向）
     */
    @Override
    public boolean onMove(RecyclerView arg0, ViewHolder srcHolder, ViewHolder targetHolder) {
        // 1.让数据集合中的两个数据进行位置交换
        // 2.刷新adapter
        adapterCallback.onItemMove(srcHolder.getAdapterPosition(), targetHolder.getAdapterPosition());
        return true;
    }

    /**
     * 左右滑动监听
     */
    @Override
    public void onSwiped(ViewHolder holder, int arg1) {
        // 1.删除数据集合里面的position位置的数据
        // 2.刷新adapter
        adapterCallback.onItemSwiped(holder.getAdapterPosition());
    }

    /**
     * 开启长安拖拽
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            ViewHolder viewHolder, float dX, float dY, int actionState,
                            boolean isCurrentlyActive) {
        Log.d(TAG, "onChildDraw");
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
                isCurrentlyActive);
    }

    //滑动消失的距离，当滑动小于这个值的时候会删除这个item，否则不会视为删除
    //返回值作为用户视为拖动的距离
    @Override
    public float getSwipeThreshold(ViewHolder viewHolder) {
        return 0.1f;
    }

    //返回值滑动消失的速度，滑动小于这个值不消失，大于消失
    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 5f;
    }

    //设置手指离开后ViewHolder的动画时间
    @Override
    public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        return 100;
    }

    //网格型RecyclerView
    @Override
    public float getMoveThreshold(ViewHolder viewHolder) {
        return 0.9f;
    }

    //返回值决定是否有滑动操作
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


}
