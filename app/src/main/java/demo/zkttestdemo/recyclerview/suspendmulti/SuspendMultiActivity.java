package demo.zkttestdemo.recyclerview.suspendmulti;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.zkttestdemo.R;

/**
 * Created by Administrator on 2016/12/29 0029.
 */
public class SuspendMultiActivity extends Activity{

    private RelativeLayout mSuspensionBar;
    private TextView mSuspensionTv;
    private int mCurrentPosition = 0;

    private int mSuspensionHeight;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspend_multi);

        mSuspensionBar = (RelativeLayout) findViewById(R.id.suspension_bar);
        mSuspensionTv = (TextView) findViewById(R.id.tv_time);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final SuspendMultiAdapter suspendMultiAdapter = new SuspendMultiAdapter(this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(suspendMultiAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mSuspensionHeight = mSuspensionBar.getHeight();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (suspendMultiAdapter.getItemViewType(mCurrentPosition + 1) == SuspendMultiAdapter.TYPE_SUSPEND_HEAD) {
                    View view = linearLayoutManager.findViewByPosition(mCurrentPosition + 1);
                    if (view != null) {
                        if (view.getTop() <= mSuspensionHeight) {
                            mSuspensionBar.setY(-(mSuspensionHeight - view.getTop()));
                        } else {
                            mSuspensionBar.setY(0);
                        }
                    }
                }

                if (mCurrentPosition != linearLayoutManager.findFirstVisibleItemPosition()) {
                    mCurrentPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    mSuspensionBar.setY(0);

                    updateSuspensionBar();
                }
            }
        });

        updateSuspensionBar();

    }

    private void updateSuspensionBar() {
        mSuspensionTv.setText("zktzktzkt " + (1 + mCurrentPosition / 4));
    }

}
