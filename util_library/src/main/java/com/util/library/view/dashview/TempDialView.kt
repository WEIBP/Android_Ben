package com.util.library.view.dashview

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Paint.Style.STROKE
import android.graphics.Path
import android.graphics.Shader.TileMode.REPEAT
import android.util.AttributeSet
import android.view.View
import com.util.library.common.ColorUtil.Companion.rgb

/**
 * author: Ben
 * created on: 2020/5/19 9:57
 * description:
 */
class TempDialView : View {
    private var mLinearGradient: LinearGradient? = null
    private var minValue = -20f
    private var maxValue = 100f
    private var currentValue = 0f
    private var mWidth = 300
    private var mHeight = 100
    private var mLineWidth = mHeight shr 2
    private var longScaleLength = mHeight shr 2
    private var shortScaleLength = mHeight shr 3
    private val scaleWidth = 2
    private val colorLeft = rgb("#D2E839")
    private val colorRight = rgb("#C70101")
    private var mPaint: Paint? = null
    private var mPath: Path? = null
    private var pointerPaint: Paint? = null
    private var valuePain  = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context?) : super(context) {}
    constructor(
        context: Context?,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        mPaint = Paint()
        mPath = Path()
        pointerPaint = Paint()

    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var tempWidth: Int
        var tempHeigh: Int

        if (widthMode == MeasureSpec.EXACTLY) {
            tempWidth = widthSize
        } else {
            tempWidth = paddingLeft + mWidth + paddingRight
            if (widthMode == MeasureSpec.AT_MOST) {
                tempWidth = mWidth.coerceAtMost(widthSize)
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            tempHeigh = heightSize
        } else {
            tempHeigh = paddingTop + mHeight + paddingBottom
            if (heightMode == MeasureSpec.AT_MOST) {
                tempHeigh = mHeight.coerceAtMost(heightSize)
            }
        }

        setMeasuredDimension(tempWidth, tempHeigh)

        mWidth = tempWidth - paddingLeft - paddingRight
        mHeight = tempHeigh - paddingTop - paddingBottom

        mLineWidth = mHeight shr 2
        longScaleLength = mHeight shr 2
        shortScaleLength = mHeight shr 3

        //设置圆形指标的属性
        valuePain.style = STROKE
        valuePain.color = rgb("#FF7F00")
        valuePain.strokeWidth = 20f

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint!!.strokeWidth = mLineWidth.toFloat()

        if (mLinearGradient == null) {

            mLinearGradient = LinearGradient(
                    -1f, 0f, mWidth.toFloat(), 0f, intArrayOf(colorLeft, colorRight), null, REPEAT
            )

            mPaint!!.shader = mLinearGradient
        }

        val lineY = (mHeight shr 1).toFloat()

        canvas.drawLine(
                0f, lineY, mWidth.toFloat(), lineY, mPaint!!
        )


        mPaint!!.strokeWidth = scaleWidth.toFloat()
        val space = mWidth.toFloat() / 50
        val topBaseLine = mHeight shr 2
        val bottomBaseLine = mHeight * 3 shr 2

        for (i in 0..50) {

            var x = i * space
            if (i == 0) {
                x += space / 16
            }

            if (i == 50) {
                x -= space / 16
            }

            if (i % 5 == 0) {
                canvas.drawLine(x, topBaseLine.toFloat(), x, topBaseLine - longScaleLength.toFloat(), mPaint!!)
                canvas.drawLine(x, bottomBaseLine.toFloat(), x, bottomBaseLine + longScaleLength.toFloat(), mPaint!!)
            } else {
                canvas.drawLine(x, topBaseLine.toFloat(), x, topBaseLine - shortScaleLength.toFloat(), mPaint!!)
                canvas.drawLine(x, bottomBaseLine.toFloat(), x, bottomBaseLine + shortScaleLength.toFloat(), mPaint!!)
            }
        }

        drawCircleValue(canvas)
    }

    private fun drawCircleValue(canvas: Canvas) {
            canvas.drawCircle(mWidth* (currentValue- minValue)/(maxValue-minValue),mHeight/2f,mHeight/8f,valuePain)
    }

    fun setMinValue(minValue: Float) {
        this.minValue = minValue
    }

    fun setMaxValue(maxValue: Float) {
        this.maxValue = maxValue
    }

    fun setCurrentValue(value: Float) {
        currentValue = value
        if (currentValue >= maxValue) {
            currentValue = maxValue
        }
        if (currentValue <= minValue) {
            currentValue = minValue
        }
        invalidate()
    }
}