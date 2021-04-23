package com.util.library.view.mpchart.data

import android.graphics.RectF
import com.blankj.utilcode.util.LogUtils


class LineAreaRectBean(private val contentRect: RectF) {

    init {
        // TODO: 2020/8/13    1 拖动范围的问题，不能拖出chart范围  2 点和拖动范围的判定

    }

    var startX = 0f
    var startY = 0f
    var endX = 0f
    var endY = 0f
    var isShow = false
    var offsetX = 0f
    var offsetY = 0f

    //判断当前是否绘制
    fun isDrawArea(): Boolean {
        return startX != endX && startY != endY
    }

    fun getStartXWithOffset(): Float {
        return startX + offsetX
    }

    fun getStartYWithOffset(): Float {
        return startY + offsetY
    }

    fun getEndXWithOffset(): Float {
        return endX + offsetX
    }

    fun getEndYWithOffset(): Float {
        return endY + offsetY
    }


    fun setAreaRect(x: Float, y: Float, x1: Float, y1: Float, b: Boolean) {
        clearOffset()
        startX = x
        startY = y
        endX = x1
        endY = y1
        isShow = b
    }

    private fun clearOffset() {
        offsetX = 0f
        offsetY = 0f
    }


    fun checkActionDownInDrawArea(x: Float, y: Float): Boolean {
        return isShow && x in (minOf(startX, endX)..maxOf(startX, endX))
                && y in (minOf(startY, endY)..maxOf(startY, endY))
    }

    fun translationArea(x: Float, y: Float) {

        //判定是否超出边界
        offsetX = when {
            x + startX < contentRect.left -> contentRect.left - startX
            x + startX > contentRect.right -> contentRect.right - startX
            x + endX < contentRect.left -> contentRect.left - endX
            x + endX > contentRect.right -> contentRect.right - endX
            else -> x
        }


        offsetY = when {
            y + startY < contentRect.top -> contentRect.top - startY
            y + startY > contentRect.bottom -> contentRect.bottom - startY
            y + endY < contentRect.top -> contentRect.top - endY
            y + endY > contentRect.bottom -> contentRect.bottom - endY
            else -> y
        }

    }


    fun resetArea() {
        startX += offsetX
        startY += offsetY
        endX += offsetX
        endY += offsetY
        clearOffset()
    }

    override fun toString(): String {
        return "LineAreaRectBean: startX: $startX startY:  $startY endX: $endX endY: $endY offsetX: $offsetX offsetY: $offsetY "
    }
}