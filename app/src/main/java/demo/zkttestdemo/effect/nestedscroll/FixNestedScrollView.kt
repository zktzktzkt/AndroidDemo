package demo.zkttestdemo.effect.nestedscroll

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by zhoukaitong on 2021/6/21.
 * Description:
 */
class FixNestedScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    private var mChildRV: RecyclerView? = null
    private var mTotalDistance: Double = 0.0

    //TODO 不必纠结这个数值，就是为了效果一点点调出来的，具体阈值的根据业务处理
    private var mDefaultValue: Int = 600

    private var mVelocityY: Int = 0
    private var mFlingHelper: FlingHelper = FlingHelper(getContext())

    init {

        setOnScrollChangeListener(object : OnScrollChangeListener {
            override fun onScrollChange(v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {

                mChildRV?.let {
                    if (it.computeVerticalScrollOffset() > 0) {
                        scrollTo(0, mDefaultValue)
                        return
                    }
                }

                if (scrollY >= mDefaultValue && !v.canScrollVertically(1)) {
                    Log.e("FixNestedScrollView", "onScrollChange scrollY:${scrollY} 不能滑了")
                    val remainDistance = mTotalDistance - scrollY
                    val velocitY = mFlingHelper.getVelocityByDistance(remainDistance)
                    childFling(velocitY)
                }
            }
        })
    }

    private fun childFling(velocitY: Int) {
        mChildRV?.fling(0, velocitY)
    }

    fun setchildRV(recyclerView: RecyclerView) {
        mChildRV = recyclerView
    }

    /**
     * 注意一定要用 NestedScrollingParent2 的方法，五个参数的
     * @param dy 上滑正 下滑负
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        //super.onNestedPreScroll(target, dx, dy, consumed, type)
        //Log.e("FixNestedScrollView", "view:${target.javaClass.simpleName} getTop:${target.top} dy:${dy} consumed[1]:${consumed[1]}  scrollY:${scrollY}")
        if (scrollY < mDefaultValue) {
            consumed[1] = dy
            scrollBy(0, dy)
        }
    }

    /**
     * @return false接收fling，true不接收
     */
    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return false
    }


    override fun fling(velocityY: Int) {
        super.fling(velocityY)
        mVelocityY = velocityY
        mTotalDistance = mFlingHelper.getSplineFlingDistance(velocityY)
        Log.e("FixNestedScrollView", "fling滑动总距离->$mTotalDistance")
    }


}