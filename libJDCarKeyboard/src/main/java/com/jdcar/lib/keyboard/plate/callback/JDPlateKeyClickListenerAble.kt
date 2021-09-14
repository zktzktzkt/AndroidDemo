package com.jdcar.lib.keyboard.plate.callback

/**
 * 车牌Keyboard点击监听
 */
interface JDPlateKeyClickListenerAble {
    /**
     * 输入keyCode
     */
    fun onInputKey(primaryCode: Int)

    /**
     * ABC和省键盘切换
     */
    fun onChangePlateKeyboard(primaryCode: Int, inputProvince: Boolean)

    /**
     * 省键盘：取消输入
     */
    fun onCancelKeyboard()

    /**
     * 点击"完成"
     */
    fun onClickFinish()
}