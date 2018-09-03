package com.ypzn.bubble;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PointFEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

public class DragBubbleView extends View {

    /**
     * 气泡默认状态--静止
     */
    private final int BUBBLE_STATE_DEFAULT = 0;
    /**
     * 气泡相连
     */
    private final int BUBBLE_STATE_CONNECT = 1;
    /**
     * 气泡分离
     */
    private final int BUBBLE_STATE_APART = 2;
    /**
     * 气泡消失
     */
    private final int BUBBLE_STATE_DISMISS = 3;

    /**
     * 气泡半径
     */
    private float mBubbleRadius;
    /**
     * 气泡颜色
     */
    private int mBubbleColor;
    /**
     * 气泡消息文字
     */
    private String mTextStr;
    /**
     * 气泡消息文字颜色
     */
    private int mTextColor;
    /**
     * 气泡消息文字大小
     */
    private float mTextSize;
    /**
     * 不动气泡的半径
     */
    private float mBubStillRadius;
    /**
     * 可动气泡的半径
     */
    private float mBubMoveableRadius;
    /**
     * 不动气泡的圆心
     */
    private PointF mBubStillCenter;
    /**
     * 可动气泡的圆心
     */
    private PointF mBubMoveableCenter;
    /**
     * 气泡的画笔
     */
    private Paint mBubblePaint;
    /**
     * 贝塞尔曲线path
     */
    private Path mBezierPath;

    private Paint mTextPaint;

    //文本绘制区域
    private Rect mTextRect;

    private Paint mBurstPaint;

    //爆炸绘制区域
    private Rect mBurstRect;

    /**
     * 气泡状态标志
     */
    private int mBubbleState = BUBBLE_STATE_DEFAULT;
    /**
     * 两气泡圆心距离
     */
    private float mDist;
    /**
     * 气泡相连状态最大圆心距离
     */
    private float mMaxDist;
    /**
     * 手指触摸偏移量
     */
    private float MOVE_OFFSET;

    /**
     * 气泡爆炸的bitmap数组
     */
    private Bitmap[] mBurstBitmapsArray;
    /**
     * 是否在执行气泡爆炸动画
     */
    private boolean mIsBurstAnimStart = false;

    /**
     * 当前气泡爆炸图片index
     */
    private int mCurDrawableIndex;

    /**
     * 气泡爆炸的图片id数组
     */
    private int[] mBurstDrawablesArray = {R.drawable.burst_1, R.drawable.burst_2
            , R.drawable.burst_3, R.drawable.burst_4, R.drawable.burst_5};


    public DragBubbleView(Context context) {
        this(context, null);
    }

