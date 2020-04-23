package demo.zkttestdemo.effect.doodle;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import demo.zkttestdemo.R;

/**
 * 涂鸦板、签名板
 */
public class DoodleActivity extends AppCompatActivity {

    private TextView tv_path;
    private DoodleView doodleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doodle);

        doodleView = (DoodleView) findViewById(R.id.doodleView);
        tv_path = (TextView) findViewById(R.id.tv_path);

    }

    public void saveClick(View view) {
        tv_path.setText(doodleView.saveFile("doodle"));
    }

    public void clearClick(View view) {
        doodleView.clearDraw();
    }
}
