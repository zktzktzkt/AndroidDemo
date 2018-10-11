package demo.zkttestdemo.recyclerview.nested;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import demo.zkttestdemo.R;

/**
 * 嵌套滑动
 */
public class NestedRecyclerActivity extends AppCompatActivity {

    MyRecyclerView rvNormal;
    RecyclerView rvBetter;
    MyRecyclerView rvFeedRoot;
    SectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_recycler);
        setUpDrawer();

        adapter = new SectionAdapter();
        rvNormal = (MyRecyclerView) findViewById(R.id.rv_normal);
        //        rvBetter = findViewById(R.id.rv_better);
        //        rvFeedRoot = (MyRecyclerView) findViewById(R.id.rv_feed);

        rvNormal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //        rvBetter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //        rvFeedRoot.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rvNormal.setAdapter(adapter);
        //        rvBetter.setAdapter(adapter);
        //        rvFeedRoot.setAdapter(adapter);
    }

    private void setUpDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.consider_angle, R.string.ignore_child_requests);
        drawerToggle.syncState();

    }
}
