package demo.zkttestdemo.effect.surfaceview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View.MeasureSpec.AT_MOST
import com.blankj.utilcode.util.SizeUtils
import demo.zkttestdemo.R
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author huchangjie1
 * @date 2022-02-25
 *
 * 倒计时view解决：主线程阻塞造成的倒计时跳秒/间隔不足1s的问题
 */
class DownTimeView constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {

    companion object {
        const val TAG = "DownTimeView"

        //水平左对齐
        const val LEFT = 1

        //水平居中
        const val CENTER = 2

        //水平右对齐
        const val RIGHT = 3
    }

    //draw 线程
    private val mDrawThread: DrawThread

    //用线程池管理线程，避免随意开辟新线程
    private val mES: ExecutorService

    //倒计时的数
    private var mCountDown = 5

    //字体颜色
    private val mTextColor: Int

    //字体大小
    private val mTextSize: Float

    //文本显示模式
    private val mGravity: Int

    //倒计时文本
    private val mText: String?
    private val mBgRect = RectF()

    //背景画笔
    var mBgPain = Paint()

    //文本画笔
    var mTextPaint = Paint()

    //绘制的文本
    private var mContent = ""

    //背景半径
    private var mRadio: Float = 50f

    //倒计时结束回调
    private var mCompleteRun: Runnable? = null

    init {
        mDrawThread = DrawThread()
        mES = Executors.newSingleThreadExecutor()

        val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DownTimeView)
        mTextColor = ta.getColor(R.styleable.DownTimeView_downTime_textColor, Color.WHITE)
        mTextSize = ta.getDimension(R.styleable.DownTimeView_downTime_textSize, SizeUtils.dp2px(12f).toFloat())
        mText = ta.getString(R.styleable.DownTimeView_downTime_text)?.toString()
        mGravity = ta.getInt(R.styleable.DownTimeView_downTime_textGravity, 0)
        ta.recycle()
        //定义画笔
        mTextPaint.let {
            it.color = mTextColor
            //去锯齿
            it.isAntiAlias = true
            it.textSize = mTextSize
        }
        mBgPain.let {
            it.color = Color.BLACK
            it.alpha = 20
            it.style = Paint.Style.FILL
        }

        mContent = mText + mCountDown + "s"

        holder.addCallback(this)
        //设置画布  背景透明
        holder.setFormat(PixelFormat.TRANSLUCENT)
        setZOrderOnTop(true)
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
    }


    /**
     * @param time:单位秒
     */
    fun startTime(time: Int, runnable: Runnable) {
        this.mCountDown = time
        this.mCompleteRun = runnable

        mContent = mText + mCountDown + "s"
        invalidate()
    }


    /**
     * 用于绘制的线程
     */
    internal inner class DrawThread : Runnable {

        var isRun = true

        override fun run() {
            synchronized(holder) {
                //绘制背景
                var canvas: Canvas?
                while (isRun) {
                    canvas = holder.lockCanvas()
                    try {
                        canvas?.let {
                            //本次绘制的文本
                            mContent = mText + mCountDown-- + "s"
                            //清除canvas
                            it.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                            //绘制背景
                            it.drawRoundRect(mBgRect, mRadio, mRadio, mBgPain)
                            val local = calLocation(mContent)
                            //锁定并返回画布
                            when (mGravity) {
                                LEFT -> {
                                    it.drawText(mContent, 0f, paddingTop + local[1] - 2, mTextPaint)
                                }
                                CENTER -> {
                                    val left = (mBgRect.width() - local[0]) / 2
                                    it.drawText(mContent, left, paddingTop + local[1] - 2, mTextPaint)
                                }
                                RIGHT -> {
                                    val right = mBgRect.width() - local[0]
                                    it.drawText(mContent, right, paddingTop + local[1] - 2, mTextPaint)
                                }
                                else -> {
                                    it.drawText(mContent, paddingLeft.toFloat(), paddingTop + local[1] - 2, mTextPaint)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        //解锁画布并显示到屏幕上
                        if (canvas != null) {
                            holder.unlockCanvasAndPost(canvas)
                        }
                    }

                    Thread.sleep(1000)
                    if (mCountDown < 1) {
                        isRun = false
                        mCompleteRun?.run()
                    }
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var right = this@DownTimeView.width.toFloat()
        var bottom = this@DownTimeView.height.toFloat()
        //适配wrap_content
        if (MeasureSpec.getMode(widthMeasureSpec) == AT_MOST || MeasureSpec.getMode(heightMeasureSpec) == AT_MOST) {
            //根据文字的大小计算surface view 的大小
            val rect = calLocation(mContent)
            right = paddingLeft + paddingRight + rect[0]
            bottom = paddingTop + paddingBottom + rect[1]
            setMeasuredDimension(right.toInt(), bottom.toInt())
        }

        mBgRect.let {
            it.left = 0f
            it.right = right
            it.top = 0f
            it.bottom = bottom
        }
        mRadio = bottom / 2
    }

    /**
     * 计算文字的大小
     *
     * @return [0]:文字width； [1]:文字height
     */
    private fun calLocation(txt: String): FloatArray {

        val textRect = Rect()
        mTextPaint.getTextBounds(txt, 0, txt.length, textRect)

        Log.d(TAG, "text width: ${textRect.width()}")
        Log.d(TAG, "text height: ${textRect.height()}")
        return floatArrayOf(textRect.width().toFloat(), textRect.height().toFloat())
    }

    /**
     * surface创建的时候调用
     */
    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(TAG, "surface create")
        mES.execute(mDrawThread)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(TAG, "surface destroyed")
        mDrawThread.isRun = false
        mES.shutdown()
        //app切到后台会直接执行
        mCompleteRun?.run()
    }

}