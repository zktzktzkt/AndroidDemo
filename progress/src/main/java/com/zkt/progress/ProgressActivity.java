package com.zkt.progress;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        TriangleProgressView triangleProgress = findViewById(R.id.triangleProgress);
        triangleProgress.setProgress(150, 1000);
    }
}
