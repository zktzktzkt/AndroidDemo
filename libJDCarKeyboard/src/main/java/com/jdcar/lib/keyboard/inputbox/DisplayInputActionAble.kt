package com.jdcar.lib.keyboard.inputbox

/**
 * 输入展示框的行为接口
 */
interface DisplayInputActionAble {
    /**
     * 点击展示框，用于显示键盘
     */
    fun onClickInputBox(position: Int, displayContent: String?)

    /**
     * 输入框焦点改变监听，用于判断港、澳、学、警、领、完成、数字是否可以点击
     */
    fun onInputPositionChange(oldPos: Int, newPos: Int)

    /**
     * 已输入的字符串监听，用于判断是否可以点击完成
     */
    fun onInputChange(result:String?)
}