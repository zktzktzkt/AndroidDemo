package demo.zkttestdemo.effect.loadingdialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import demo.zkttestdemo.R;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class CustomProgressDialog extends ProgressDialog {
    private ImageView loading_iv;
    private TextView loading_tv;
    AnimationDrawable animationDrawable;
    private int  resId;  //资源id
    private String tips;  //需要显示的文本信息


    public CustomProgressDialog(Context context, String tips, int resId) {
        super(context);

        this.tips = tips;
        this.resId = resId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog);
        //设置透明背景
        getWindow().setBackgroundDrawable(new ColorDrawable());

        loading_iv = (ImageView) findViewById(R.id.loading_iv);
        loading_tv = (TextView) findViewById(R.id.loading_tv);

        initData();

        setCanceledOnTouchOutside(true);
    }

    private void initData() {
        loading_tv.setText(tips);
        loading_iv.setBackgroundResource(resId);
        animationDrawable = (AnimationDrawable) loading_iv.getBackground();

        loading_iv.post(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }
        });
    }
}
