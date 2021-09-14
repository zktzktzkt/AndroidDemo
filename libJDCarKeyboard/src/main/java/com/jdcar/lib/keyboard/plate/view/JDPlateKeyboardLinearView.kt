package com.jdcar.lib.keyboard.plate.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.BitmapDrawable
import android.inputmethodservice.Keyboard.KEYCODE_DELETE
import android.inputmethodservice.Keyboard.KEYCODE_DONE
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.View.OnFocusChangeListener
import android.widget.*
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.jdcar.lib.keyboard.R
import com.jdcar.lib.keyboard.keys.JDKeyboardViewImpl
import com.jdcar.lib.keyboard.plate.JDPlateABCKeyboardImpl
import com.jdcar.lib.keyboard.plate.JDPlateProvinceKeyboardImpl
import com.jdcar.lib.keyboard.plate.PlateConstants
import com.jdcar.lib.keyboard.plate.callback.JDPlateKeyClickListenerAble
import java.lang.reflect.Method


/**
 * 横向的车牌手动输入组件
 * showLayout中必须有id : layoutPlateProvince 、etPlateInput、layoutPlateScan、tvPlateProvince
 * 自定义键盘的坑：
 * 1、点击返回键隐藏键盘
 * 2、隐藏系统键盘
 */
class JDPlateKeyboardLinearView : LinearLayout {
    var showPlateScan: Boolean? = false//是否展示车牌扫一扫

    var layoutPlateProvince: ConstraintLayout? = null//车牌省包裹view
    var etPlateInput: EditText? = null//车牌输入框
    var layoutPlateScan: ConstraintLayout? = null//车牌扫一扫
    var tvPlateProvince: TextView? = null//车牌省

    private var popupPlateKeyboard: PopupWindow? = null
    private var layoutPlateKeyboardView: JDKeyboardViewImpl? = null
    private var abcKeyboard: JDPlateABCKeyboardImpl? = null
    private var provinceKeyboard: JDPlateProvinceKeyboardImpl? = null

