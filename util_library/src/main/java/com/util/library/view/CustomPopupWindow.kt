package com.util.library.view

import android.app.Activity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.blankj.utilcode.util.LogUtils

class CustomPopupWindow(val context: Activity) : PopupWindow() {


    override fun dismiss() {
        super.dismiss()
        setBackgroundAlpha(1f)
    }

    override fun showAsDropDown(anchor: View?) {
        super.showAsDropDown(anchor)
        setBackgroundAlpha(0.5f)
    }

    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int) {
        super.showAsDropDown(anchor, xoff, yoff)
        setBackgroundAlpha(0.5f)
    }

    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int, gravity: Int) {
        super.showAsDropDown(anchor, xoff, yoff, gravity)
        setBackgroundAlpha(0.5f)
    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        super.showAtLocation(parent, gravity, x, y)
        setBackgroundAlpha(0.5f)
    }

    private fun setBackgroundAlpha(f: Float) {
        val lp: WindowManager.LayoutParams = context.window.attributes
        lp.alpha = f
        context.window.attributes = lp
    }

}