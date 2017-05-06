package demo.zkttestdemo.recyclerview.singlechoice;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import demo.zkttestdemo.R;

/**
 * Created by Administrator on 2016/12/28 0028.
 */
public class SingleChoiceAdapter extends RecyclerView.Adapter<SingleChoiceAdapter.MyHolder> {
    Activity activity;
    private int choosePos = 0;
    public SingleChoiceAdapter(SingleChoiceActivity singleChoiceActivity) {
        this.activity = singleChoiceActivity;
    }

    @Override
    public SingleChoiceAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(activity).inflate(R.layout.item_single_choice, parent, false));
    }

    @Override
    public void onBindViewHolder(SingleChoiceAdapter.MyHolder holder, final int position) {
        if(choosePos == position){
            holder.radioBtn.setChecked(true);
        }else {
            holder.radioBtn.setChecked(false);
        }

        holder.radioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePos = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final RadioButton radioBtn;

        public MyHolder(View itemView) {
            super(itemView);

            radioBtn = (RadioButton) itemView.findViewById(R.id.radioBtn);
        }
    }
}