    private var maxInputLength = PlateConstants.Plate_Number_Common//最大输入长度，默认普通车牌

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        onInitLayout(attrs)
    }

    //初始化自定义属性
    private fun onInitLayout(attrs: AttributeSet?) {

        val typedArray: TypedArray? =
            context.obtainStyledAttributes(attrs, R.styleable.JDPlateKeyboardLinearView)
        val inflateLayout: Int? = typedArray?.getResourceId(
            R.styleable.JDPlateKeyboardLinearView_customLayout,
            R.layout.layout_plate_keyboard_linear
        )
        showPlateScan =
            typedArray?.getBoolean(R.styleable.JDPlateKeyboardLinearView_showScan, false)
        typedArray?.recycle()

        if (null == inflateLayout) {
            LayoutInflater.from(context).inflate(R.layout.layout_plate_keyboard_linear, this, true)
        } else {
            LayoutInflater.from(context).inflate(inflateLayout, this, true)
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onFinishInflate() {
        super.onFinishInflate()
        layoutPlateProvince = findViewById(R.id.layoutPlateProvince)
        etPlateInput = findViewById(R.id.etPlateInput)
        layoutPlateScan = findViewById(R.id.layoutPlateScan)
        tvPlateProvince = findViewById(R.id.tvPlateProvince)

        if (null != showPlateScan && showPlateScan!!) {
            layoutPlateScan?.visibility = View.VISIBLE
        } else {
            layoutPlateScan?.visibility = View.GONE
        }

        layoutPlateProvince?.setOnClickListener {
            onShowPopupOfPlateKeyboard(it, true)
        }

        etPlateInput?.setOnClickListener {
            onShowPopupOfPlateKeyboard(it, false)
        }
        etPlateInput?.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                return@OnFocusChangeListener
            }
            onShowPopupOfPlateKeyboard(v, false)
        }

        onDisableShowSoftInput(etPlateInput)
        onInitPlatePopupWindow()
    }

    override fun onDetachedFromWindow() {
        if (null != popupPlateKeyboard && popupPlateKeyboard?.isShowing == true) {
            popupPlateKeyboard?.dismiss()
            popupPlateKeyboard = null
        }
        super.onDetachedFromWindow()
    }

    /**
     * 创建PopupWindow
     */
    private fun onInitPlatePopupWindow() {
        if (null != popupPlateKeyboard && popupPlateKeyboard?.isShowing == true) {
            return
        }
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.layout_plate_keyboard_popup, null, false
        )
        layoutPlateKeyboardView = view.findViewById(R.id.layoutPlateKeyboardView)
        abcKeyboard = JDPlateABCKeyboardImpl(context, JDPlateABCKeyboardImpl.DEFAULT_XML_LAYOUT)
        provinceKeyboard =
            JDPlateProvinceKeyboardImpl(context, JDPlateProvinceKeyboardImpl.DEFAULT_XML_LAYOUT)
        abcKeyboard?.setOnKeyClickListener(object : JDPlateKeyClickListenerAble {

            override fun onInputKey(primaryCode: Int) {
                var preInputContent: String = etPlateInput?.text.toString()
                when (primaryCode) {
                    KEYCODE_DONE -> {
                        onHidePopupOfPlateKeyboard()
                        var result = tvPlateProvince?.text.toString()
                        result += preInputContent
                        Log.d("JDKeyboard", "result = $result")
                    }
                    KEYCODE_DELETE -> {
                        if (preInputContent.isNotEmpty()) {
                            preInputContent =
                                preInputContent.substring(0, preInputContent.length - 1)
                            onUpdatePlateInputContent(preInputContent)
                        }
                    }
                    else -> {
                        preInputContent += primaryCode.toChar().toString()
                        onUpdatePlateInputContent(preInputContent)
                    }
                }

            }

            override fun onChangePlateKeyboard(primaryCode: Int, inputProvince: Boolean) {
                onSwitchPlateKeyboard(true)
            }

            override fun onCancelKeyboard() {

            }

            override fun onClickFinish() {

            }

        })
        provinceKeyboard?.setOnKeyClickListener(object : JDPlateKeyClickListenerAble {

            override fun onInputKey(primaryCode: Int) {
                tvPlateProvince?.text = primaryCode.toChar().toString()
                onSwitchPlateKeyboard(false)
            }

            override fun onChangePlateKeyboard(primaryCode: Int, inputProvince: Boolean) {
                onSwitchPlateKeyboard(false)
            }

            override fun onCancelKeyboard() {
                onHidePopupOfPlateKeyboard()
            }

            override fun onClickFinish() {}

        })
        layoutPlateKeyboardView?.keyboard = abcKeyboard
        layoutPlateKeyboardView?.setOnKeyboardActionListener(abcKeyboard)

        popupPlateKeyboard = PopupWindow(
            view, LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT, true
        )
        popupPlateKeyboard?.isFocusable = true
        popupPlateKeyboard?.isTouchable = true
        popupPlateKeyboard?.isOutsideTouchable = true
        popupPlateKeyboard?.setBackgroundDrawable(BitmapDrawable())
    }

    /**
     * 展示车牌键盘
     */
    private fun onShowPopupOfPlateKeyboard(view: View, showProvinceKeyboard: Boolean) {
        onSwitchPlateKeyboard(showProvinceKeyboard)
        popupPlateKeyboard?.showAtLocation(view.parent as View, Gravity.BOTTOM, 0, 0)
    }

    /**
     * 隐藏车牌键盘
     */
    private fun onHidePopupOfPlateKeyboard() {
        if (null != popupPlateKeyboard && popupPlateKeyboard?.isShowing == true) {
            popupPlateKeyboard?.dismiss()
            popupPlateKeyboard
        }
    }

    /**
     * 切换ABC和省键盘
     */
    fun onSwitchPlateKeyboard(showProvinceKeyboard: Boolean) {
        if (showProvinceKeyboard) {
            layoutPlateKeyboardView?.keyboard = provinceKeyboard
            layoutPlateKeyboardView?.setOnKeyboardActionListener(provinceKeyboard)
        } else {
            layoutPlateKeyboardView?.keyboard = abcKeyboard
            layoutPlateKeyboardView?.setOnKeyboardActionListener(abcKeyboard)
        }
    }

    /**
     * 设置车牌号
     */
    fun onSetPlate(plateNo: String?) {

        if (null == plateNo || plateNo.isEmpty() || plateNo.length < 2) {
            return
        }
        tvPlateProvince?.text = plateNo[0].toString()
        val nextContent: String = plateNo.substring(1, plateNo.length)
        if (nextContent.length > maxInputLength) {
            maxInputLength = nextContent.length
        }
        onUpdatePlateInputContent(nextContent)
    }

    /**
     * 更新输入框
     */
    private fun onUpdatePlateInputContent(value: String) {
        var nextContent = value
        if (value.length > maxInputLength) {
            nextContent = value.substring(0, maxInputLength)
        }
        etPlateInput?.setText(nextContent)
        etPlateInput?.setSelection(nextContent.length)
    }

    /**
     * 隐藏系统键盘
     */
    private fun onDisableShowSoftInput(inputBox: EditText?) {
        if (null == inputBox) {
            return
        }
        if (Build.VERSION.SDK_INT <= 10) {
            inputBox.inputType = InputType.TYPE_NULL
        } else {
            val cls = EditText::class.java
            var method: Method
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType)
                method.isAccessible = true
                method.invoke(inputBox, false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                method = cls.getMethod("setSoftInputShownOnFocus", Boolean::class.javaPrimitiveType)
                method.isAccessible = true
                method.invoke(inputBox, false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}