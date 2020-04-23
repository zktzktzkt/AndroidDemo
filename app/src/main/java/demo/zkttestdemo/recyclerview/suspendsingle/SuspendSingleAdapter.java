package demo.zkttestdemo.recyclerview.suspendsingle;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import demo.zkttestdemo.R;

/**
 * Created by Administrator on 2016/12/29 0029.
 */
public class SuspendSingleAdapter extends RecyclerView.Adapter<SuspendSingleAdapter.MyHolder> {

    @Override
    public SuspendSingleAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("Adapter: ", "onCreateViewHolder");
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suspend_single, parent, false));
    }

    @Override
    public void onBindViewHolder(SuspendSingleAdapter.MyHolder holder, int position) {
        holder.tv_nickname.setText("zkt " + position);
        Log.e("Adapter: ", "onBindViewHolder");
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.e("Adapter: ", "onAttachedToRecyclerView");
    }

    @Override
    public int getItemViewType(int position) {
        Log.e("Adapter: ", "getItemViewType");
        return super.getItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(MyHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.e("Adapter: ", "onViewAttachedToWindow");
    }

    @Override
    public int getItemCount() {
        Log.e("Adapter: ", "getItemCount");
        return 20;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView tv_nickname;

        public MyHolder(View itemView) {
            super(itemView);

            tv_nickname = (TextView) itemView.findViewById(R.id.tv_nickname);
        }
    }
}
