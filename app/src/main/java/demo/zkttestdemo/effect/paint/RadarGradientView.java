package demo.zkttestdemo.effect.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 雷达
 */
public class RadarGradientView extends View {

    private int mWidth, mHeight;

    //五个圆
    private float[] pots = {0.05f, 0.1f, 0.15f, 0.2f, 0.25f};

    private Shader scanShader; // 扫描渲染shader
    private int scanSpeed = 5; // 扫描速度

    private Paint mPaintCircle; // 画圆用到的paint
    private Paint mPaintRadar; // 扫描用到的paint
    private Matrix matrix;

    public RadarGradientView(Context context) {
        super(context);
        init();
    }

    public RadarGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadarGradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 画圆用到的paint
        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCircle.setStyle(Paint.Style.STROKE); // 描边
        mPaintCircle.setStrokeWidth(1); // 宽度
        mPaintCircle.setAlpha(100); // 透明度
        mPaintCircle.setColor(Color.parseColor("#B0C4DE")); // 设置颜色 亮钢兰色

        // 扫描用到的paint
        mPaintRadar = new Paint();
        mPaintRadar.setStyle(Paint.Style.FILL_AND_STROKE); // 填充
        mPaintRadar.setAntiAlias(true); // 抗锯齿

        // 旋转需要的矩阵
        matrix = new Matrix();
        this.post(run);
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            matrix.postRotate(scanSpeed, mWidth / 2, mHeight / 2); // 旋转矩阵
            invalidate(); // 通知view重绘

            postDelayed(run, 50); // 调用自身 重复绘制
        }
    };

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 取屏幕的宽高是为了把雷达放在屏幕的中间
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mWidth = mHeight = Math.min(mWidth, mHeight);

        scanShader = new SweepGradient(mWidth / 2, mHeight / 2,
                new int[]{Color.TRANSPARENT, Color.parseColor("#84B5CA")}, null);
        mPaintRadar.setShader(scanShader); // 设置着色器
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < pots.length; i++) {
            canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * pots[i], mPaintCircle);
        }

        // 画布的旋转变换 需要调用save() 和 restore()
        canvas.save();
        canvas.concat(matrix);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * pots[4], mPaintRadar);
        canvas.restore();
    }

}
