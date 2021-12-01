package demo.zkttestdemo.effect.nestedscroll

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView

/**
 * Created by zhoukaitong on 2021/6/21.
 * Description:
 */
class FixNestedScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    /**
     * 注意一定要用 NestedScrollingParent2 的方法，五个参数的
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        //super.onNestedPreScroll(target, dx, dy, consumed, type)
        Log.e("FixNestedScrollView", "view:${target.javaClass.simpleName} getTop:${target.top} dy:${dy} consumed[1]:${consumed[1]}  scrollY:${scrollY}")
        //不必纠结这个数值，就是为了效果一点点调出来的，具体阈值的根据业务处理
        if (scrollY < 600) {
            consumed[1] = dy
            scrollBy(0, dy)
        } else {
            //上滑正 下滑负
            if (dy > 0) {
                scrollTo(0, 600)
            }
        }
    }

    /**
     * @return false接收fling，true不接收
     */
    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return false
    }


}