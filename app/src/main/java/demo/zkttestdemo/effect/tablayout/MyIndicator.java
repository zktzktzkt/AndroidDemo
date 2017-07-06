package demo.zkttestdemo.effect.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 2017/6/27.
 */

public class MyIndicator extends RelativeLayout {
    /**
     * 线的颜色
     */
    private int mColor;
    /**
     * 线的高度
     */
    private int mHeight;
    /**
     * xml数组
     */
    private String[] mList;
    /**
     * textview数组
     */
    private List<TextView> mTextList = new ArrayList<>();
    /**
     * string数组
     */
    private List<String> mListTitle = new ArrayList<>();
    /**
     * 默认字体大小
     */
    private int mTextNomal;
    /**
     * 被选择字体大小
     */
    private int mTextPress;
    /**
     * 默认字体颜色
     */
    private int mText_Nomal;
    /**
     * 被选择的颜色
     */
    private int mText_Press;
    /**
     * 每个格子个长度
     */
    private int mContWidth;
    /**
     * 被选择的tab
     */
    private int mSelected;
    /**
     * 底线
     */
    private View mLine;
    /**
     * 是否或去过
     */
    private boolean mIsCheck;

    /**
     * 字体的多少倍
     */
    private float mBai;
    /**
     * 记录移动结束位置
     */
    private int mEndAddress;

    /**
     * 禁止滑动表情下标
     */
    private int mProhibitPisition = -1;

    /**
     * 动画时间
     */

    private int mAnimationTime;

    /**
     * 当前选中
     */
    private int mNowPosition;

    /**
     * 是否铺满
     */
    private boolean isFull;


    public MyIndicator(Context context) {
        this(context, null);
    }

