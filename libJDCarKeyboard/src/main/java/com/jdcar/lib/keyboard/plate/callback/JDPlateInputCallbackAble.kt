package com.jdcar.lib.keyboard.plate.callback

/**
 * 车牌手动输入键
 */
interface JDPlateInputCallbackAble {
    /**
     * 输入完成
     */
    fun onInputFinish(result: String?)

    /**
     * 输入取消
     */
    fun onInputCancel()

}