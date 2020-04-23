package com.zkt.mylibrary;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ypzn.basemodule.ARouterManager;
import com.zkt.mylibrary.bean.QQMessage;

import java.util.List;

@Route(path = ARouterManager.ItemTouchHelperActivity)
public class ItemTouchHelperActivity extends AppCompatActivity implements StartDragListener {

    private RecyclerView recyclerview;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_touch_helper);

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);

        List<QQMessage> list = DataUtils.init();

        MessageAdapter adapter = new MessageAdapter(list, this);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new MessageItemTouchCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerview);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
