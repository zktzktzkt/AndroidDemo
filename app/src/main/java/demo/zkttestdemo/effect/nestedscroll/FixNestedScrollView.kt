package demo.zkttestdemo.effect.nestedscroll

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by zhoukaitong on 2021/6/21.
 * Description:
 */
class FixNestedScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    private var mChildVelocityY: Float = 0.0f
    private var mChildRV: RecyclerView? = null
    private var mVelocityY: Int = 0
    private var mFlingHelper: FlingHelper = FlingHelper(getContext())

    //限制滑动的最大距离 调用attachRV()方法时赋值
    private var mLimitHeight: Int = 0

    init {

        setOnScrollChangeListener(object : OnScrollChangeListener {

            override fun onScrollChange(v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {

                //当child没有滑到顶的时候，控制外层不允许滑动
                mChildRV?.let {
                    if (it.computeVerticalScrollOffset() > 0) {
                        scrollTo(0, mLimitHeight)
                        return
                    }
                }
                // 到达顶部的时候，允许下拉刷新
                if (scrollY == 0) {
                    // refreshLayout.setEnabled(true);
                }
                // 处理子View的fling
                if (scrollY >= mLimitHeight && !v.canScrollVertically(1)) {
                    Log.e("FixNestedScrollView", "onScrollChange scrollY:${scrollY} 不能滑了")
                    childFling()
                }
            }
        })
    }

    private fun childFling() {
        if (mVelocityY > 0) {
            //速度转距离
            val splineFlingDistance = mFlingHelper.getSplineFlingDistance(mVelocityY)
            if (splineFlingDistance > mLimitHeight) {
                // 距离转速度
                val velocityByDistance = mFlingHelper.getVelocityByDistance(splineFlingDistance - mLimitHeight)
                mChildRV?.fling(0, velocityByDistance)
            }
            mVelocityY = 0
        }
    }

    /**
     * 绑定内层的rv
     */
    fun attachRV(recyclerView: RecyclerView, limitHeight: Int) {
        mChildRV = recyclerView
        mLimitHeight = limitHeight
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mChildRV?.stopScroll() //停止child的fling事件
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    /**
     * 注意一定要用 NestedScrollingParent2 的方法，五个参数的
     * 方法触发时机：child的MOVE事件和fling事件
     * @param dy 上滑正 下滑负
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        //super.onNestedPreScroll(target, dx, dy, consumed, type)
        Log.e("FixNestedScrollView", "view:${target.javaClass.simpleName} getTop:${target.top} dy:${dy} consumed[1]:${consumed[1]}  scrollY:${scrollY}")
        if (scrollY < mLimitHeight) {
            consumed[1] = dy
            scrollBy(0, dy)
        }
    }

    /**
     * @return false接收fling，true不接收
     */
    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        mChildVelocityY = velocityY
        return false
    }


    /**
     * 自己的fling
     */
    override fun fling(velocityY: Int) {
        super.fling(velocityY)
        mVelocityY = if (velocityY <= 0) 0 else velocityY //滑动的速度
    }


}