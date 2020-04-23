package demo.zkttestdemo.recyclerview.multichoice;

import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;

/**
 * Created by Administrator on 2016/12/28 0028.
 */

public class MultiChoiceActivity extends Activity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_choice);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        List<MultiChoiceBean> beanArrayList = new ArrayList<>();
        for(int i=0; i<20; i++){
            MultiChoiceBean multiChoiceBean = new MultiChoiceBean();
            beanArrayList.add(multiChoiceBean);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MultiChoiceAdapter(this, beanArrayList));
        //取消item的动画，防止item刷新产生闪烁
        ((DefaultItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }
}
