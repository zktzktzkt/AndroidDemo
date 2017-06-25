package demo.zkttestdemo.effect.bottomsheet;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import demo.zkttestdemo.R;

public class PopUpBottomSheetActivity extends AppCompatActivity implements View.OnClickListener {

    private int height;
    private LinearLayout ll_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet2);

        findViewById(R.id.promptly_buy).setOnClickListener(this);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);

        height = ll_bottom.getLayoutParams().height;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.promptly_buy:
                //                SlideFromBottomPopup slideFromBottomPopup = new SlideFromBottomPopup(PopUpBottomSheetActivity.this);
                //                slideFromBottomPopup.setOffsetY(DisplayUtil.dip2px(80, PopUpBottomSheetActivity.this));
                //                slideFromBottomPopup.showPopupWindow(ll_bottom);

                ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.popup_slide_from_bottom, null);
                PopupWindow pw = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pw.setOutsideTouchable(false); // 设置是否允许在外点击使其消失，到底有用没？
                //  pw.setAnimationStyle(R.style.PopupAnimation); // 设置动画
                pw.showAsDropDown(ll_bottom, 0, 3); // y<3不显示
                pw.update();
                break;
        }
    }


}
