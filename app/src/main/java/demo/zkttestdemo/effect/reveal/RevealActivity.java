package demo.zkttestdemo.effect.reveal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ypzn.revealview.RevealDrawable;

import demo.zkttestdemo.R;

public class RevealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal);

        RevealDrawable drawable = new RevealDrawable(
                getResources().getDrawable(R.drawable.avft),
                getResources().getDrawable(R.drawable.avft_active)
        );

        findViewById(R.id.iv).setBackground(drawable);

    }
}
