package com.zkt.mylibrary;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zkt.mylibrary.bean.QQMessage;

import java.util.Collections;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyHolder> implements ItemTouchHelperAdapterCallback {
    private List<QQMessage> list;
    private StartDragListener dragListener;

    public MessageAdapter(List<QQMessage> list, StartDragListener dragListener) {
        this.list = list;
        this.dragListener = dragListener;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public ImageView iv_logo;
        public TextView tv_name;
        public TextView tv_time;
        public TextView tv_lastMsg;
        public EditText edittext;


        public MyHolder(View itemView) {
            super(itemView);
            iv_logo = (ImageView) itemView.findViewById(R.id.iv_logo);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_lastMsg = (TextView) itemView.findViewById(R.id.tv_lastMsg);
            edittext = itemView.findViewById(R.id.edittext);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        holder.itemView.setTag(position);
        final QQMessage qqMessage = list.get(position);
        holder.iv_logo.setImageResource(qqMessage.getLogo());
        holder.tv_name.setText(qqMessage.getName());
        holder.tv_lastMsg.setText(qqMessage.getLastMsg());
        holder.tv_time.setText(qqMessage.getTime());
        holder.iv_logo.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 传递事件
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //					ItemTouchHelper.startDrag();
                    dragListener.onStartDrag(holder);
                }
                return false;
            }
        });

        Log.e("qqqqqqq", "qqMessage.getEditContent() -> " + qqMessage.getEditContent() + "  ");
        Log.e("qqqqqqq", "onBindViewHolder position -> " + position + "  ");

        holder.edittext.setText(qqMessage.getEditContent());

        holder.edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((Integer) holder.itemView.getTag() == position) {
                    qqMessage.setEditContent(holder.edittext.getText().toString());
                }
            }
        });
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        return new MyHolder(view);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        //让数据集合中的两个数据进行位置交换
        Collections.swap(list, fromPosition, toPosition);
        //同时还要刷新RecyclerView
        //		notifyDataSetChanged();这种会刷新整个adapter不推荐使用
        notifyItemMoved(fromPosition, toPosition);

        return false;
    }

    @Override
    public void onItemSwiped(int adapterPosition) {
        //1.删除数据集合里面的position位置的数据
        list.remove(adapterPosition);
        //2.刷新adapter
        notifyItemRemoved(adapterPosition);
    }

}
