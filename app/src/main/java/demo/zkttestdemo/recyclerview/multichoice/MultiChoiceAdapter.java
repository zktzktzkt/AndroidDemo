package demo.zkttestdemo.recyclerview.multichoice;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.List;

import demo.zkttestdemo.R;

/**
 * 多选 - 操作的是数据
 * Created by Administrator on 2016/12/28 0028.
 */
public class MultiChoiceAdapter extends RecyclerView.Adapter<MultiChoiceAdapter.MyHolder> {
    Activity activity;
    List<MultiChoiceBean> beanArrayList;

    public MultiChoiceAdapter(MultiChoiceActivity multiChoiceActivity, List<MultiChoiceBean> beanArrayList) {
        this.activity = multiChoiceActivity;
        this.beanArrayList = beanArrayList;
    }

    @Override
    public MultiChoiceAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MultiChoiceAdapter.MyHolder(LayoutInflater.from(activity).inflate(R.layout.item_single_choice, parent, false));
    }

    @Override
    public void onBindViewHolder(MultiChoiceAdapter.MyHolder holder, final int position) {
        if (beanArrayList.get(position).getIsSelect()) {
            holder.radioBtn.setChecked(true);
        }else {
            holder.radioBtn.setChecked(false);
        }

        holder.radioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSelect = beanArrayList.get(position).getIsSelect();
                beanArrayList.get(position).setIsSelect(!isSelect);
               /* if( beanArrayList.get(position).getIsSelect()){
                    beanArrayList.get(position).setIsSelect(false);
                }else {
                    beanArrayList.get(position).setIsSelect(true);
                }*/
               notifyItemRangeChanged(position,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beanArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private final RadioButton radioBtn;

        public MyHolder(View itemView) {
            super(itemView);

            radioBtn = (RadioButton) itemView.findViewById(R.id.radioBtn);
        }
    }
}