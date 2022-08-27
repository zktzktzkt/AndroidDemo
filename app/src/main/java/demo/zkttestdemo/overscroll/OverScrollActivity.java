package demo.zkttestdemo.overscroll;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import demo.zkttestdemo.R;
import demo.zkttestdemo.recyclerview.suspendsingle.SuspendSingleAdapter;

/**
 * Created by Administrator on 2016/12/29 0029.
 */
public class OverScrollActivity extends Activity {

    private OverScrollLayout overscroll;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_scroll);

        overscroll = (OverScrollLayout) findViewById(R.id.overscroll);
        overscroll.setOrientation(OverScrollLayout.ScrollOrientation.HORIZONTAL);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new SuspendSingleAdapter());
    }

}
