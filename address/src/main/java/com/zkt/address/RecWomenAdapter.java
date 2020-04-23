package com.zkt.address;


import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zkt.progress.R;

import java.util.ArrayList;
import java.util.List;


public class RecWomenAdapter extends RecyclerView.Adapter<RecWomenAdapter.Holder> {

    private List<Women> mList;

    private Activity mActivity;

    public RecWomenAdapter(Activity activity) {
        mList = new ArrayList<>();
        mActivity = activity;
    }

    public void addDatas(List<Women> data) {
        this.mList.clear();
        this.mList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rec_women, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Women women = mList.get(position);
        holder.tv.setText(women.getName());
        //        Glide.with(mActivity).load(women.getPhoto()).into(holder.iv_logo);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    static class Holder extends RecyclerView.ViewHolder {
        TextView tv;

        Holder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(tv.getContext(), "点击item" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}