package com.util.library.common.spinkit

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatButton
import java.lang.Integer.min

class SpinkitButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatButton(context, attrs, defStyleAttr)  {

    private var isMorphing = false
    private var startAngle = 0
    private lateinit var paint: Paint
    private var arcValueAnimator: ValueAnimator? = null

    lateinit var tempText : String

    init {
        init(context, attrs)
    }


    private fun init(context: Context, attrs: AttributeSet?) {

        isMorphing = false

        paint = Paint().apply {
            color = Color.WHITE
            strokeWidth = 4f
            style = Paint.Style.STROKE
            textSize = 2f
            isAntiAlias = true
        }
    }

    fun startAnim() {
        isMorphing = true
        tempText = text.toString()
        text = ""
        //画中间的白色圆圈
        showArc()
    }

    fun stopAnim() {
        arcValueAnimator?.cancel()
        isMorphing = false
        text = tempText
    }

    private fun showArc() {
        arcValueAnimator  =  ValueAnimator.ofInt(0, 1080).apply {
              addUpdateListener { animation ->
                  startAngle = animation.animatedValue as Int
                  invalidate()
              }
              interpolator = LinearInterpolator()
              repeatCount = ValueAnimator.INFINITE
              duration = 3000
              start()
          }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isMorphing) {
             (Math.min(width,height)*0.6f/2f).apply{
                 canvas.drawArc(RectF((width/2 - this), (height/2 - this),
                         (width/2 + this), (height/2 + this)),
                         startAngle.toFloat(), 270f, false, paint)
             }

        }
    }
}