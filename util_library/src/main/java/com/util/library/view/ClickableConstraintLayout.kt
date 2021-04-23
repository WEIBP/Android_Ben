package com.util.library.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout

class ClickableConstraintLayout: ConstraintLayout {

    constructor(context: Context): super(context){

    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){

    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr){

    }


    var isChildClickAble = true

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (!isChildClickAble) {
            return true
        }
        return super.onInterceptTouchEvent(ev)
    }
}