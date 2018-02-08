package demo.zkttestdemo.effect.lockpattern

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 九宫格自定义控件
 * Created by zkt on 2018-2-2.
 */
class LockPatternView : View {

    // 确保只初始化一次
    private var mIsInit = false
    // 外圆的半径
    private var mDotRadius: Int = 0
    // 二维数组初始化， int[3][3]
    private var mPoints: Array<Array<Point?>> = Array(3) { Array<Point?>(3, { null }) }
    // 画笔
    private lateinit var mLinePaint: Paint
    private lateinit var mPressedPaint: Paint
    private lateinit var mErrorPaint: Paint
    private lateinit var mNormalPaint: Paint
    private lateinit var mArrowPaint: Paint
    // 颜色
    private val mOuterNormalColor = 0xffd9d9d9.toInt()
    private val mOuterPressedColor = 0xff8cbad8.toInt()
    private val mOuterErrorColor = 0xff901032.toInt()
    private val mInnerNormalColor = 0xff929292.toInt()
    private val mInnerPressedColor = 0xff0596f6.toInt()
    private val mInnerErrorColor = 0xffea0945.toInt()

    // 按下的时候是否按在一个点上
    private var mIsTouchPoint = false
    // 选中的所有点
    private var mSelectPoint = ArrayList<Point>()

