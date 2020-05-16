package demo.zkttestdemo.effect.compass;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import demo.zkttestdemo.R;

public class CompassTestActivity extends Activity implements
        SensorEventListener {
    // 定义显示指南针图片的组件
    private ImageView     image;
    // 记录指南针图片转过的角度
    private float         currentDegree = 0f;
    // 定义真机的Sensor管理器
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        image = (ImageView) findViewById(R.id.main_iv);
        // 获取真机的传感器管理服务
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 为系统的方向传感器注册监听器
        mSensorManager.registerListener(this,
                                        mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                                        SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 取消注册
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 如果真机上触发event的传感器类型为水平传感器类型
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float degree = event.values[0]; // 获取绕Z轴转过的角度
			Log.e("onSensorChanged", "degree -> " + degree);
			image.setRotation(-degree);
            currentDegree = -degree;
        }
    }
}