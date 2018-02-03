package demo.zkttestdemo.effect.lockpattern

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

/**
 * 九宫格自定义控件
 * Created by zkt on 2018-2-2.
 */
class LockPatternView : View {

    // 确保只初始化一次
    private var mIsInit = false

    // 构造函数
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        // 初始化九宫格
        if (!mIsInit) {
            initDot()
            initPaint()
            mIsInit = true;
        }
        // 绘制九宫格
    }

    /**
     * 初始化画笔
     * 3个点状态的画笔（默认+按下+错误）、线的画笔、箭头的画笔
     */
    private fun initPaint() {

    }

    /**
     * 初始化九个点
     */
    private fun initDot() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

