package com.zkt.scratchview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * 刮刮乐，刮奖效果
 */
public class ScratchActivity extends AppCompatActivity {

    private ScratchView scratchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);

        scratchView = findViewById(R.id.scratch_view);
        scratchView.setEraseStatusListener(new ScratchView.EraseStatusListener() {
            @Override
            public void onProgress(int percent) {

            }

            @Override
            public void onCompleted(View view) {
                Toast.makeText(ScratchActivity.this, "画完啦", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void reset(View view) {
        scratchView.reset();
    }

    public void clear(View view) {
        scratchView.clear();
    }
}
