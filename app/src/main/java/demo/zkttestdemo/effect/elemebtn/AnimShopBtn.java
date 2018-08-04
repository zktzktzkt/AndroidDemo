package demo.zkttestdemo.effect.elemebtn;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by zkt on 2018-3-31.
 */

public class AnimShopBtn extends View {

    //提示语 收缩动画(0-1) 展开(1-0) 注：0-1和1-0是收缩的比例
    //普通模式时(count > 0)，应该是1
    protected float mAnimExpandHintFraction;
    private ValueAnimator mAnimReduce; //收缩动画
    private ValueAnimator mAnimExpand; //展开动画

    protected float mAnimFraction;
    private ValueAnimator mAnimDel; //减动画
    private ValueAnimator mAnimAdd; //加动画

    //View的宽高
    private int mWidth, mHeight;
    //View的左上基准点
    private int mLeft, mTop;
    private float mRadius;

    //是否绘制count=0时的，hint区域
    private boolean isHintMode;
    private Paint mHintPaint;
    private int mHintBgColor;
    private int mHintTextColor;
    private int mHintTextSize;
    private float mHintRoundValue;
    private String mHintText;

    protected int mCount;
    protected int mMaxCount;
    private float mStrokeWidth;
    private float mCircleLineWidth;

    //减区域
    private Paint mDelPaint;
    private Path mDelPath;
    private Region mDelRegion;

    //加区域
    private Paint mAddPaint;
    private Path mAddPath;
    private Region mAddRegion;

    //文字区域
    private Paint mTextPaint;
    private int mTextColor;
    private int mTextSize;

    //两圆之间的距离
    private int mGapBetweenCircle;

    public AnimShopBtn(Context context) {
        this(context, null);
    }