    // 构造函数
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        // 初始化九宫格
        if (!mIsInit) {
            initDot()
            initPaint()
            mIsInit = true
        }
        // 绘制九宫格
        drawShow(canvas)
    }

    /**
     * 初始化绘制
     */
    private fun drawShow(canvas: Canvas) {
        //绘制九宫格显示
        for (i in 0..2) {
            for (point in mPoints[i]) {
                if (point!!.statusIsNormal()) {
                    //先绘制外圆
                    mNormalPaint.color = mOuterNormalColor
                    canvas.drawCircle(point.centerX.toFloat(), point.centerY.toFloat(),
                            mDotRadius.toFloat(), mNormalPaint)
                    //后绘制内圆
                    mNormalPaint.color = mInnerNormalColor
                    canvas.drawCircle(point.centerX.toFloat(), point.centerY.toFloat(),
                            mDotRadius / 6.toFloat(), mNormalPaint)
                }
                if (point.statusIsPressed()) {
                    //先绘制外圆
                    mNormalPaint.color = mOuterNormalColor
                    canvas.drawCircle(point.centerX.toFloat(), point.centerY.toFloat(),
                            mDotRadius.toFloat(), mPressedPaint)
                    //后绘制内圆
                    mNormalPaint.color = mInnerNormalColor
                    canvas.drawCircle(point.centerX.toFloat(), point.centerY.toFloat(),
                            mDotRadius / 6.toFloat(), mPressedPaint)
                }
                if (point.statusIsError()) {
                    //先绘制外圆
                    mNormalPaint.color = mOuterNormalColor
                    canvas.drawCircle(point.centerX.toFloat(), point.centerY.toFloat(),
                            mDotRadius.toFloat(), mErrorPaint)
                    //后绘制内圆
                    mNormalPaint.color = mInnerNormalColor
                    canvas.drawCircle(point.centerX.toFloat(), point.centerY.toFloat(),
                            mDotRadius / 6.toFloat(), mErrorPaint)
                }

            }
        }
    }

    /**
     * 初始化画笔
     * 3个点状态的画笔（默认+按下+错误）、线的画笔、箭头的画笔
     */
    private fun initPaint() {
        // 默认的画笔
        mNormalPaint = Paint()
        mNormalPaint.color = mInnerNormalColor
        mNormalPaint.style = Paint.Style.STROKE
        mNormalPaint.isAntiAlias = true
        mNormalPaint.strokeWidth = (mDotRadius / 9).toFloat()
        // 按下的画笔
        mPressedPaint = Paint()
        mPressedPaint.color = mInnerPressedColor
        mPressedPaint.style = Paint.Style.STROKE
        mPressedPaint.isAntiAlias = true
        mPressedPaint.strokeWidth = (mDotRadius / 6).toFloat()
        // 错误的画笔
        mErrorPaint = Paint()
        mErrorPaint.color = mInnerErrorColor
        mErrorPaint.style = Paint.Style.STROKE
        mErrorPaint.isAntiAlias = true
        mErrorPaint.strokeWidth = (mDotRadius / 6).toFloat()
        // 线的画笔
        mLinePaint = Paint()
        mLinePaint.color = mInnerPressedColor
        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.isAntiAlias = true
        mLinePaint.strokeWidth = (mDotRadius / 9).toFloat()
        // 箭头的画笔
        mArrowPaint = Paint()
        mArrowPaint.color = mInnerPressedColor
        mArrowPaint.style = Paint.Style.FILL
        mArrowPaint.isAntiAlias = true

    }

    /**
     * 初始化九个点
     */
    private fun initDot() {
        //用二维数组装起来
        // 不断绘制的时候 这几个点都有状态，而且肯定需要回调，都有下标，每一个点都是一个对象
        // 计算中心位置
        var width = this.width
        var height = this.height

        //兼容横竖屏
        var offsetX = 0
        var offsetY = 0
        if (height > width) {
            offsetY = (height - width) / 2
            height = width
        } else {
            offsetX = (width - height) / 2
            width = height
        }

        var squareWidth = width / 3

        //外圆的大小
        mDotRadius = width / 12

        mPoints[0][0] = Point(offsetX + squareWidth / 2, offsetY + squareWidth / 2, 0)
        mPoints[0][1] = Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth / 2, 1)
        mPoints[0][2] = Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth / 2, 2)
        mPoints[1][0] = Point(offsetX + squareWidth / 2, offsetY + squareWidth * 3 / 2, 3)
        mPoints[1][1] = Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth * 3 / 2, 4)
        mPoints[1][2] = Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth * 3 / 2, 5)
        mPoints[2][0] = Point(offsetX + squareWidth / 2, offsetY + squareWidth * 5 / 2, 6)
        mPoints[2][1] = Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth * 5 / 2, 7)
        mPoints[2][2] = Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth * 5 / 2, 8)
    }

    /**
     * 宫格的类
     */
    class Point(var centerX: Int, var centerY: Int, var index: Int) {
        private val STATUS_NORMAL = 1
        private val STATUS_PRESSED = 2
        private val STATUS_ERROR = 3
        //当前点的状态 默认情况下
        private var status = STATUS_NORMAL

        fun setStatusPressed() {
            status = STATUS_PRESSED
        }

        fun setStatusNormal() {
            status = STATUS_NORMAL
        }

        fun setStatusError() {
            status = STATUS_ERROR
        }

        fun statusIsPressed(): Boolean {
            return status == STATUS_PRESSED
        }

        fun statusIsError(): Boolean {
            return status == STATUS_ERROR
        }

        fun statusIsNormal(): Boolean {
            return status == STATUS_NORMAL
        }
    }

    var mMovingX = 0f
    var mMovingY = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mMovingX = event.x
        mMovingY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 判断一个点是否在圆里面 （点到圆心的距离 < 半径）
                var point = point
                if (point != null) {
                    mIsTouchPoint = true
                    mSelectPoint.add(point)
                    //改变当前点的状态
                    point.setStatusPressed()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (mIsTouchPoint) {
                    //按下的时候一定要在一个点上，不断触摸的时候不断判断新的点
                    var point = point
                    if (point != null) {
                        if (!mSelectPoint.contains(point)) {
                            mSelectPoint.add(point)
                        }
                        point.setStatusPressed()
                    }
                }
            }
            MotionEvent.ACTION_UP -> {

            }
        }

        invalidate()
        return true
    }

    /**
     * 获取按下的点
     * @return 当前按下的点
     */
    private val point: Point?
        get() {
            for (i in mPoints.indices) {
                for (j in 0..mPoints[i].size - 1) {
                    val point = mPoints[i][j]
                    if (point != null) {
                        if (MathUtil.checkInRound(point.centerX.toFloat(), point.centerY.toFloat(),
                                mDotRadius.toFloat(), mMovingX, mMovingY)) {
                            return point
                        }
                    }
                }
            }
            return null
        }

}