    public MyIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Indicator, 0, 0);
        mColor = array.getColor(R.styleable.Indicator_indicatorColor, Color.RED);
        mList = getContext().getResources().getStringArray(array.getResourceId(R.styleable.Indicator_array, 0));
        mTextNomal = array.getInteger(R.styleable.Indicator_text_nomal_size, 12);
        mTextPress = array.getInteger(R.styleable.Indicator_text_press_size, 13);
        mText_Nomal = array.getColor(R.styleable.Indicator_text_nomal_color, Color.GRAY);
        mText_Press = array.getColor(R.styleable.Indicator_text_press_color, Color.BLACK);
        mSelected = array.getInteger(R.styleable.Indicator_selected, 0);
        isFull = array.getBoolean(R.styleable.Indicator_isFull, false);
        mAnimationTime = array.getInteger(R.styleable.Indicator_speed, 300);
        mBai = array.getFloat(R.styleable.Indicator_multiply, (float) 1.2);
        mHeight = array.getInteger(R.styleable.Indicator_line_hegith, 5);
        for (int i = 0; i < mList.length; i++) {
            mListTitle.add(mList[i]);
        }
        array.recycle();
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mIsCheck) {
            mIsCheck = true;
            initView();
        }
    }

    /**
     * 初始化布局
     */
    private void initView() {
        measure(0, 0);
        //获取每个textview布局所占的宽度
        mContWidth = getWidth() / mListTitle.size();
        //添加线
        mLine = new View(getContext());
        addView(mLine);
        int mWeight;
        if (isFull) {
            mWeight = mContWidth;
        } else {
            mWeight = (int) (setLineLength(mListTitle.get(mSelected)) * mBai);
            if (mWeight > mContWidth) {
                mWeight = mContWidth;
            }
        }
        //设置线的基本属性
        LayoutParams mLayoutParams1 = new LayoutParams(mWeight, mHeight);
        mLayoutParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mLine.setLayoutParams(mLayoutParams1);
        mLine.setBackgroundColor(mColor);
        //获取上一次所在位置
        mEndAddress = mContWidth * mSelected + (mContWidth - mWeight) / 2;
        //添加textview
        for (int i = 0; i < mListTitle.size(); i++) {
            addTextView(i);
        }
        //初始化点击事件
        setListener();
    }


    /**
     * 添加textview
     */
    private void addTextView(int i) {
        //初始化textview
        TextView textView = new TextView(getContext());
        textView.setText(mListTitle.get(i));
        //设置textview基本属性
        LayoutParams mLayoutParams = new LayoutParams(mContWidth, LayoutParams.MATCH_PARENT);
        mLayoutParams.leftMargin = mContWidth * i;
        mLayoutParams.addRule(CENTER_VERTICAL);
        textView.setLayoutParams(mLayoutParams);
        textView.setGravity(Gravity.CENTER);
        if (i == mSelected) {
            textView.setTextColor(mText_Press);
            textView.setTextSize(mTextPress);
        } else {
            textView.setTextColor(mText_Nomal);
            textView.setTextSize(mTextNomal);
        }
        //添加textview到布局
        mTextList.add(textView);
        addView(textView);
    }

    /**
     * 清空字体颜色
     */
    private void resetColor() {
        for (int i = 0; i < mTextList.size(); i++) {
            mTextList.get(i).setTextColor(getContext().getResources().getColor(R.color.text_hint));
        }
    }

    /**
     * 点击tab事件
     */
    private void setListener() {
        setAnimation(0, mEndAddress);
        for (int i = 0; i < mTextList.size(); i++) {
            final int finalI = i;
            mTextList.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalI != mSelected && mProhibitPisition != finalI) {
                        setChanger(finalI);
                    }
                    if (mOnIndiacatorClickListener != null)
                        mOnIndiacatorClickListener.onClick(finalI, v);
                }
            });
        }
    }


    /**
     * 动画
     *
     * @param statX 开始位置
     * @param endX  结束位置
     */
    private void setAnimation(int statX, int endX) {
        AnimationSet mSet = new AnimationSet(true);
        TranslateAnimation translate1 = new TranslateAnimation(
                statX, endX, 0, 0);
        mSet.addAnimation(translate1);
        mSet.setFillAfter(true);
        mSet.setDuration(mAnimationTime);
        mLine.startAnimation(mSet);

    }


    private OnIndiacatorClickListener mOnIndiacatorClickListener;


    /**
     * 计算下划线长度
     */
    private int setLineLength(String mTextView) {
        return dip2px(getContext(), mTextView.length() * mTextPress);
    }


    public int dip2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }


    /**
     * 添加页面
     *
     * @param charSequence
     */
    public void add(CharSequence charSequence, int position) {
        removeAllViews();
        mTextList.clear();
        if (mListTitle.size() == position)
            mListTitle.set(position - 1, charSequence.toString());
        else
            mListTitle.add(charSequence.toString());
        initView();

    }


    /**
     * 设置是否铺满
     */
    public void setFull() {
        isFull = true;
    }

    /**
     * 设置监听
     *
     * @param onIndiacatorClickListener
     */
    public void setIndiacatorListener(OnIndiacatorClickListener onIndiacatorClickListener) {
        if (onIndiacatorClickListener != null) {
            this.mOnIndiacatorClickListener = onIndiacatorClickListener;
        }
    }


    /**
     * 设置禁止滑动页面
     *
     * @param i
     */
    public void setProhibitPositio(int i) {
        this.mProhibitPisition = i;
    }

    /**
     * 设置动画时间
     */
    public void setAnimationTime(int time) {
        this.mAnimationTime = time;
    }

    /**
     * 外部监听
     */
    public interface OnIndiacatorClickListener {
        void onClick(int position, View view);
    }

    /**
     * 改变下标
     *
     * @param position
     */
    public void setChanger(int position) {
        //改为默认字体颜色修改选中字体颜色
        resetColor();
        mTextList.get(position).setTextColor(mText_Press);
        mTextList.get(position).setTextSize(mTextPress);
        mSelected = position;
        int mWeight;
        if (isFull) {
            mWeight = mContWidth;
        } else {
            mWeight = (int) (setLineLength(mListTitle.get(mSelected)) * mBai);
            if (mWeight > mContWidth) {
                mWeight = mContWidth;
            }
        }
        /**
         * 计算当前选择textview的X点位置
         */
        int way = mContWidth * mSelected + (mContWidth - mWeight) / 2;
        LayoutParams mLayoutParams = new LayoutParams(mWeight, mHeight);
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mLine.setLayoutParams(mLayoutParams);
        setAnimation(mEndAddress, way);
        mEndAddress = way;
        this.mNowPosition = position;
    }


    /**
     * 获取当前下标
     */
    public int getPosition() {
        return mNowPosition;
    }

}
