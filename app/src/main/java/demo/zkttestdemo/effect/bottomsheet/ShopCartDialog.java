package demo.zkttestdemo.effect.bottomsheet;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import demo.zkttestdemo.R;


/**
 * Created by cheng on 16-12-22.
 */
public class ShopCartDialog extends Dialog {

    public ShopCartDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_slide_from_bottom);

    }


}
