package demo.zkttestdemo.recyclerview.refresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import demo.zkttestdemo.R;
import demo.zkttestdemo.recyclerview.refresh.bean.Body;

/**
 * Created by zkt on 18/10/12.
 * Description:
 */

public class MyAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private Context mContext;
    //数据
    private List<Body> bodyList;

    public MyAdapter(Context context, List<Body> bodyList) {
        this.mContext = context;
        this.bodyList = bodyList;
        inflater = LayoutInflater.from(context);
    }

    public static final int TYPE_BODY = 1;
    public static final int TYPE_FOOT = 2;

    @Override
    public int getItemViewType(int position) {
        int viewType = -1;
        if (position == bodyList.size()) {
            viewType = TYPE_FOOT;
        } else {
            viewType = TYPE_BODY;
        }
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_BODY:
                holder = new BodyViewHolder(inflater.inflate(R.layout.item_body, parent, false));
                break;
            case TYPE_FOOT:
                holder = new FootViewHolder(inflater.inflate(R.layout.item_body, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BodyViewHolder) {
            BodyViewHolder bodyViewHolder = (BodyViewHolder) holder;
            Body body = bodyList.get(position);
            bodyViewHolder.tvBody.setText(body.getName() + " hello" + body.getAge());
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return bodyList.size() + 1;
    }

    class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    class BodyViewHolder extends RecyclerView.ViewHolder {
        //加自己的功能
        public TextView tvBody;

        public BodyViewHolder(View itemView) {
            super(itemView);
            tvBody = itemView.findViewById(R.id.tv_body);
        }
    }


}
