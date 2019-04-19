package com.zkt.address;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.zkt.progress.R;

import java.util.List;

/**
 * Created by zkt on 19/04/15.
 * Description:
 */
public class AddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        final List<Women> womens = WomenList.getWomen();

        RecWomenAdapter adapter = new RecWomenAdapter(this);
        adapter.addDatas(womens);


        final GridDecoration decoration = new GridDecoration(womens.size(), 3) {
            @Override
            public String getHeaderName(int pos) {
                return womens.get(pos).getName();
            }
        };

        decoration.setOnHeaderClickListener(new NormalDecoration.OnHeaderClickListener() {
            @Override
            public void headerClick(int pos) {

                Toast.makeText(AddressActivity.this, "点击到头部" + pos, Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView recycler = findViewById(R.id.recycler);

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recycler.addItemDecoration(decoration);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);

    }

}
