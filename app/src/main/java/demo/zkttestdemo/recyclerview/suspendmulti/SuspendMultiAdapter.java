package demo.zkttestdemo.recyclerview.suspendmulti;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import demo.zkttestdemo.R;
import demo.zkttestdemo.recyclerview.suspendsingle.SuspendSingleAdapter;

/**
 * Created by Administrator on 2016/12/29 0029.
 */
public class SuspendMultiAdapter extends RecyclerView.Adapter {
    Activity activity;
    public static final int TYPE_SUSPEND_HEAD = 0;
    public static final int TYPE_NORMAL = 1;

    public SuspendMultiAdapter(SuspendMultiActivity suspendMultiActivity) {
        this.activity = suspendMultiActivity;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 4 == 0)
            return TYPE_SUSPEND_HEAD;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_SUSPEND_HEAD) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suspend_header, parent, false);
            return new MyHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suspend_single, parent, false);
            return new SuspendSingleAdapter().new MyHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            ((MyHolder) holder).mTvTime.setText("zktzktzkt " + (1 + position / 4));
        } else if (holder instanceof SuspendSingleAdapter.MyHolder) {
            ((SuspendSingleAdapter.MyHolder) holder).tv_nickname.setText("zkt " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView mTvTime;

        public MyHolder(View itemView) {
            super(itemView);

            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
