package com.jdcar.lib.keyboard.plate

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import com.jdcar.lib.keyboard.R
import com.jdcar.lib.keyboard.keys.JDAbstractKeyboard
import com.jdcar.lib.keyboard.keys.KeyStyleAble
import com.jdcar.lib.keyboard.plate.callback.JDPlateKeyClickListenerAble

/**
 * 车牌省键盘
 */
class JDPlateProvinceKeyboardImpl : JDAbstractKeyboard {

    companion object {
        val DEFAULT_XML_LAYOUT = R.xml.default_plate_province_keyboard
    }

    private var onKeyClickListener: JDPlateKeyClickListenerAble? = null

    constructor(context: Context, xmlLayoutResId: Int) : super(context, xmlLayoutResId)

    /**
     * 按键点击监听
     * 删除和完成操作的判断，在展示的控件里面
     */
    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {

        if (primaryCode == KEYCODE_MODE_CHANGE) {
            onKeyClickListener?.onChangePlateKeyboard(primaryCode, true)
        } else if (primaryCode == KEYCODE_CANCEL) {
            onKeyClickListener?.onCancelKeyboard()
        } else {
            onKeyClickListener?.onInputKey(primaryCode)
        }

        Log.d("JDKeyboard", "onKey()#primaryCode = $primaryCode,keyCodes = $keyCodes")
    }

    override fun getDefaultKeyStyle(context: Context): KeyStyleAble {
        return object : KeyStyleAble {
            override fun getKeyBackound(key: Key): Drawable? {
                return key.iconPreview
            }

            override fun getKeyTextSize(key: Key): Float? {
                return null
            }

            override fun getKeyTextColor(key: Key): Int {
                if (key.label == PlateConstants.Plate_KeyLabel_ABC || key.label == PlateConstants.Plate_KeyLabel_Cancel) {
                    return Color.parseColor("#FFFFFF")
                }
                return Color.parseColor("#333333")
            }

            override fun getKeyLabel(key: Key): CharSequence? {
                return key.label
            }

        }
    }

    fun setOnKeyClickListener(onKeyClickListener: JDPlateKeyClickListenerAble) {
        this.onKeyClickListener = onKeyClickListener
    }

}