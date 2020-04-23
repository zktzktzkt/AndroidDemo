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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diff_activity);
        initView();

        initData();

        mRv = ((RecyclerView) findViewById(R.id.diff_rv));
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DiffAdapter(this, mDatas);

        mRv.setAdapter(mAdapter);
        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
    }

    private void initData() {
        mDatas = new ArrayList<>();
        mDatas.add(new TestBean("张旭童1", "Android", R.drawable.ic_launcher));
        mDatas.add(new TestBean("张旭童2", "Java", R.drawable.ic_launcher));
        mDatas.add(new TestBean("张旭童3", "背锅", R.drawable.ic_launcher));
        mDatas.add(new TestBean("张旭童4", "手撕产品", R.drawable.ic_launcher));
        mDatas.add(new TestBean("张旭童5", "手撕测试", R.drawable.ic_launcher));
    }

    /**
     * 模拟刷新操作
     */
    public void onRefresh() {
        try {
            List<TestBean> newDatas = new ArrayList<>();
            for (TestBean bean : mDatas) {
                newDatas.add(bean.clone());//clone一遍旧数据 ，模拟刷新操作
            }
            newDatas.add(new TestBean("赵子龙", "帅", R.drawable.ic_menu_camera));//模拟新增数据
            newDatas.get(0).setDesc("Android+");
            newDatas.get(0).setPic(R.drawable.ic_menu_send);//模拟修改数据
            TestBean testBean = newDatas.get(1);//模拟数据位移
            newDatas.remove(testBean);
            newDatas.add(testBean);
            //别忘了将新数据给Adapter
//            mDatas = newDatas;
//            mAdapter.setDatas(mDatas);
//            mAdapter.notifyDataSetChanged();//以前我们大多数情况下只能这样
            notifyData(newDatas);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void notifyData(List<TestBean> newDatas) {
        //文艺青年新宠
        //利用DiffUtil.calculateDiff()方法，传入一个规则DiffUtil.Callback对象，和是否检测移动item的 boolean变量，得到DiffUtil.DiffResult 的对象
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(mDatas, newDatas), true);

        //利用DiffUtil.DiffResult对象的dispatchUpdatesTo（）方法，传入RecyclerView的Adapter，轻松成为文艺青年
        diffResult.dispatchUpdatesTo(mAdapter);

        //别忘了将新数据给Adapter
        mDatas = newDatas;
        mAdapter.setDatas(mDatas);
    }

    private void initView() {
        mBtnRefresh = (Button) findViewById(R.id.btnRefresh);
    }
}
