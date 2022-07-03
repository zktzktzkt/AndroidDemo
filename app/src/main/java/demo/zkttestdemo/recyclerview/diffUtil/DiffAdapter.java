package demo.zkttestdemo.recyclerview.diffUtil;

import android.content.Context;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

import demo.zkttestdemo.R;


public class DiffAdapter extends RecyclerView.Adapter<DiffAdapter.DiffVH> {
    private final static String TAG = "zkt";
    private List<TestBean> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;

    public DiffAdapter(Context mContext, List<TestBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    public List<TestBean> getDatas() {
        return mDatas;
    }

    /**
     * 设置数据
     */
    public void setDatas(List<TestBean> datas) {
        if (null == mDatas) {
            this.mDatas = datas;
            notifyDataSetChanged();
        } else {
            updateData(datas);
        }
    }

    /**
     * diffutil刷新数据
     */
    public void updateData(List<TestBean> newDatas) {
        //利用DiffUtil.calculateDiff()方法，传入一个规则DiffUtil.Callback对象，和是否检测移动item的 boolean变量，得到DiffUtil.DiffResult 的对象
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(mDatas, newDatas), true);
        diffResult.dispatchUpdatesTo(this);

        //更新数据源
        this.mDatas = newDatas;
    }

    @Override
    public DiffVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiffVH(mInflater.inflate(R.layout.item_diff, parent, false));
    }

    @Override
    public void onBindViewHolder(final DiffVH holder, int position) {
        TestBean bean = mDatas.get(position);
        holder.tv1.setText(bean.getName());
        holder.tv2.setText(bean.getDesc());
        holder.iv.setImageResource(bean.getPic());

        holder.iv.setOnClickListener(v -> {
            //TODO 警告：一定要用ViewHolder中获取position的方法。如果外界有直接位移数据源中的对象的操作，经过diffutil后，方法实参position错乱。
            ToastUtils.showShort("position：" + position + " LayoutPosition：" + holder.getLayoutPosition());
        });
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