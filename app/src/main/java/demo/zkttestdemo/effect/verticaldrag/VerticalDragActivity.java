package demo.zkttestdemo.effect.verticaldrag;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import demo.zkttestdemo.R;

/**
 * 仿汽车之家
 */
public class VerticalDragActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<String> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_drag);

        mListView = findViewById(R.id.list_view);
        mItems = new ArrayList<String>();

        for (int i = 0; i < 200; i++) {
            mItems.add("i -》 " + i);
        }

        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mItems.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView item = (TextView) (convertView = LayoutInflater.from(VerticalDragActivity.this).inflate(R.layout.item, parent, false));
                item.setText(mItems.get(position));
                return convertView;
            }
        });
    }
}
