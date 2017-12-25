package demo.zkttestdemo.effect.favor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import demo.zkttestdemo.R;

/**
 * 点赞飘心效果
 */
public class FavorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favor);

        final FavorLayout favor = findViewById(R.id.favor);

        favor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favor.addHeart();
            }
        });
    }
}
