package demo.zkttestdemo.effect.transition;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import demo.zkttestdemo.R;

public class TransitionTwoActivity extends AppCompatActivity {
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 允许使用transitions,共享元素用不到，比如Activity的跳转动画需要；但一定要放在setcontentview之前
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_transition_two);

        image = (ImageView) findViewById(R.id.image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将原先的跳转改成如下方式，注意这里面的第三个参数决定了TransitionActivity 布局中的android:transitionName的值，它们要保持一致
                Intent intent = new Intent(TransitionTwoActivity.this, TransitionActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(TransitionTwoActivity.this, image, "shareTransition").toBundle());
            }
        });

    }
}
