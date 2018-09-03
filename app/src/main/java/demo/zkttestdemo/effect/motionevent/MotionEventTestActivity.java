package demo.zkttestdemo.effect.motionevent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import demo.zkttestdemo.R;

public class MotionEventTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_event_test);

        //总结
        //1、如果View的clickable = true，则该View和它所在的ViewGroup的dispatch，onIntercept, onTouch的down move up都会执行

        //2、如果View的clickable = false，则该View和它所在的ViewGroup只会执行down；并且down会一层层往上回传
    }
}
