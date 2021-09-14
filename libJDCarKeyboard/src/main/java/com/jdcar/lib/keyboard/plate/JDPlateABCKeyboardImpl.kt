package com.jdcar.lib.keyboard.plate

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import com.jdcar.lib.keyboard.R
import com.jdcar.lib.keyboard.keys.JDAbstractKeyboard
import com.jdcar.lib.keyboard.keys.KeyStyleAble
import com.jdcar.lib.keyboard.plate.callback.JDPlateKeyClickListenerAble

/**
 * ABC键盘
 */
class JDPlateABCKeyboardImpl : JDAbstractKeyboard {

    companion object {
        val DEFAULT_XML_LAYOUT = R.xml.default_plate_abc_keyboard
    }

    //输入框聚焦的位置，用于判断港澳学警领是否可以点击
    private var mFocusPosition: Int = 0

    //已输入的字符串，用于判断是否可以点击完成
    private var mInputText: String? = null

    private var onKeyClickListener: JDPlateKeyClickListenerAble? = null

    constructor(context: Context, xmlLayoutResId: Int) : super(context, xmlLayoutResId)

    /**
     * 按键点击监听
     * 删除和完成操作的判断，在展示的控件里面
     */
    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        if (primaryCode == PlateConstants.Plate_KeyCode_I || primaryCode == PlateConstants.Plate_KeyCode_O) {
            //I 、 O
            return
        } else if ((mFocusPosition != 6) && (primaryCode == PlateConstants.Plate_KeyCode_Embassy
                    || primaryCode == PlateConstants.Plate_KeyCode_HongKong
                    || primaryCode == PlateConstants.Plate_KeyCode_Macau
                    || primaryCode == PlateConstants.Plate_KeyCode_Coach
                    || primaryCode == PlateConstants.Plate_KeyCode_Police
                    || primaryCode == PlateConstants.Plate_KeyCode_Consulate)
        ) {
            //使、港、澳、学、警、领
            return
        } else if ((mFocusPosition == 1) && (primaryCode in 48..57)) {
            //数字0-9
            return
        } else if (primaryCode == KEYCODE_MODE_CHANGE) {
            //切换键盘
            onKeyClickListener?.onChangePlateKeyboard(primaryCode, false)
        } else if (primaryCode == KEYCODE_DONE) {
            if (TextUtils.isEmpty(mInputText) || mInputText!!.length < 7) {
                return
            }
            //点击完成
            onKeyClickListener?.onClickFinish()
        } else {
            onKeyClickListener?.onInputKey(primaryCode)
        }

        Log.d("JDKeyboard", "onKey()#primaryCode = $primaryCode,keyCodes = $keyCodes")
    }

    /**
     * 按键样式
     */
    override fun getDefaultKeyStyle(context: Context): KeyStyleAble {
        return object : KeyStyleAble {
            override fun getKeyBackound(key: Key): Drawable? {
                if (key.label == PlateConstants.Plate_KeyLabel_I || key.label == PlateConstants.Plate_KeyLabel_O) {
                    return context.resources?.getDrawable(R.drawable.default_key_common_disable)
                }
                return key.iconPreview
            }

            override fun getKeyTextSize(key: Key): Float? {
                return null
            }

            override fun getKeyTextColor(key: Key): Int {
//                const val Plate_KeyLabel_HongKong: String = "港"
//                const val Plate_KeyLabel_Macau: String = "澳"
//                const val Plate_KeyLabel_Coach: String = "学"
//                const val Plate_KeyLabel_Police: String = "警"
//                const val Plate_KeyLabel_Consulate: String = "领"
                if (key.label == PlateConstants.Plate_KeyLabel_I || key.label == PlateConstants.Plate_KeyLabel_O) {
                    return Color.parseColor("#929292")
                } else if (key.label == PlateConstants.Plate_KeyLabel_Finish) {
                    return Color.parseColor("#FFFFFF")
                }
                return Color.parseColor("#333333")
            }

            override fun getKeyLabel(key: Key): CharSequence? {
                return key.label
            }

        }
    }

    /**
     * 更新输入框聚焦的位置
     */
    fun onUpdatePositionOfInputBoxFocus(position: Int) {
        mFocusPosition = position
    }

    /**
     * 更新输入的字符串
     */
    fun onUpdateInputText(content: String?) {
        mInputText = content
    }

    fun setOnKeyClickListener(onKeyClickListener: JDPlateKeyClickListenerAble) {
        this.onKeyClickListener = onKeyClickListener
    }

}