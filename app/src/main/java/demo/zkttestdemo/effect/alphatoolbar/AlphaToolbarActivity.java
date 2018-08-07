package demo.zkttestdemo.effect.alphatoolbar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import demo.zkttestdemo.R;
import demo.zkttestdemo.effect.statusbar.StatusBarUtil;
import demo.zkttestdemo.utils.BitmapCompressor;
import demo.zkttestdemo.utils.DensityUtil;

/**
 * Created by Administrator on 2016/12/31 0031.
 */

public class AlphaToolbarActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alpha_toolbar);

        StatusBarUtil.setActivityTranslucent(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView image_sky = findViewById(R.id.image_sky);
        AlphaToolbarScrollView alphaScroll = findViewById(R.id.alphaScroll);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        Bitmap bitmap = BitmapCompressor.decodeSampledBitmapFromResource(getResources(), R.drawable.sky, w_screen, DensityUtil.dip2px(this, 300));
        image_sky.setImageBitmap(bitmap);
        toolbar.getBackground().setAlpha(0);

        alphaScroll.setTitleAndHead(toolbar, image_sky, true);
    }
}
