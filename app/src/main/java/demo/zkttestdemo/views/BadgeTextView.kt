package demo.zkttestdemo.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.ColorInt
import com.blankj.utilcode.util.SizeUtils
import demo.zkttestdemo.R

/**
 * Created by zhoukaitong on 2021/4/16.
 * Description:
 */
class BadgeTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {


    /**文本宽度*/
    private var mTextWidth: Int = 0
    /**当多个文本时的间距*/
    private var mMultiTextGap: Float
    /**边框粗细*/
    private var mStrokeWidth: Float = 0F
    val mRect = RectF()
    val mStrokeRect = RectF()
    val mBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val mStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        //style = Paint.Style.STROKE
    }

    init {
        val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.BadgeTextView)
        val bgColor = ta.getColor(R.styleable.BadgeTextView_badge_background_color, Color.parseColor("#FA2C19"))
        val strokeColor = ta.getColor(R.styleable.BadgeTextView_badge_stroke_color, Color.parseColor("#ffffff"))
        mStrokeWidth = ta.getDimension(R.styleable.BadgeTextView_badge_stroke_width, 0F)
        mMultiTextGap = ta.getDimension(R.styleable.BadgeTextView_badge_multi_gap, 8F)
        ta.recycle()

        mBgPaint.color = bgColor
        mStrokePaint.strokeWidth = mStrokeWidth
        mStrokePaint.color = strokeColor

        //默认居中
        gravity = Gravity.CENTER
    }

    /**
     * 设置背景颜色
     */
    fun setBadgeBackgroundColor(@ColorInt color: Int) {
        mBgPaint.color = color
        invalidate()
    }

    /**
     * 设置边框线颜色
     */
    fun setStrokeColor(@ColorInt color: Int) {
        mStrokePaint.color = color
        invalidate()
    }

    /**
     * 设置边框线粗细 dp
     */
    fun setStrokeWidth(width: Float) {
        mStrokeWidth = SizeUtils.dp2px(width).toFloat()
        mStrokePaint.strokeWidth = mStrokeWidth
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mTextWidth = measuredWidth
        val strokeWidth = Math.ceil(mStrokeWidth.toDouble()).toInt()
        if (text.length <= 1) {
            val size = Math.max(measuredWidth + strokeWidth * 2, measuredHeight + strokeWidth * 2)
            setMeasuredDimension(size, size)
        } else {
            setMeasuredDimension(
                measuredWidth + mMultiTextGap.toInt() + strokeWidth * 2,
                measuredHeight + strokeWidth * 2)
        }
    }

    override fun onDraw(canvas: Canvas) {
        //canvas.drawColor(Color.YELLOW)
        if (text.length >= 2) {
            mStrokeRect.apply {
                left = 0F
                top = 0F
                right = measuredWidth.toFloat()
                bottom = measuredHeight.toFloat()
            }
            mRect.apply {
                left = mStrokeWidth
                top = mStrokeWidth
                right = measuredWidth.toFloat() - mStrokeWidth
                bottom = measuredHeight.toFloat() - mStrokeWidth
            }
            canvas.drawRoundRect(mStrokeRect, 40F, 40F, mStrokePaint)
            canvas.drawRoundRect(mRect, 40F, 40F, mBgPaint)

        } else {
            canvas.drawCircle(measuredWidth / 2F, measuredWidth / 2F,
                measuredWidth / 2F, mStrokePaint)
            canvas.drawCircle(measuredWidth / 2F, measuredWidth / 2F,
                measuredWidth / 2F - mStrokeWidth, mBgPaint)
        }

        canvas.translate(measuredWidth / 2F - mTextWidth / 2F, 0F)

        super.onDraw(canvas)
    }

}
