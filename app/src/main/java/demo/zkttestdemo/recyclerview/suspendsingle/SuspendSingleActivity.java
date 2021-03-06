package demo.zkttestdemo.recyclerview.suspendsingle;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import demo.zkttestdemo.R;

/**
 * Created by Administrator on 2016/12/29 0029.
 */
public class SuspendSingleActivity extends Activity {

    private RecyclerView recyclerView;
    private RelativeLayout suspensionBar;
    private int mSuspensionHeight;
    private int mCurrentPosition = 0;
    private TextView tv_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspend);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        suspensionBar = (RelativeLayout) findViewById(R.id.suspension_bar);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new SuspendSingleAdapter());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mSuspensionHeight = suspensionBar.getHeight();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View view = linearLayoutManager.findViewByPosition(mCurrentPosition + 1);
                if(view != null){
                    if(view.getTop() <= mSuspensionHeight){
                        suspensionBar.setY(-(mSuspensionHeight - view.getTop()));
                    }else {
                        suspensionBar.setY(0);
                    }
                }

                if (mCurrentPosition != linearLayoutManager.findFirstVisibleItemPosition()) {
                    mCurrentPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    suspensionBar.setY(0);

                    updateSuspensionBar();
                }

            }
        });

        updateSuspensionBar();
    }

    private void updateSuspensionBar() {

        tv_nickname.setText("zkt " + mCurrentPosition);
    }
}
