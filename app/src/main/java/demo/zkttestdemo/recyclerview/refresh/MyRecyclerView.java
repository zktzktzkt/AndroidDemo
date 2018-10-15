package demo.zkttestdemo.recyclerview.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 18/10/14.
 * Description:
 */

public class MyRecyclerView extends LinearLayout {
    private int mTouchSlop;
    //分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;

    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private RecyclerViewRefreshStateCall mRecyclerViewRefreshStateCall;

    //状态
    //默认状态
    private int DEFAULT = 0;
    //头部显示不全
    private final int PULL_DOWN_REFRESH = 1;
    //头部显示全
    private final int RELEASE_REFRESH = 2;
    //刷新中
    private final int REFRESHING = 3;
    //加载更多
    private final int LOAD_MORE = 4;
    //状态标记
    private int STATE = DEFAULT;
    //刷新头部宽度
    private int rfreshHeaderWidth;
    //刷新头部高度
    private int refreshHeadviewHeight;

    private OnPullListener mOnPullListener;
    private View mRefreshHeaderView;
    int refreshHeadviewId;
    boolean flag = false;

    public MyRecyclerView(Context context) {
        this(context, null);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //      ★★★★★一个坑initAttrs方法里的typedArray去获取属性时，第一次获取的属性全是0，他会马上重走一次构造方法，再次获取一次，才能获得正确的值
        //      如果第一次获取的值为0，则不去initView
        //       在这里以flag作为一个标识，第一遍走initAttrs时，将flag设为true，也就是第二次才走initView
        initAttrs(context, attrs);
        if (flag) {
            initView(context);
        }
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PullRefreshRecyclerView);
        refreshHeadviewId = typedArray.getResourceId(R.styleable.PullRefreshRecyclerView_refresh_header_view, 0);
        typedArray.recycle();

        flag = true;
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        mScroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();

        //添加headerview
        // ★★★★★ 注意不要用这个方法inflate布局，会导致layout的所有属性失效，height、width、margin
        // ★★★★★ mRefreshHeaderView = mInflater.inflate(R.layout.headerview_moren, null);
        // 原因见  http://blog.csdn.net/zhaokaiqiang1992/article/details/36006467
        if (refreshHeadviewId == 0) {
            mRefreshHeaderView = LayoutInflater.from(context).inflate(R.layout.headerview_default, this, false);
            //这里我提供了一个默认的显示效果，如果用户不使用mRealPullRefreshView.setOnPullShowViewListener的话，会默认使用这个
            // 用户可以实现OnPullShowViewListener接口，去实现自己想要的显示效果
            mRecyclerViewRefreshStateCall = new ImplRecyclerViewRefreshStateCall(this);
        } else {
            mRefreshHeaderView = LayoutInflater.from(context).inflate(refreshHeadviewId, this, false);
            if (mRecyclerViewRefreshStateCall == null) {
                throw new RuntimeException("由于您使用了自定义的头布局，你要使用setRecyclerViewRefreshStateCall()方法，自定义一个该布局的动画效果,可参照ImplRecyclerViewRefreshStateCall");
            }
        }

        addView(mRefreshHeaderView);

        // 以下代码主要是为了设置头布局的marginTop值为-headerviewHeight
        // 注意必须等到一小会才会得到正确的头布局宽高，滑动时差
        postDelayed(new Runnable() {
            @Override
            public void run() {
                rfreshHeaderWidth = mRefreshHeaderView.getWidth();
                refreshHeadviewHeight = mRefreshHeaderView.getHeight();

                MarginLayoutParams lp = new LayoutParams(rfreshHeaderWidth, refreshHeadviewHeight);
                lp.setMargins(0, -refreshHeadviewHeight, 0, 0);
                mRefreshHeaderView.setLayoutParams(lp);

            }
        }, 10);

        //      添加RecyclerView
        mRecyclerView = new RecyclerView(context);
        addView(mRecyclerView, LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLoadMore();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        this.mLayoutManager = manager;
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);

    }

    public View getRefreshHeaderView() {
        return mRefreshHeaderView;
    }

    public void setRecyclerViewRefreshStateCall(RecyclerViewRefreshStateCall recyclerViewRefreshStateCall) {
        mRecyclerViewRefreshStateCall = recyclerViewRefreshStateCall;
    }


    public void setOnPullListener(OnPullListener onPullListener) {
        mOnPullListener = onPullListener;
    }

    /**
     * 回调接口
     */
    public interface OnPullListener {
        /**
         * 当下拉刷新正在刷新时，这时候可以去请求数据，记得最后调用refreshFinish()复位
         */
        void onRefresh();

        /**
         * 当加载更多时
         */
        void onLoadMore();
    }


    /**
     * 回调接口，可以通过下面的回调，自定义各种状态下的显示效果
     * 可以根据下拉距离scrollY设计动画效果
     */
    public interface RecyclerViewRefreshStateCall {

        /**
         * 当处于下拉刷新时，头布局显示效果
         *
         * @param scrollY        下拉的距离
         * @param headviewHeight 头布局高度
         * @param deltaY         moveY-lastMoveY,正值为向下拉
         */
        void onPullDownRefreshState(int scrollY, int headviewHeight, int deltaY);

        /**
         * 当处于松手刷新时，头布局显示效果
         *
         * @param scrollY 下拉的距离
         * @param deltaY  moveY-lastMoveY,正值为向下拉
         */
        void onReleaseRefreshState(int scrollY, int deltaY);

        /**
         * 正在刷新时，页面的显示效果
         */
        void onRefreshingState();

        /**
         * 默认状态时，页面显示效果，主要是为了复位各种状态
         */
        void onDefaultState();
    }

}
