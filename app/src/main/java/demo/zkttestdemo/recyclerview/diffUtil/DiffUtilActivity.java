package demo.zkttestdemo.recyclerview.diffUtil;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import demo.zkttestdemo.R;


/**
 * Created by rubing on 2017/5/6.
 * rubingem@163.com
 */

public class DiffUtilActivity extends AppCompatActivity {

    private RecyclerView mRv;
    private DiffAdapter mAdapter;
    private List<TestBean> mDatas;
    private Button mBtnRefresh;

    {
        mDatas = new ArrayList<>();
        mDatas.add(new TestBean(0, "张旭童0", "原始", R.drawable.ic_launcher));
        mDatas.add(new TestBean(1, "张旭童1", "原始", R.drawable.ic_launcher));
        mDatas.add(new TestBean(2, "张旭童2", "原始", R.drawable.ic_launcher));
        mDatas.add(new TestBean(3, "张旭童3", "原始", R.drawable.ic_launcher));
        mDatas.add(new TestBean(4, "张旭童4", "原始", R.drawable.ic_launcher));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diff_activity);
        initView();

        mRv = ((RecyclerView) findViewById(R.id.diff_rv));
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DiffAdapter(this, null);
        mRv.setAdapter(mAdapter);

        mAdapter.setDatas(mDatas); //设置数据

        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
    }


    /**
     * 模拟刷新操作
     */
    public void onRefresh() {
        try {
            List<TestBean> newDatas = new ArrayList<>();
            //clone一遍旧数据。后面会把第一个数据移到最后一个。
            for (int i = 1; i < mAdapter.getDatas().size(); i++) {
                newDatas.add(mAdapter.getDatas().get(i).clone());
            }
            /* 新增数据 */
            int id = new Random().nextInt(1000);
            newDatas.add(new TestBean(id, "赵子龙" + id, "新增", R.drawable.ic_menu_camera));
            /* 修改数据 */
            newDatas.get(1).setDesc("修改");
            newDatas.get(1).setPic(R.drawable.ic_menu_send);
            /* 删除数据 */
            newDatas.remove(newDatas.get(2));
            /* 位移数据 注意:直接位移对象会导致onBindViewHolder实参的position错乱，所以需要viewholder获取position */
            newDatas.add(mAdapter.getDatas().get(0).clone());

            //更新数据
            mAdapter.setDatas(newDatas);

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mBtnRefresh = (Button) findViewById(R.id.btnRefresh);
    }
}
