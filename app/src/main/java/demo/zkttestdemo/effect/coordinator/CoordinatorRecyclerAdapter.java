package demo.zkttestdemo.effect.coordinator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2017/5/28.
 */

class CoordinatorRecyclerAdapter extends RecyclerView.Adapter<CoordinatorRecyclerAdapter.MyHolder> {
    @Override
    public CoordinatorRecyclerAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CoordinatorRecyclerAdapter.MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 50;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
