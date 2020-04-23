package demo.zkttestdemo.effect.pullAndload;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class AliPtrActivity extends Activity {
    private List<String> mDatas = new ArrayList<String>();

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private SpringView springView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ali);


        initData();

        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 3, GridLayoutManager.VERTICAL, false));
        //        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerViewAdapter = new RecyclerViewAdapter(this, mDatas);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        springView = (SpringView) findViewById(R.id.springview);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        springView.onFinishFreshAndLoad();
                    }
                }, 1000);
            }

            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        springView.onFinishFreshAndLoad();
                    }
                }, 1000);
            }
        });
        springView.setHeader(new AliHeader(this, R.drawable.ali, true));   //参数为：logo图片资源，是否显示文字
        springView.setFooter(new AliFooter(this, false));

    }

    private void initData() {
        for (int i = 0; i < 9; i++) {
            mDatas.add(i == 0 ? "We are in RecyclerView" : (i == 1 ? "SpringView支持RecyclerView\n\n这是一个仿阿里旅行的header，logo可以图片可自行替换" : ""));
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add: {
                mDatas.add("add item");
                recyclerViewAdapter.notifyItemInserted(mDatas.size() - 1);
                break;
            }
            case R.id.sub:
                if (mDatas.size() > 0) {
                    int position = mDatas.size() - 1;
                    mDatas.remove(position);
                    recyclerViewAdapter.notifyItemRemoved(position);
                }
                break;
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.SampleViewHolder> {
        private List<String> results;

        public RecyclerViewAdapter(Context context, List<String> results) {
            this.results = results;
        }

        @Override
        public SampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new SampleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SampleViewHolder holder, final int position) {
            holder.text_item.setText(results.get(position));
            if (position % 2 == 1) {
                holder.text_item.setBackgroundColor(Color.parseColor("#e3f1fc"));
                holder.text_item.setTextColor(Color.parseColor("#9dd2fc"));
            } else {
                holder.text_item.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.text_item.setTextColor(Color.parseColor("#cccccc"));
            }
        }

        @Override
        public int getItemCount() {
            return results.size();
        }

        public class SampleViewHolder extends RecyclerView.ViewHolder {
            public TextView text_item;

            public SampleViewHolder(View view) {
                super(view);
                text_item = (TextView) view.findViewById(R.id.item_text);
            }
        }
    }
}
