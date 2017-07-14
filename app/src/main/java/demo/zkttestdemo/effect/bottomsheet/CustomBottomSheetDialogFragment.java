package demo.zkttestdemo.effect.bottomsheet;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2017/6/23.
 */

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.include_bottom_sheet_layout, container, false);
        return v;
    }


}
