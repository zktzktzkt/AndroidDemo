package demo.zkttestdemo.effect.qqslidemenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import demo.zkttestdemo.R;

public class QQSlideMenuActivity extends AppCompatActivity {

    private QQSlideMenu sliding_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqslide_menu);

        sliding_menu = (QQSlideMenu) findViewById(R.id.sliding_menu);

        findViewById(R.id.iv_avator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭菜单 切换
                sliding_menu.toggleMenu();
            }
        });
    }
}