    public DragBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DragBubbleView, defStyleAttr, 0);
        mBubbleRadius = array.getDimension(R.styleable.DragBubbleView_bubble_radius, mBubbleRadius);
        mBubbleColor = array.getColor(R.styleable.DragBubbleView_bubble_color, Color.RED);
        mTextStr = array.getString(R.styleable.DragBubbleView_bubble_text);
        mTextSize = array.getDimension(R.styleable.DragBubbleView_bubble_textSize, mTextSize);
        mTextColor = array.getColor(R.styleable.DragBubbleView_bubble_textColor, Color.WHITE);
        array.recycle();

        mBubStillRadius = mBubbleRadius;
        mBubMoveableRadius = mBubStillRadius;
        mMaxDist = 8 * mBubbleRadius;

        MOVE_OFFSET = mMaxDist / 4;

        //抗锯齿
        mBubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBubblePaint.setColor(mBubbleColor);
        mBubblePaint.setStyle(Paint.Style.FILL);
        mBezierPath = new Path();

        //文本画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextRect = new Rect();

        //爆炸画笔
        mBurstPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBurstPaint.setFilterBitmap(true);
        mBurstRect = new Rect();
        mBurstBitmapsArray = new Bitmap[mBurstDrawablesArray.length];
        for (int i = 0; i < mBurstDrawablesArray.length; i++) {
            //将气泡爆炸的drawable转为bitmap
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mBurstDrawablesArray[i]);
            mBurstBitmapsArray[i] = bitmap;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initView(w, h);
    }

    private void initView(int w, int h) {

        //设置两气泡圆心初始坐标
        if (mBubStillCenter == null) {
            mBubStillCenter = new PointF(w / 2, h / 2);
        } else {
            mBubStillCenter.set(w / 2, h / 2);
        }

        if (mBubMoveableCenter == null) {
            mBubMoveableCenter = new PointF(w / 2, h / 2);
        } else {
            mBubMoveableCenter.set(w / 2, h / 2);
        }
        mBubbleState = BUBBLE_STATE_DEFAULT;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        //        1.气泡静止状态
        //        2.气泡连接状态
        //        3.气泡分离状态
        //        4.气泡消失状态
        //        5.气泡还原动画


        // 1、画拖拽的气泡 和 文字
        if (mBubbleState != BUBBLE_STATE_DISMISS) {
            canvas.drawCircle(mBubMoveableCenter.x, mBubMoveableCenter.y,
                    mBubMoveableRadius, mBubblePaint);

            mTextPaint.getTextBounds(mTextStr, 0, mTextStr.length(), mTextRect);

            canvas.drawText(mTextStr, mBubMoveableCenter.x - mTextRect.width() / 2,
                    mBubMoveableCenter.y + mTextRect.height() / 2, mTextPaint);
        }
        // 2、画相连的气泡状态
        if (mBubbleState == BUBBLE_STATE_CONNECT) {
            // 1、画静止气泡
            canvas.drawCircle(mBubStillCenter.x, mBubStillCenter.y,
                    mBubStillRadius, mBubblePaint);
            // 2、画相连曲线
            // 计算控制点坐标，两个圆心的中点
            int iAnchorX = (int) ((mBubStillCenter.x + mBubMoveableCenter.x) / 2);
            int iAnchorY = (int) ((mBubStillCenter.y + mBubMoveableCenter.y) / 2);

            //三角函数
            float cosTheta = (mBubMoveableCenter.x - mBubStillCenter.x) / mDist;
            float sinTheta = (mBubMoveableCenter.y - mBubStillCenter.y) / mDist;


            //A
            float iBubStillStartX = mBubStillCenter.x - mBubStillRadius * sinTheta;
            float iBubStillStartY = mBubStillCenter.y + mBubStillRadius * cosTheta;
            //B
            float iBubMoveableEndX = mBubMoveableCenter.x - mBubMoveableRadius * sinTheta;
            float iBubMoveableEndY = mBubMoveableCenter.y + mBubMoveableRadius * cosTheta;
            //C
            float iBubMoveableStartX = mBubMoveableCenter.x + mBubMoveableRadius * sinTheta;
            float iBubMoveableStartY = mBubMoveableCenter.y - mBubMoveableRadius * cosTheta;
            //D
            float iBubStillEndX = mBubStillCenter.x + mBubStillRadius * sinTheta;
            float iBubStillEndY = mBubStillCenter.y - mBubStillRadius * cosTheta;

            mBezierPath.reset();
            // 画上半弧
            mBezierPath.moveTo(iBubStillStartX, iBubStillStartY);
            mBezierPath.quadTo(iAnchorX, iAnchorY, iBubMoveableEndX, iBubMoveableEndY);
            // 画下半弧
            mBezierPath.lineTo(iBubMoveableStartX, iBubMoveableStartY);
            mBezierPath.quadTo(iAnchorX, iAnchorY, iBubStillEndX, iBubStillEndY);
            //闭合PATH
            mBezierPath.close();
            canvas.drawPath(mBezierPath, mBubblePaint);
        }

        // 3、画消失状态---爆炸动画

        if (mIsBurstAnimStart) {
            mBurstRect.set((int) (mBubMoveableCenter.x - mBubMoveableRadius),
                    (int) (mBubMoveableCenter.y - mBubMoveableRadius),
                    (int) (mBubMoveableCenter.x + mBubMoveableRadius),
                    (int) (mBubMoveableCenter.y + mBubMoveableRadius));

            canvas.drawBitmap(mBurstBitmapsArray[mCurDrawableIndex], null,
                    mBurstRect, mBubblePaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (mBubbleState != BUBBLE_STATE_DISMISS) {
                    mDist = (float) Math.hypot(event.getX() - mBubStillCenter.x, event.getY() - mBubStillCenter.y);

                    if (mDist < mBubbleRadius) {
                        mBubbleState = BUBBLE_STATE_CONNECT;
                    } else {
                        mBubbleState = BUBBLE_STATE_DEFAULT;
                    }

                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                mBubMoveableCenter.x = event.getX();
                mBubMoveableCenter.y = event.getY();
                mDist = (float) Math.hypot(event.getX() - mBubStillCenter.x, event.getY() - mBubStillCenter.y);
                if (mBubbleState == BUBBLE_STATE_CONNECT) {

                    if (mDist < mMaxDist) {
                        mBubStillRadius = mBubbleRadius - mDist / 8;
                    } else {
                        mBubbleState = BUBBLE_STATE_APART;
                    }
                }
                invalidate();

                break;
            }
            case MotionEvent.ACTION_UP: {

                if (mBubbleState == BUBBLE_STATE_CONNECT) {
                    //弹回的动画
                    startBubbleRestAnim();
                } else if (mBubbleState == BUBBLE_STATE_APART) {
                    if (mDist < 2 * mBubbleRadius) {
                        //弹回的动画
                        startBubbleRestAnim();
                    } else {
                        //炸裂的动画
                        startBubbleBurstAnim();
                    }
                }


                break;
            }
        }
        return true;
    }

    private void startBubbleRestAnim() {

        ValueAnimator anim = ValueAnimator.ofObject(new PointFEvaluator(),
                new PointF(mBubMoveableCenter.x, mBubMoveableCenter.y),
                new PointF(mBubStillCenter.x, mBubStillCenter.y));

        anim.setDuration(200);
        anim.setInterpolator(new OvershootInterpolator(5f));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBubMoveableCenter = (PointF) animation.getAnimatedValue();
                invalidate();
            }
        });

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mBubbleState = BUBBLE_STATE_DEFAULT;
            }
        });

        anim.start();

    }


    private void startBubbleBurstAnim() {
        //气泡改为消失状态
        mBubbleState = BUBBLE_STATE_DISMISS;
        mIsBurstAnimStart = true;
        //做一个int型属性动画，从0~mBurstDrawablesArray.length结束
        ValueAnimator anim = ValueAnimator.ofInt(0, mBurstDrawablesArray.length);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //设置当前绘制的爆炸图片index
                mCurDrawableIndex = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //修改动画执行标志
                mIsBurstAnimStart = false;
            }
        });
        anim.start();

    }
}
