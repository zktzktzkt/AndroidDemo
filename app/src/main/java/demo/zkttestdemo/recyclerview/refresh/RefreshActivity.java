package demo.zkttestdemo.recyclerview.refresh;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;
import demo.zkttestdemo.recyclerview.refresh.bean.Body;

public class RefreshActivity extends AppCompatActivity {

    private MyRecyclerRefreshView mPullRefreshRecyclerView;

    private ArrayList<Body> mBodies;

    private LinearLayoutManager mLayoutManager;
    private MyAdapter mMyAdapter;

    Handler mHandler = new Handler();

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        initData();
        mPullRefreshRecyclerView = (MyRecyclerRefreshView) findViewById(R.id.real_pull_refresh_view);

        mLayoutManager = new LinearLayoutManager(this);
        mMyAdapter = new MyAdapter(this, mBodies);
        mPullRefreshRecyclerView.setLayoutManager(mLayoutManager);
        mPullRefreshRecyclerView.setAdapter(mMyAdapter);

        mPullRefreshRecyclerView.setOnPullListener(new MyRecyclerRefreshView.OnPullListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBodies.add(0, new Body("新数据" + i++, 100));
                        mPullRefreshRecyclerView.refreshFinish();
                    }
                }, 3000);
            }

            @Override
            public void onLoadMore() {
                final List<Body> mMore = new ArrayList<>();

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 3; i++) {
                            mMore.add(new Body("more+++" + i, 100));

                        }


                        mBodies.addAll(mMore);
                        mPullRefreshRecyclerView.loadMroeFinish();
                    }
                }, 1500);
            }


        });
    }

    private void initData() {
        mBodies = new ArrayList<>();
        for (int j = 0; j < 17; j++) {
            mBodies.add(new Body("test" + j * 5, 100));
        }


    }
}