    public AnimShopBtn(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimShopBtn(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
        initAnim();
    }

    public int getCount() {
        return mCount;
    }

    public AnimShopBtn setCount(int mCount) {
        this.mCount = mCount;
        cancelAllAnim();
        initSettings();
        return this;
    }

    /**
     * 根据当前的count设置合适的属性值
     */
    private void initSettings() {
        if (mCount == 0) {
            //不显示数字和-号
            mAnimFraction = 1;
            isHintMode = true;
            mAnimExpandHintFraction = 0;
        } else {
            mAnimFraction = 0;
            isHintMode = false;
            mAnimExpandHintFraction = 1;
        }
    }

    private void cancelAllAnim() {
        if (null != mAnimReduce && mAnimReduce.isRunning()) {
            mAnimReduce.cancel();
        }
        if (null != mAnimExpand && mAnimExpand.isRunning()) {
            mAnimExpand.cancel();
        }
        if (null != mAnimDel && mAnimDel.isRunning()) {
            mAnimDel.cancel();
        }
        if (null != mAnimAdd && mAnimAdd.isRunning()) {
            mAnimAdd.cancel();
        }
    }

    public int getMaxCount() {
        return mMaxCount;
    }

    public void setMaxCount(int mMaxCount) {
        this.mMaxCount = mMaxCount;
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        //hint收缩
        mAnimReduce = ValueAnimator.ofFloat(0, 1);
        mAnimReduce.setDuration(500);
        mAnimReduce.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimExpandHintFraction = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimReduce.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isHintMode = false;
                mAnimAdd.start();
                invalidate();
            }
        });

        //hint扩展
        mAnimExpand = ValueAnimator.ofFloat(1, 0);
        mAnimExpand.setDuration(500);
        mAnimExpand.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimExpandHintFraction = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimExpand.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isHintMode = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimAdd.start();
            }
        });

        //减按钮的动画
        mAnimDel = ValueAnimator.ofFloat(0, 1);
        mAnimDel.setDuration(500);
        mAnimDel.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimFraction = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimDel.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimExpand.start();
            }
        });

        //加按钮的动画
        mAnimAdd = ValueAnimator.ofFloat(1, 0);
        mAnimAdd.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimFraction = (float) animation.getAnimatedValue();
                invalidate();
            }
        });


    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextColor = Color.GREEN;
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, getContext().getResources().getDisplayMetrics());
        mTextPaint.setTextSize(mTextSize);

        mDelPath = new Path();
        mDelRegion = new Region();
        mDelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //isDelFillMode = false
        mDelPaint.setStyle(Paint.Style.STROKE);

        mAddPath = new Path();
        mAddRegion = new Region();
        mAddPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //isDelFillMode = true
        mAddPaint.setStyle(Paint.Style.FILL);

        mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getContext().getResources().getDisplayMetrics());
        mStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getContext().getResources().getDisplayMetrics());
        mGapBetweenCircle = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getContext().getResources().getDisplayMetrics());

        mCount = 1;
        isHintMode = false;
        mMaxCount = 6;

        mHintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHintBgColor = Color.BLUE;
        mHintTextColor = Color.WHITE;
        mHintTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, getContext().getResources().getDisplayMetrics());
        mHintRoundValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getContext().getResources().getDisplayMetrics());
        mHintText = "该睡了";
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);

        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (wMode) {
            case MeasureSpec.AT_MOST:
                wSize = (int) (getPaddingLeft() + mRadius * 2 + mGapBetweenCircle + mRadius * 2 + getPaddingRight() + mStrokeWidth);
                break;
        }

        switch (hMode) {
            case MeasureSpec.AT_MOST:
                hSize = (int) (getPaddingTop() + mRadius * 2 + getPaddingBottom() + mStrokeWidth * 2);
                break;
        }

        setMeasuredDimension(wSize, hSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
        mLeft = (int) (getPaddingLeft() + mStrokeWidth);
        mTop = (int) (getPaddingTop() + mStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isHintMode) {
            //hint展开
            //背景
            mHintPaint.setColor(mHintBgColor);
            RectF rectF = new RectF(mLeft + (mWidth - mRadius * 2) * mAnimExpandHintFraction, mTop,
                    mWidth - mStrokeWidth, mHeight - mStrokeWidth);
            canvas.drawRoundRect(rectF, mHintRoundValue, mHintRoundValue, mHintPaint);

            //绘制前景文字
            mHintPaint.setColor(mHintTextColor);
            mHintPaint.setTextSize(mHintTextSize);
            //计算BaseLine绘制起点的 X坐标，Y坐标  (文字绘制到中心的套路代码)
            int baseX = (int) (mWidth / 2 - mHintPaint.measureText(mHintText) / 2);
            int baseY = (int) (mHeight / 2 - (mHintPaint.descent() + mHintPaint.ascent()) / 2);
            canvas.drawText(mHintText, baseX, baseY, mHintPaint);

        } else {

            // - 的背景
            if (mCount > 0) {
                mDelPaint.setColor(Color.GREEN);
            } else {
                mDelPaint.setColor(Color.GREEN);
            }

            //动画的基准值 动画：减 0~1, 加 1~0
            //动画位移的Max
            float animOffsetMax = mRadius * 2 + mGapBetweenCircle;
            //透明度动画的基准值
            int animAlphaMax = 255;
            //旋转
            int animRotateMax = 360;
            mDelPaint.setAlpha((int) (animAlphaMax * (1 - mAnimFraction)));
            mDelPaint.setStrokeWidth(mStrokeWidth);
            //考虑动画 硬件加速(API 19)
            mDelPath.reset();
            mDelPath.addCircle(animOffsetMax * mAnimFraction + mLeft + mRadius, mTop + mRadius, mRadius, Path.Direction.CW);
            mDelRegion.setPath(mDelPath, new Region(mLeft, mTop,
                    getWidth() - getPaddingRight(), getHeight() - getPaddingBottom()));
            canvas.drawPath(mDelPath, mDelPaint);

            // - 前景
            if (mCount > 0) {
                mDelPaint.setColor(Color.GREEN);
            } else {
                mDelPaint.setColor(Color.GREEN);
            }

            canvas.save(); //保存之前的canvas状态（坐标系），以便于接下来的操作不影响之前的canvas内容
            canvas.translate(animOffsetMax * mAnimFraction + mLeft + mRadius, mTop + mRadius);
            canvas.rotate(animRotateMax * (1 - mAnimFraction));
            mCircleLineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getContext().getResources().getDisplayMetrics());
            mDelPaint.setStrokeWidth(mCircleLineWidth);
            canvas.drawLine(-mRadius / 2, 0, mRadius / 2, 0, mDelPaint);
            canvas.restore(); //恢复上一次保存的canvas的状态（坐标系）

            //数量
            canvas.save();
            canvas.translate(mAnimFraction * (mGapBetweenCircle / 2 - mTextPaint.measureText(mCount + "") / 2 + mRadius), 0);
            canvas.rotate(animRotateMax * mAnimFraction, mLeft + mRadius * 2 + mGapBetweenCircle / 2, mTop + mRadius);
            mTextPaint.setAlpha((int) (animAlphaMax * (1 - mAnimFraction)));
            canvas.drawText(mCount + "", mLeft + mRadius * 2 + mGapBetweenCircle / 2 - mTextPaint.measureText(mCount + "") / 2
                    , mTop + mRadius - (mTextPaint.descent() + mTextPaint.ascent()) / 2, mTextPaint);
            canvas.restore();


            //右边的圆
            //背景圆圈
            if (mCount < mMaxCount) {
                mAddPaint.setColor(Color.BLUE);
            } else {
                mAddPaint.setColor(Color.BLACK);
            }
            mAddPaint.setStrokeWidth(mStrokeWidth);
            //右边圆的左边界
            float left = mLeft + mRadius * 2 + mGapBetweenCircle;
            mAddPath.reset();
            mAddPath.addCircle(left + mRadius, mTop + mRadius, mRadius, Path.Direction.CCW);
            mAddRegion.setPath(mAddPath, new Region(mLeft, mTop,
                    getWidth() - getPaddingRight(), getHeight() - getPaddingBottom()));
            canvas.drawPath(mAddPath, mAddPaint);

            //数量 +
            if (mCount < mMaxCount) {
                mAddPaint.setColor(Color.WHITE);
            } else {
                mAddPaint.setColor(Color.BLACK);
            }
            mAddPaint.setStrokeWidth(mCircleLineWidth);
            canvas.drawLine(left + mRadius / 2, mTop + mRadius,
                    left + mRadius / 2 + mRadius, mTop + mRadius, mAddPaint);
            canvas.drawLine(left + mRadius, mTop + mRadius / 2,
                    left + mRadius, mTop + mRadius + mRadius / 2, mAddPaint);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isHintMode) {
                    onAddClick();
                } else {
                    // ▲ 通过Region.contains(x,y)判断 触摸的落点是否在对应的Region区域
                    if (mAddRegion.contains((int) event.getX(), (int) event.getY())) {
                        onAddClick();
                    } else if (mDelRegion.contains((int) event.getX(), (int) event.getY())) {
                        onDelClick();
                    }
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 当 - 事件触发 回调
     */
    protected void onDelClick() {
        if (mCount > 0) {
            mCount--;
            onDelSuccessListener();
            invalidate();
        } else {
            Toast.makeText(getContext(), "购车车商品数量为0", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 当 + 事件触发 回调
     */
    protected void onAddClick() {
        if (mCount < mMaxCount) {
            mCount++;
            onAddSuccessListener();
            invalidate();
        } else {
            Toast.makeText(getContext(), "不能继续加了！", Toast.LENGTH_SHORT).show();
        }
    }

    //当 + 触发成功了
    protected void onAddSuccessListener() {
        if (mCount == 1) {
            cancelAllAnim();
            mAnimReduce.start();
        }
    }

    protected void onDelSuccessListener() {
        if (mCount == 0) {
            cancelAllAnim();
            mAnimDel.start();
        }
    }
}
