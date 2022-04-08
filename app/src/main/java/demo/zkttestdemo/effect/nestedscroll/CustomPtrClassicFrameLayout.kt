package demo.zkttestdemo.effect.nestedscroll

import `in`.srain.cube.views.ptr.PtrClassicFrameLayout
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

class CustomPtrClassicFrameLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null
) : PtrClassicFrameLayout(context, attrs) {

    private var mListener: DispatchTouchEventListener? = null

    private var mLastPosition = 0f
    private var mDistance = 0f
    private var isHandle = false

    override fun dispatchTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastPosition = e.rawY
                mDistance = 0f
                isHandle = mListener?.down() ?: false
            }
            MotionEvent.ACTION_MOVE -> {
                val moveY = e.rawY - mLastPosition
                mLastPosition = e.rawY
                //滑动距离
                mDistance += moveY
                mListener?.move(moveY, mDistance)
            }
        }
        return if (!isHandle) super.dispatchTouchEvent(e) else isHandle
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }

    interface DispatchTouchEventListener {
        fun down(): Boolean
        fun move(moveY: Float, mDistance: Float)
    }

    fun setDispatchTouchEventListener(listener: DispatchTouchEventListener) {
        this.mListener = listener
    }
}