package demo.zkttestdemo.effect.viewdraghelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import demo.zkttestdemo.R;

public class ViewDragHelperDemoActivity extends AppCompatActivity {
    private VDHLinearLayout mVDHLiearLayout;
    private TextView mDragView;
    private TextView mAutobackView;
    private TextView mEdgeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drag_helper_demo);

        mDragView = (TextView) findViewById(R.id.child1);
        mAutobackView = (TextView) findViewById(R.id.child2);
        mEdgeView = (TextView) findViewById(R.id.child3);
        mVDHLiearLayout = (VDHLinearLayout) findViewById(R.id.root_vdh);
        init();
    }

    private void init() {
    }
}
