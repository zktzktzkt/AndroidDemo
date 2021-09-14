package com.jdcar.lib.keyboard.plate.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.jdcar.lib.keyboard.R
import com.jdcar.lib.keyboard.inputbox.DisplayInputActionAble
import com.jdcar.lib.keyboard.inputbox.DisplayWrapLayout
import com.jdcar.lib.keyboard.keys.JDKeyboardViewImpl
import com.jdcar.lib.keyboard.plate.JDPlateABCKeyboardImpl
import com.jdcar.lib.keyboard.plate.JDPlateProvinceKeyboardImpl
import com.jdcar.lib.keyboard.plate.callback.JDPlateInputCallbackAble
import com.jdcar.lib.keyboard.plate.callback.JDPlateKeyClickListenerAble

/**
 * 全屏车牌手动输入
 */
class JDPlateKeyboardFullScreenView : ConstraintLayout {
    private var displayInputLayout: DisplayWrapLayout? = null
    private var plateKeyboardLayout: JDKeyboardViewImpl? = null

    private var abcKeyboard: JDPlateABCKeyboardImpl? = null
    private var provinceKeyboard: JDPlateProvinceKeyboardImpl? = null

    private var inputCallback: JDPlateInputCallbackAble? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.layout_plate_keyboard_fullscreen, this, true)
    }

    /**
     * 在所有的控件都从xml文件中加载完成后调用
     */
    override fun onFinishInflate() {
        super.onFinishInflate()

        findViewById<View>(R.id.viewBgClose).setOnClickListener {
            inputCallback?.onInputCancel()
        }
        initPlateKeyboard()
    }

    private fun initPlateKeyboard() {

        displayInputLayout = findViewById(R.id.topDisplayContainer)
        plateKeyboardLayout = findViewById(R.id.bottomKeyboardContainer)

        displayInputLayout?.setOnUserActionListener(object : DisplayInputActionAble {

            override fun onClickInputBox(position: Int, displayContent: String?) {
                onSetPlateKeyboardVisibility(View.VISIBLE)
            }

            override fun onInputPositionChange(oldPos: Int, newPos: Int) {
                if (newPos == 0 && oldPos != newPos) {
                    onSwitchPlateKeyboard(true)
                } else if (newPos > 0 && oldPos <= 0) {
                    onSwitchPlateKeyboard(false)
                }
                abcKeyboard?.onUpdatePositionOfInputBoxFocus(newPos)
            }

            override fun onInputChange(result: String?) {
                abcKeyboard?.onUpdateInputText(result)
            }

        })

        abcKeyboard = JDPlateABCKeyboardImpl(context, JDPlateABCKeyboardImpl.DEFAULT_XML_LAYOUT)
        provinceKeyboard =
            JDPlateProvinceKeyboardImpl(context, JDPlateProvinceKeyboardImpl.DEFAULT_XML_LAYOUT)
        abcKeyboard?.setOnKeyClickListener(object : JDPlateKeyClickListenerAble {

            override fun onInputKey(primaryCode: Int) {
                displayInputLayout?.updateText(primaryCode)
            }

            override fun onChangePlateKeyboard(primaryCode: Int, inputProvince: Boolean) {
                displayInputLayout?.currentItemPosition = 0
            }

            override fun onCancelKeyboard() {
            }

            override fun onClickFinish() {
                onSetPlateKeyboardVisibility(View.GONE)
                displayInputLayout?.currentItemPosition = -1
                inputCallback?.onInputFinish(displayInputLayout?.getDisplayContent())
            }

        })
        provinceKeyboard?.setOnKeyClickListener(object : JDPlateKeyClickListenerAble {

            override fun onInputKey(primaryCode: Int) {
                displayInputLayout?.updateText(primaryCode)
            }

            override fun onChangePlateKeyboard(primaryCode: Int, inputProvince: Boolean) {
                onSwitchPlateKeyboard(false)
            }

            override fun onCancelKeyboard() {
                onSetPlateKeyboardVisibility(View.GONE)
                displayInputLayout?.currentItemPosition = -1
            }

            override fun onClickFinish() {

            }

        })

        plateKeyboardLayout = findViewById(R.id.bottomKeyboardContainer)
        plateKeyboardLayout?.keyboard = provinceKeyboard
        plateKeyboardLayout?.setOnKeyboardActionListener(provinceKeyboard)

        //默认取消焦点
        displayInputLayout?.currentItemPosition = -1
    }

    /**
     * 切换ABC和省键盘
     */
    fun onSwitchPlateKeyboard(showProvinceKeyboard: Boolean) {
        if (showProvinceKeyboard) {
            plateKeyboardLayout?.keyboard = provinceKeyboard
            plateKeyboardLayout?.setOnKeyboardActionListener(provinceKeyboard)
        } else {
            plateKeyboardLayout?.keyboard = abcKeyboard
            plateKeyboardLayout?.setOnKeyboardActionListener(abcKeyboard)
        }
    }

    /**
     * 控制键盘展示隐藏
     */
    fun onSetPlateKeyboardVisibility(newVisibility: Int) {
        if (plateKeyboardLayout?.visibility == newVisibility) {
            return
        }
        plateKeyboardLayout?.visibility = newVisibility
    }

    /**
     * 手动输入车牌回调
     */
    fun onSetInputCallback(listener: JDPlateInputCallbackAble) {
        this.inputCallback = listener
    }

    /**
     * 车牌输入展示框获取焦点
     */
    fun onFocusPlateInputBox() {
        displayInputLayout?.currentItemPosition = 0
    }
}