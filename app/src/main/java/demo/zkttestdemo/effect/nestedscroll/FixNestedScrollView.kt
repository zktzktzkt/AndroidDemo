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

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        Log.e("FixNestedScrollView", "view:${target.javaClass.simpleName} dy:${dy} consumed[1]:${consumed[1]}")
        super.onNestedPreScroll(target, dx, dy, consumed)
    }
}