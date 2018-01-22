package demo.zkttestdemo.effect.paykeyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2017/5/28.
 * 自定义输入框
 */
public class PasswordEdittext extends EditText {
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 一个密码所占的宽度
     */
    private int mPasswordItemWidth;
    /**
     * 密码的个数默认为6位数
     */
    private int mPasswordNumber = 6;
    /**
     * 背景边框颜色
     */
    private int mBgColor = Color.parseColor("#d1d2d6");
    /**
     * 背景边框大小
     */
    private int mBgSize = 1;
    /**
     * 背景边框圆角大小
     */
    private int mBgCorner = 0;
    /**
     * 分割线颜色
     */
    private int mDivisionLineColor = mBgColor;
    /**
     * 分割线的大小
     */
    private int mDivisionLineSize = 1;
    /**
     * 密码圆点的颜色
     */
    private int mPasswordColor = mDivisionLineColor;
    /**
     * 密码圆点的半径大小
     */
    private int mPasswordRadius = 4;

    public PasswordEdittext(Context context) {
        this(context, null);
    }

    public PasswordEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);

        initAttributeSet(context, attrs);
        initPaint();

        //默认只能设置数字和字母
        setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setDither(true); //防抖动
    }

    /**
     * 初始化属性
     */
    private void initAttributeSet(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PasswordEdittext);
        //获取大小
        mDivisionLineSize = (int) array.getDimension(R.styleable.PasswordEdittext_divisionLineSize, dip2px(mDivisionLineSize));
        mPasswordRadius = (int) array.getDimension(R.styleable.PasswordEdittext_passwordRadius, dip2px(mPasswordRadius));
        mBgSize = (int) array.getDimension(R.styleable.PasswordEdittext_bgSize, dip2px(mBgSize));
        mBgCorner = (int) array.getDimension(R.styleable.PasswordEdittext_bgCorner, 0);
        //获取颜色
        mBgColor = array.getColor(R.styleable.PasswordEdittext_bgColor, mBgColor);
        mDivisionLineColor = array.getColor(R.styleable.PasswordEdittext_divisionLineColor, mDivisionLineColor);
        mPasswordColor = array.getColor(R.styleable.PasswordEdittext_passwordColor, mPasswordColor);

        array.recycle();
    }

    /**
     * dp转px
     */
    private float dip2px(int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas); 原来的editext效果不需要，所以不需要调super

        //一个密码的宽度 = （总长度 - 两边边缘的线宽 - 中间分割线的宽度）/ 密码的数量
        mPasswordItemWidth = (getWidth() - 2 * mBgSize - (mPasswordNumber - 1) * mDivisionLineSize) / mPasswordNumber;
        //画背景
        drawBg(canvas);
        //画分割线
        drawDivisionLine(canvas);
        //画密码
        drawPassword(canvas);

        //当前密码是不是满了
        if (mListener != null) {
            String password = getText().toString().trim();
            if (password.length() >= mPasswordNumber) {
                mListener.passwordFull(password);
            }
        }
    }

    /**
     * 绘制密码
     */
    private void drawPassword(Canvas canvas) {
        //设置密码的颜色
        mPaint.setColor(mPasswordColor);
        //密码绘制是实心
        mPaint.setStyle(Paint.Style.FILL);
        //获取当前text
        String text = getText().toString().trim();
        //获取密码长度
        int passwordLength = text.length();
        //不断绘制密码
        for (int i = 0; i < passwordLength; i++) {
            int cx = mBgSize + i * mPasswordItemWidth + i * mDivisionLineSize + mPasswordItemWidth / 2;
            int cy = getHeight() / 2;
            canvas.drawCircle(cx, cy, mPasswordRadius, mPaint);
        }

    }

    /**
     * 绘制分割线
     */
    private void drawDivisionLine(Canvas canvas) {
        mPaint.setStrokeWidth(mDivisionLineSize);
        mPaint.setColor(mDivisionLineColor);

        for (int i = 0; i < mPasswordNumber - 1; i++) {
            //边缘线的宽度 + 单个密码的宽度 + 分割线的宽度
            int startX = mBgSize + (i + 1) * mPasswordItemWidth + i * mDivisionLineSize;
            int startY = mBgSize;
            int endX = startX;
            int endY = getHeight() - mBgSize;

            canvas.drawLine(startX, startY, endX, endY, mPaint);
        }
    }

    /**
     * 绘制背景
     */
    private void drawBg(Canvas canvas) {
        RectF rect = new RectF(mBgSize, mBgSize, getWidth() - mBgSize, getHeight() - mBgSize);

        //设置背景的颜色
        mPaint.setColor(mBgColor);
        //设置画笔的大小
        mPaint.setStrokeWidth(mBgSize);
        //画空心
        mPaint.setStyle(Paint.Style.STROKE);

        //绘制背景，drawRect，drawRoundRect
        // 如果有圆角就用drawRoundRect，否则用drawRect
        if (mBgCorner == 0) {
            canvas.drawRect(rect, mPaint);
        } else {
            canvas.drawRoundRect(rect, mBgCorner, mBgCorner, mPaint);
        }
    }

    /**
     * 添加一个密码
     */
    public void addPassword(String number) {
        //把之前的密码取出来
        String password = getText().toString().trim();
        if (password.length() >= mPasswordNumber) {
            return;
        }

        password += number;
        setText(password);
    }

    /**
     * 删除最后一位密码
     */
    public void deleteLastPassword() {
        String password = getText().toString().trim();
        //判断当前密码是不是空
        if (TextUtils.isEmpty(password)) {
            return;
        }

        password = password.substring(0, password.length() - 1);
        setText(password);
    }

    private PasswordFullListener mListener;

    /**
     * 设置当前密码是否已经填满
     */
    public void setOnPasswordFullListener(PasswordFullListener listener) {
        this.mListener = listener;
    }

    /**
     * 密码已经全部填满
     */
    public interface PasswordFullListener {
        void passwordFull(String password);
    }
}
