package com.jdcar.lib.keyboard.inputbox

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.jdcar.lib.keyboard.R
import java.util.*


var TextView.textColor: Int
    get() = currentTextColor
    set(value) = setTextColor(value)

/**
 * 单个展示输入框
 */
class DisplayTextView : AppCompatTextView, View.OnClickListener {
    private val LOG_TAG = "JDKeyboard"

    private var mIsNewEnergy = false//是否为新能源
    private var inputText: String? = null//输入的内容,因为onDraw时，text需要改变，不能直接用text
    private var isCursorShowing: Boolean = true//光标是否在展示
    private var timerTask: TimerTask? = null//用于模拟光标的闪烁
    private var timer: Timer? = null
    private val cursorFlashTime: Long = 400//光标刷新时间
    private var currentItemPosition: Int = 0//当前选中的位置
    private var onItemClickListener: OnItemClickListener? = null//点击回调

    private val colorOfNormalCursor = Color.parseColor("#1766DE")//光标颜色(正常）
    private val colorOfNewEnergyCursor = Color.parseColor("#00CF61")//光标颜色（新能源）
    private var colorOfBlack = Color.parseColor("#333333")//文字颜色

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        timer?.scheduleAtFixedRate(timerTask, 0, cursorFlashTime)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        timer?.cancel()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (TextUtils.isEmpty(inputText)) {
            text = if (currentItemPosition == tag) {
                if (isCursorShowing) "|" else ""
            } else {
                ""
            }
        }
    }

    override fun onClick(v: View?) {
        isSelected = true
        currentItemPosition = tag as Int
        onItemClickListener?.onItemClick(currentItemPosition)
    }

    private fun init() {
        gravity = Gravity.CENTER
        textSize = 16f
//        val typeFace = Typeface.createFromAsset(context?.assets, "fonts/JDZhengHT-EN-Bold.ttf")
        typeface = Typeface.DEFAULT_BOLD
        textColor = colorOfBlack
        setBackgroundResource(R.drawable.default_key_common_normal)
        setOnClickListener(this)
        timerTask = object : TimerTask() {
            override fun run() {
                isCursorShowing = !isCursorShowing
                postInvalidate()
            }
        }
        timer = Timer()
    }

    // 更新TextView
    fun setInputText(content: String?) {
        inputText = if (TextUtils.isEmpty(content)) {
            ""
        } else {
            content
        }

        refreshCursor(currentItemPosition)
        text = inputText
    }

    // 设置是否为新能源
    fun setIsNewEnergy(isNewEnergy: Boolean) {
        mIsNewEnergy = isNewEnergy
    }

    // 刷新光标
    fun refreshCursor(position: Int) {
        currentItemPosition = position
        isSelected = null != tag && currentItemPosition == tag as Int
        Log.d(LOG_TAG, "tag= $tag , isSelected= $isSelected")

        //背景色
        var curBackgroundResource: Int = if (mIsNewEnergy) {
            R.drawable.selector_bg_input_new_energy
        } else {
            R.drawable.selector_bg_input_normal
        }
        //最后一个是否为空
        if (tag != currentItemPosition && tag == 7 && TextUtils.isEmpty(inputText)) {
            curBackgroundResource = R.drawable.shape_solid_ffffff_stroke_929292_corners_4
        }
        setBackgroundResource(curBackgroundResource)

        //文字颜色
        val curTextColor: Int = if (TextUtils.isEmpty(inputText)) {
            if (mIsNewEnergy) {
                colorOfNewEnergyCursor
            } else {
                colorOfNormalCursor
            }
        } else {
            colorOfBlack
        }
        textColor = curTextColor
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


}