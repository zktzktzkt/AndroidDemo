package demo.zkttestdemo.recyclerview.nested;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;

import demo.zkttestdemo.R;


public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {

    int DEFAULT_COUNT = 20;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return DEFAULT_COUNT;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView        rvHorizontal;
        LinearLayoutManager layoutManager;

        public ViewHolder(View itemView) {
            super(itemView);
            rvHorizontal = (RecyclerView) itemView.findViewById(R.id.rv_horizontal);

            layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            rvHorizontal.setLayoutManager(layoutManager);

            final LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
            linearSnapHelper.attachToRecyclerView(rvHorizontal);

            rvHorizontal.setAdapter(new FakeAdapter(R.layout.item_card_hor));

            rvHorizontal.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        Log.e("OnScrollListener", "onScrollStateChanged: newState" + newState);
                        int firstPosition = layoutManager.findFirstVisibleItemPosition();
                        int lastPosition  = layoutManager.findLastVisibleItemPosition();
                        int diffPosition  = (lastPosition - firstPosition) / 2;
                        Log.e("OnScrollListener", "选中的：" + (firstPosition + diffPosition));
                        ToastUtils.showShort("选中的：" + (firstPosition + diffPosition));
                    }
                }
            });
        }

        public void bind() {
            layoutManager.scrollToPosition(0);
        }
    }
}
