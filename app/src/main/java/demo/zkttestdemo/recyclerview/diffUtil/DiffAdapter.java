package demo.zkttestdemo.recyclerview.diffUtil;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import demo.zkttestdemo.R;


public class DiffAdapter extends RecyclerView.Adapter<DiffAdapter.DiffVH> {
    private final static String TAG = "zxt";
    private List<TestBean> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;

    public DiffAdapter(Context mContext, List<TestBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setDatas(List<TestBean> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public DiffVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiffVH(mInflater.inflate(R.layout.item_diff, parent, false));
    }

    @Override
    public void onBindViewHolder(final DiffVH holder, final int position) {
        TestBean bean = mDatas.get(position);
        holder.tv1.setText(bean.getName());
        holder.tv2.setText(bean.getDesc());
        holder.iv.setImageResource(bean.getPic());
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    class DiffVH extends RecyclerView.ViewHolder {
        TextView tv1, tv2;
        ImageView iv;

        public DiffVH(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }
    }
}