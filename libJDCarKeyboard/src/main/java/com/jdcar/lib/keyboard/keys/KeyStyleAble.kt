package com.jdcar.lib.keyboard.keys

import android.graphics.drawable.Drawable
import android.inputmethodservice.Keyboard

/**
 * 按键样式
 */
interface KeyStyleAble {
    fun getKeyBackound(key: Keyboard.Key): Drawable?

    fun getKeyTextSize(key: Keyboard.Key): Float?

    fun getKeyTextColor(key: Keyboard.Key): Int?

    fun getKeyLabel(key: Keyboard.Key): CharSequence?
}