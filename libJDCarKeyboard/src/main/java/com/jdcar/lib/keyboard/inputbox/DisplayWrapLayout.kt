package com.jdcar.lib.keyboard.inputbox

import android.content.Context
import android.inputmethodservice.Keyboard
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.jdcar.lib.keyboard.R
import kotlin.properties.Delegates

/**
 * 所有展示输入框
 */
class DisplayWrapLayout : ConstraintLayout {

    private val displayTextViewList: ArrayList<DisplayTextView> = ArrayList()//存储TextView

    //光标位置
    var currentItemPosition: Int by Delegates.vetoable(0, { property, oldValue, newValue ->
        println("监听到属性变化：property->${property.name} oldValue->$oldValue newValue->$newValue")
        onUserActionListener?.onInputPositionChange(oldValue, newValue)

        //判断是否为新能源
        val isNewEnergy: Boolean = if (newValue == displayTextViewList.size - 1 && newValue != 0) {
            true
        } else {
            val lastTextView = displayTextViewList.lastOrNull()
            !(null == lastTextView || lastTextView.text.isNullOrEmpty()
                    || lastTextView.text == "|")
        }
        displayTextViewList.forEach {
            it.setIsNewEnergy(isNewEnergy)
            it.refreshCursor(newValue)
        }
        true
    })

    private var onUserActionListener: DisplayInputActionAble? = null


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initLayout()
    }

    private fun initLayout() {
        LayoutInflater.from(context)
            .inflate(R.layout.layout_license_plate_input_display, this, true)

        val tvPositionA = findViewById<DisplayTextView>(R.id.tvPositionA)
        val tvPositionB = findViewById<DisplayTextView>(R.id.tvPositionB)
        val tvPositionC = findViewById<DisplayTextView>(R.id.tvPositionC)
        val tvPositionD = findViewById<DisplayTextView>(R.id.tvPositionD)
        val tvPositionE = findViewById<DisplayTextView>(R.id.tvPositionE)
        val tvPositionF = findViewById<DisplayTextView>(R.id.tvPositionF)
        val tvPositionG = findViewById<DisplayTextView>(R.id.tvPositionG)
        val tvPositionH = findViewById<DisplayTextView>(R.id.tvPositionH)
        displayTextViewList.clear()
        displayTextViewList.add(tvPositionA)
        displayTextViewList.add(tvPositionB)
        displayTextViewList.add(tvPositionC)
        displayTextViewList.add(tvPositionD)
        displayTextViewList.add(tvPositionE)
        displayTextViewList.add(tvPositionF)
        displayTextViewList.add(tvPositionG)
        displayTextViewList.add(tvPositionH)
        onAddClickListener()
    }

    //初始化：设置DisplayTextView监听
    private fun onAddClickListener() {
        displayTextViewList.forEachIndexed { index, displayTextView ->
            displayTextView.tag = index
            displayTextView.setOnItemClickListener(object : DisplayTextView.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    currentItemPosition = position
                    onUserActionListener?.onClickInputBox(
                        position,
                        displayTextView.text.toString()
                    )
                }
            })
        }
    }

    /**
     * 点击：键盘，更新展示的输入内容
     */
    fun updateText(primaryCode: Int) {
        if (isArrayOutOfBounds()) {
            return
        }
        when (primaryCode) {
            Keyboard.KEYCODE_DELETE -> onClickKeyOfDelete()
            else -> onClickKeyOfInput(primaryCode)
        }

        //通知ABC键盘更新
        onUserActionListener?.onInputChange(getDisplayContent())
    }

    //点击：键盘输入
    private fun onClickKeyOfInput(primaryCode: Int) {
        displayTextViewList[currentItemPosition].setInputText(
            primaryCode.toChar().toString()
        )
        if (currentItemPosition in 0 until (displayTextViewList.size - 1)
            && !displayTextViewList[currentItemPosition].text.isNullOrEmpty()
            && displayTextViewList[currentItemPosition].text != "|"
        ) {
            currentItemPosition += 1
        }
    }

    //点击：键盘删除
    private fun onClickKeyOfDelete() {
        //判断当前输入框是否有值
        if ((displayTextViewList[currentItemPosition].text.isNullOrEmpty()
                    || displayTextViewList[currentItemPosition].text == "|")
            && currentItemPosition != 0
        ) {
            currentItemPosition -= 1
            displayTextViewList[currentItemPosition].setInputText("")
        } else {
            displayTextViewList[currentItemPosition].setInputText("")
        }

        //判断最后一位输入框是否有值
//        if (displayTextViewList[currentItemPosition].text.isNullOrEmpty()) {
//            mIsNewEnergy = false
//        }
    }

    /**
     * 直接设置展示的内容
     */
    fun setDisplayContent(value: String?) {
        if (TextUtils.isEmpty(value)) {

            displayTextViewList.forEach {
                it.setInputText("")
            }
            currentItemPosition = 0
            return
        }

        var displayValue = value
        if (value!!.length > displayTextViewList.size) {
            //新能源车牌，输入到最后一位，切换为普通车牌
            displayValue = value.substring(0, displayTextViewList.size)
        }

        if (displayValue!!.isEmpty()) {
            return
        }

        val cursorPosition = if (currentItemPosition >= displayTextViewList.size) {
            //新能源车牌，输入到最后一位，切换为普通车牌
            displayTextViewList.size - 1
        } else if (displayValue.length == currentItemPosition + 1
            && displayValue.length < displayTextViewList.size
        ) {
            //输入聚焦框移动到展示字符串的下一位 & 展示字符串的长度
            currentItemPosition + 1
        } else {
            currentItemPosition
        }

        displayTextViewList.forEachIndexed { index, displayTextView ->
            kotlin.run {
                val textContent: String = if (index < displayValue.length) {
                    displayValue[index].toString()
                } else {
                    ""
                }
                displayTextView.setInputText(textContent)
            }
        }
        currentItemPosition = cursorPosition
    }

    /**
     * 获取展示的内容
     */
    fun getDisplayContent(): String {
        val sBuilder = StringBuilder()
        displayTextViewList.forEach {
            if (it.text.isNotEmpty() && it.text != "|") {
                sBuilder.append(it.text)
            }
        }
        return sBuilder.toString()
    }

    /**
     * 避免光标超过输入框的个数
     */
    private fun isArrayOutOfBounds(): Boolean {
        if (currentItemPosition < 0 || currentItemPosition >= displayTextViewList.size) {
            return true
        }

        return false
    }

    fun setOnUserActionListener(onUserClickListener: DisplayInputActionAble) {
        this.onUserActionListener = onUserClickListener
    }
}