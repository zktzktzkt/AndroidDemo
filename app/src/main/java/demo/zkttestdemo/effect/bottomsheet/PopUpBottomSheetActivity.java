package demo.zkttestdemo.effect.bottomsheet;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import demo.zkttestdemo.R;
import demo.zkttestdemo.utils.DisplayUtil;

public class PopUpBottomSheetActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_bottom;
    private ShowUpPopupWindow showUpPopupWindow;
    private View view_bg;
    private ViewGroup popView;
    private BottomSheetBehavior behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet2);

        findViewById(R.id.promptly_buy).setOnClickListener(this);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        view_bg = findViewById(R.id.view_bg);

        behavior = BottomSheetBehavior.from(findViewById(R.id.car_container));

        //初始化ppoupwindow
     //   initPopWindow();
    }

    private void initPopWindow() {
        //因为view的高度是在view显示后才测量的，所以必须要手动测量一波，获取宽高
        popView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.popup_slide_from_bottom, null);
        int widSpec = View.MeasureSpec.makeMeasureSpec(DisplayUtil.getScreenWidth(getApplicationContext()), View.MeasureSpec.EXACTLY);
        popView.measure(widSpec, View.MeasureSpec.UNSPECIFIED);
        showUpPopupWindow = new ShowUpPopupWindow(popView, popView.getMeasuredWidth(), popView.getMeasuredHeight());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.promptly_buy:
             /*   PopupWindow pw = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pw.setOutsideTouchable(false); // 设置是否允许在外点击使其消失，到底有用没？
                //  pw.setAnimationStyle(R.style.PopupAnimation); // 设置动画
                //  pw.showAtLocation(rl_root, Gravity.BOTTOM, 0, 200);//不好使，始终在最底部显示
                // pw.showAsDropDown(ll_bottom, 0, 3); // y<3不显示
                pw.update();*/
                // TODO: 2017-9-23 下面这个是好使的
                /*if (showUpPopupWindow.isShowing()) {
                    showUpPopupWindow.dismiss();
                    view_bg.setVisibility(View.GONE);
                } else {
                    showUpPopupWindow.showAsPullUp(ll_bottom, popView.getMeasuredHeight(), 0, 0);
                    view_bg.setVisibility(View.VISIBLE);
                }
*/
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }

                break;
        }
    }

    public void onDarkBg(View view) {
        view_bg.setVisibility(View.GONE);
        showUpPopupWindow.dismiss();
    }

    public void onTest1Click(View view) {
        Toast.makeText(this, "test1点击", Toast.LENGTH_SHORT).show();
    }
}
