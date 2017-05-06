package demo.zkttestdemo.effect.transition;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import demo.zkttestdemo.R;

public class TransitionActivity extends AppCompatActivity {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        image = (ImageView) findViewById(R.id.image);

       /* image.setTransitionName("image");
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new ChangeTransform());
        transitionSet.addTransition(new ChangeBounds());
        transitionSet.addTarget("image");
        transitionSet.setDuration(1000);
        Intent intent = new Intent(this, TransitionTwoActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, image, "image");
        startActivity(intent, options.toBundle());*/

       image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //将原先的跳转改成如下方式，注意这里面的第三个参数决定了TransitionTwoActivity 布局中的android:transitionName的值，它们要保持一致
               Intent intent = new Intent(TransitionActivity.this, TransitionTwoActivity.class);
               startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(TransitionActivity.this, image, "shareTransition").toBundle());
           }
       });

    }
}
