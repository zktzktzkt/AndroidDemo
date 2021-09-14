package com.jdcar.lib.keyboard.keys

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import androidx.annotation.IntegerRes

/**
 * 实现自定义键盘继承的类
 * 加载xml属性，记录按键属性
 */
abstract class JDAbstractKeyboard : Keyboard, KeyboardView.OnKeyboardActionListener {

    var context: Context? = null
    private var keyStyle: KeyStyleAble? = null

    constructor(context: Context, xmlLayoutResId: Int) : super(context, xmlLayoutResId) {
        this.context = context
        this.keyStyle = getDefaultKeyStyle(context)
    }

    constructor(context: Context, xmlLayoutResId: Int, modeId: Int) : super(
            context,
            xmlLayoutResId,
            modeId
    ) {
        this.context = context
        this.keyStyle = getDefaultKeyStyle(context)
    }

    constructor(
            context: Context,
            xmlLayoutResId: Int,
            modeId: Int,
            width: Int,
            height: Int
    ) : super(context, xmlLayoutResId, modeId, width, height) {
        this.context = context
        this.keyStyle = getDefaultKeyStyle(context)
    }

    constructor(
            context: Context,
            layoutTemplateResId: Int,
            characters: CharSequence,
            columns: Int,
            horizontalPadding: Int
    ) : super(context, layoutTemplateResId, characters, columns, horizontalPadding) {
        this.context = context
        this.keyStyle = getDefaultKeyStyle(context)
    }

    fun setKeyStyle(keyStyle: KeyStyleAble) {
        this.keyStyle = keyStyle
    }

    fun getKeyStyle(): KeyStyleAble? {
        if (null == keyStyle) {
            keyStyle = CommonKeyStyle(context!!)
        }
        return keyStyle
    }

    fun getKeyCode(@IntegerRes redId: Int): Int? {
        return context?.resources?.getInteger(redId)
    }

    override fun swipeRight() {

    }

    override fun onPress(primaryCode: Int) {

    }

    override fun onRelease(primaryCode: Int) {

    }

    override fun swipeLeft() {

    }

    override fun swipeUp() {

    }

    override fun swipeDown() {

    }

    override fun onText(text: CharSequence?) {

    }

    abstract fun getDefaultKeyStyle(context: Context): KeyStyleAble

    open class CommonKeyStyle : KeyStyleAble {
        private var context: Context? = null

        constructor(context: Context) {
            this.context = context
        }

        override fun getKeyBackound(key: Keyboard.Key): Drawable? {
            return key.iconPreview
        }

        override fun getKeyTextSize(key: Keyboard.Key): Float? {
            return null
        }

        override fun getKeyTextColor(key: Keyboard.Key): Int? {
            return Color.parseColor("#333333")
        }

        override fun getKeyLabel(key: Keyboard.Key): CharSequence? {
            return key.label
        }
    }
}