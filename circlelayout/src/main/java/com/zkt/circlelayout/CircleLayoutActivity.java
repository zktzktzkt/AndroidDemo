package com.zkt.circlelayout;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ypzn.basemodule.ARouterManager;

@Route(path = ARouterManager.CircleLayoutActivity)
public class CircleLayoutActivity extends AppCompatActivity {

    private CircleLayout circleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_layout);

        circleLayout = findViewById(R.id.circleLayout);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void addView(View view) {
        CircleView circleView = new CircleView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dp2px(50), dp2px(50));
        circleView.setLayoutParams(params);
        circleLayout.addView(circleView);
    }

    public void clear(View view) {
        circleLayout.removeAllViews();
    }
}
