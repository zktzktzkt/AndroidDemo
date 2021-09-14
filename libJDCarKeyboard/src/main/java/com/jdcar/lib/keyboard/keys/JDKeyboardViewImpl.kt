package com.jdcar.lib.keyboard.keys

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import com.jdcar.lib.keyboard.utils.KeyboardUtils

/**
 * 渲染键盘，监听按键操作
 */
class JDKeyboardViewImpl : KeyboardView {

    private var rKeyBackground: Drawable? = null
    private var rLabelTextSize: Int = 0
    private var rKeyTextSize: Int = 0
    private var rKeyTextColor: Int = 0
    private var rShadowRadius: Float = 0.toFloat()
    private var rShadowColor: Int = 0

    private var rClipRegion: Rect? = null
    private var rInvalidatedKey: Keyboard.Key? = null

    constructor (context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor (context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        init()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        rKeyBackground = KeyboardUtils.getFieldValue(this, "mKeyBackground") as Drawable
        rLabelTextSize = KeyboardUtils.getFieldValue(this, "mLabelTextSize") as Int
        rKeyTextSize = KeyboardUtils.getFieldValue(this, "mKeyTextSize") as Int
        rKeyTextColor = KeyboardUtils.getFieldValue(this, "mKeyTextColor") as Int
        rShadowRadius = KeyboardUtils.getFieldValue(this, "mShadowRadius") as Float
        rShadowColor = KeyboardUtils.getFieldValue(this, "mShadowColor") as Int
    }

    override fun onDraw(canvas: Canvas?) {
        if (null == keyboard || keyboard !is JDAbstractKeyboard
                || null == (keyboard as JDAbstractKeyboard).getKeyStyle()
        ) {
            super.onDraw(canvas)
            return
        }
        rClipRegion = KeyboardUtils.getFieldValue(this, "mClipRegion") as Rect
        rInvalidatedKey = if (KeyboardUtils.getFieldValue(
                        this,
                        "mInvalidatedKey"
                ) == null
        ) null else KeyboardUtils.getFieldValue(this, "mInvalidatedKey") as Keyboard.Key
        super.onDraw(canvas)
        onRefreshKey(canvas)
    }

    /**
     * onRefreshKey是对父类的private void onBufferDraw()进行的重写. 只是在对key的绘制过程中进行了重新设置.
     *
     * @param canvas
     */
    private fun onRefreshKey(canvas: Canvas?) {
        val paint: Paint = KeyboardUtils.getFieldValue(this, "mPaint") as Paint
        val padding: Rect = KeyboardUtils.getFieldValue(this, "mPadding") as Rect

        paint.color = rKeyTextColor
        val kbdPaddingLeft = paddingLeft
        val kbdPaddingTop = paddingTop
        var keyBackground: Drawable?

        val clipRegion = rClipRegion
        val invalidKey = rInvalidatedKey
        var drawSingleKey = false
        if (invalidKey != null && clipRegion != null && canvas?.getClipBounds(clipRegion) == true) {
            // Is clipRegion completely contained within the invalidated key?
            if (invalidKey.x + kbdPaddingLeft - 1 <= clipRegion.left &&
                    invalidKey.y + kbdPaddingTop - 1 <= clipRegion.top &&
                    invalidKey.x + invalidKey.width + kbdPaddingLeft + 1 >= clipRegion.right &&
                    invalidKey.y + invalidKey.height + kbdPaddingTop + 1 >= clipRegion.bottom
            ) {
                drawSingleKey = true
            }
        }

        //拿到当前键盘被弹起的输入源 和 键盘为每个key的定制实现customKeyStyle
//        val etCur = (keyboard as BaseKeyboard).getEditText()
        val customKeyStyle = (keyboard as JDAbstractKeyboard).getKeyStyle()

        val keys = keyboard.keys
        //canvas.drawColor(0x00000000, PorterDuff.Mode.CLEAR);

        keys.forEach continuing@{
            if (drawSingleKey && invalidKey != it) return@continuing

            //获取为Key自定义的背景, 若没有定制, 使用KeyboardView的默认属性keyBackground设置
            keyBackground = customKeyStyle?.getKeyBackound(it)
            if (null == keyBackground) {
                keyBackground = rKeyBackground
            }

            val drawableState = it.currentDrawableState
            keyBackground?.state = drawableState

            //获取为Key自定义的Label, 若没有定制, 使用xml布局中指定的
            var keyLabel = customKeyStyle?.getKeyLabel(it)
            if (null == keyLabel) {
                keyLabel = it.label
            }

            // Switch the character to uppercase if shift is pressed
            val label = if (keyLabel == null) null else adjustCase(keyLabel).toString()

            val bounds = keyBackground?.bounds
            if (it.width != bounds?.right || it.height != bounds.bottom) {
                keyBackground?.setBounds(0, 0, it.width, it.height)
            }
            canvas?.translate((it.x + kbdPaddingLeft).toFloat(), (it.y + kbdPaddingTop).toFloat())
            keyBackground?.draw(canvas!!)

            if (label != null) {
                //获取为Key的Label的字体大小, 若没有定制, 使用KeyboardView的默认属性keyTextSize设置
                val customKeyTextSize = customKeyStyle?.getKeyTextSize(it)
                // For characters, use large font. For labels like "Done", use small font.
                if (null != customKeyTextSize) {
                    paint.textSize = customKeyTextSize
                    paint.typeface = Typeface.DEFAULT
                    //paint.setTypeface(Typeface.DEFAULT_BOLD)
                } else {
                    if (label.length > 1 && it.codes.size < 2) {
                        paint.textSize = rKeyTextSize.toFloat()
                        paint.typeface = Typeface.DEFAULT_BOLD
                    } else {
                        paint.textSize = rKeyTextSize.toFloat()
                        paint.typeface = Typeface.DEFAULT_BOLD
                    }
                }

                //获取为Key的Label的字体颜色, 若没有定制, 使用KeyboardView的默认属性keyTextColor设置
                val customKeyTextColor = customKeyStyle?.getKeyTextColor(it)
                if (null != customKeyTextColor) {
                    paint.color = customKeyTextColor
                } else {
                    paint.color = rKeyTextColor
                }
                // Draw a drop shadow for the text
                paint.setShadowLayer(rShadowRadius, 0f, 0f, rShadowColor)
                // Draw the text
                canvas?.drawText(
                        label,
                        ((it.width - padding.left - padding.right) / 2 + padding.left).toFloat(),
                        (it.height - padding.top - padding.bottom) / 2
                                + (paint.textSize - paint.descent()) / 2 + padding.top,
                        paint
                )
                // Turn off drop shadow
                paint.setShadowLayer(0f, 0f, 0f, 0)
            } else if (it.icon != null) {
                val drawableX = (it.width - padding.left - padding.right
                        - it.icon.intrinsicWidth) / 2 + padding.left
                val drawableY = (it.height - padding.top - padding.bottom
                        - it.icon.intrinsicHeight) / 2 + padding.top
                canvas?.translate(drawableX.toFloat(), drawableY.toFloat())
                it.icon.setBounds(
                        0, 0,
                        it.icon.intrinsicWidth, it.icon.intrinsicHeight
                )
                it.icon.draw(canvas!!)
                canvas.translate((-drawableX).toFloat(), (-drawableY).toFloat())
            }
            canvas?.translate((-it.x - kbdPaddingLeft).toFloat(), (-it.y - kbdPaddingTop).toFloat())
        }
        rInvalidatedKey = null
    }

    private fun adjustCase(label: CharSequence?): CharSequence? {
        var outLagbel = label
        if (keyboard.isShifted && label != null && label.length < 3
                && Character.isLowerCase(label[0])
        ) {
            outLagbel = label.toString().toUpperCase()
        }
        return outLagbel
    }

}