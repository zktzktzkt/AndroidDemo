package com.jdcar.lib.keyboard.utils

import android.content.Context
import android.text.TextUtils

object KeyboardUtils{

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun sp2px(context: Context, spValue: Float): Float {
        val fontScale: Float =
            context.resources.displayMetrics.scaledDensity
        return spValue * fontScale + 0.5f
    }

    @JvmStatic
    fun getFieldValue(obj: Any?, fieldName: String): Any? {
        if (obj == null || TextUtils.isEmpty(fieldName)) {
            return null
        }

        var clazz: Class<*> = obj.javaClass
        while (clazz != Any::class.java) {
            try {
                val field = clazz.getDeclaredField(fieldName)
                field.isAccessible = true
                return field.get(obj)
            } catch (e: Exception) {
            }

            clazz = clazz.superclass
        }
        return null
    }
}