package demo.zkttestdemo.effect.snow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import demo.zkttestdemo.R;

/**
 * 下雪效果
 */
public class SnowActivity extends AppCompatActivity {

    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private SnowView snowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snow);

        snowView = findViewById(R.id.snowView);

        //绘制雪球bitmap
        Paint snowPaint = new Paint();
        snowPaint.setColor(Color.WHITE);
        snowPaint.setStyle(Paint.Style.FILL);
        bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmapCanvas.drawCircle(25, 25, 25, snowPaint);

        //先初始化SnowObject
        SnowObject snowObject = new SnowObject.Builder(ContextCompat.getDrawable(this, R.drawable.ic_snow))
                .setSpeed(10, true)
                .setSize(50, 50, true)
                .build();

        snowView.addFallObject(snowObject, 50);//添加50个雪球对象
    }   
}
