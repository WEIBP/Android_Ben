package com.util.library.view.dashview

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.blankj.utilcode.util.LogUtils
import com.util.library.R
import kotlin.math.roundToLong


/**
 * author: Ben
 * created on: 2020/5/18 10:26
 * description:
 */
class DashBoardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var colorDialLower = 0
    private var colorDialMiddle = 0
    private var colorDialHigh = 0
    private var textSizeDial = 0
    private var strokeWidthDial = 0
    private var titleDial: String? = null
    private var titleDialSize = 0
    private var titleDialColor = 0
    private var valueTextSize = 0
    private var animPlayTime = 0
    private var radiusDial = 0
    private var mRealRadius = 0
    private var currentValue = 0f
    private var arcPaint: Paint? = null
    private var mRect: RectF? = null
    private var pointerPaint: Paint? = null
    private var fontMetrics: Paint.FontMetrics? = null
    private var titlePaint: Paint? = null
    private var pointerPath: Path? = null
    private var arcRoundRange = 270
    private var maxValue = 100f
    private var minValue = 0f
    private var showValueText = true
    private var showTitleText = true
    private var showScale = true
    private var longScaleLength = 8
    private var shortScaleLength = 3
    private var offsetX = 0
    private var offsetY = 0
    fun setLongScaleLength(longScaleLength: Int) {
        this.longScaleLength = longScaleLength
    }

    fun setShortScaleLength(shortScaleLength: Int) {
        this.shortScaleLength = shortScaleLength
    }

    fun setArcRoundRange(arcRoundRange: Int) {
        var arcRoundRange = arcRoundRange
        if (arcRoundRange > 300) {
            arcRoundRange = 300
        }
        if (arcRoundRange < 160) {
            arcRoundRange = 160
        }
        fitX()
        this.arcRoundRange = arcRoundRange
    }

    private fun fitX() {
        if (offsetX > 0) {
            if (!showValueText && arcRoundRange <= 180) {
                radiusDial = ((measuredHeight - paddingTop - paddingBottom) * 0.75).toInt()
                mRealRadius = radiusDial - strokeWidthDial / 2
            } else if (!showValueText && arcRoundRange <= 225) {
                radiusDial = ((measuredHeight - paddingTop - paddingBottom) * 0.65).toInt()
                mRealRadius = radiusDial - strokeWidthDial / 2
            }
            mRect!![-mRealRadius.toFloat(), -mRealRadius.toFloat(), mRealRadius.toFloat()] = mRealRadius.toFloat()
            offsetX = (measuredWidth - paddingLeft - paddingRight) / 2 - mRealRadius
        }
    }

    fun setShowScale(showScale: Boolean) {
        this.showScale = showScale
    }


    fun setShowValueText(showValueText: Boolean) {
        this.showValueText = showValueText
    }

    fun setShowTitleText(showTitleText: Boolean) {
        this.showTitleText = showTitleText
    }

    fun setAnimPlayTime(animPlayTime: Int) {
        this.animPlayTime = animPlayTime
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        @SuppressLint("Recycle") val attributes = context.obtainStyledAttributes(attrs, R.styleable.DashBoardView)

        // 表盘圆弧高中低颜色
        colorDialLower = attributes.getColor(R.styleable.DashBoardView_color_dial_lower, DEFAULT_COLOR_LOWER)
        colorDialMiddle = attributes.getColor(R.styleable.DashBoardView_color_dial_middle, DEFAULT_COLOR_MIDDLE)
        colorDialHigh = attributes.getColor(R.styleable.DashBoardView_color_dial_high, DEFAULT_COLOR_HIGH)

        //表盘刻度字体大小
        textSizeDial = attributes.getDimension(R.styleable.DashBoardView_text_size_dial, sp2px(DEFAULT_TEXT_SIZE_DIAL).toFloat()).toInt()
        //表盘外圈宽度
        strokeWidthDial = attributes.getDimension(R.styleable.DashBoardView_stroke_width_dial, dp2px(DEFAULT_STROKE_WIDTH).toFloat()).toInt()
        // 半径或直径
        radiusDial = attributes.getDimension(R.styleable.DashBoardView_radius_circle_dial, dp2px(DEFAULT_RADIUS_DIAL).toFloat()).toInt()

        //标题属性
        titleDial = attributes.getString(R.styleable.DashBoardView_text_title_dial)
        titleDialSize = attributes.getDimension(R.styleable.DashBoardView_text_title_size, dp2px(DEAFAULT_TITLE_SIZE).toFloat()).toInt()
        titleDialColor = attributes.getColor(R.styleable.DashBoardView_text_title_color, DEAFAULT_COLOR_TITLE)
        valueTextSize = attributes.getDimension(R.styleable.DashBoardView_text_size_value, dp2px(DEFAULT_VALUE_SIZE).toFloat()).toInt()
        //动画时间
        animPlayTime = attributes.getInt(R.styleable.DashBoardView_animator_play_time, DEFAULT_ANIM_PLAY_TIME)
        arcRoundRange = attributes.getInt(R.styleable.DashBoardView_arc_rount_range, arcRoundRange)
        maxValue = attributes.getFloat(R.styleable.DashBoardView_max_value, maxValue)
        minValue = attributes.getFloat(R.styleable.DashBoardView_min_value, minValue)
        longScaleLength = attributes.getInt(R.styleable.DashBoardView_long_scale_length, longScaleLength)
        shortScaleLength = attributes.getInt(R.styleable.DashBoardView_short_scale_length, shortScaleLength)
        showScale = attributes.getBoolean(R.styleable.DashBoardView_show_scale_text, true)
        showTitleText = attributes.getBoolean(R.styleable.DashBoardView_show_title_text, true)
        showValueText = attributes.getBoolean(R.styleable.DashBoardView_show_value_text, true)
    }

    private fun initPaint() {
        arcPaint = Paint()
        arcPaint!!.isAntiAlias = true
        arcPaint!!.style = Paint.Style.STROKE
        arcPaint!!.strokeWidth = strokeWidthDial.toFloat()
        pointerPaint = Paint()
        pointerPaint!!.isAntiAlias = true
        pointerPaint!!.textSize = textSizeDial.toFloat()
        pointerPaint!!.textAlign = Paint.Align.CENTER
        fontMetrics = pointerPaint!!.fontMetrics
        titlePaint = Paint()
        titlePaint?.isFilterBitmap = true
        titlePaint!!.isAntiAlias = true
        titlePaint!!.textAlign = Paint.Align.CENTER
        titlePaint!!.isFakeBoldText = true
        pointerPath = Path()
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var mWidth: Int
        var mHeight: Int
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize
        } else {
            mWidth = paddingLeft + radiusDial * 2 + paddingRight
            if (widthMode == MeasureSpec.AT_MOST) {
                mWidth = Math.min(mWidth, widthSize)
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize
        } else {
            mHeight = paddingTop + radiusDial * 2 + paddingBottom
            if (heightMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(mHeight, heightSize)
            }
        }
        setMeasuredDimension(mWidth, mHeight)
        radiusDial = Math.min(measuredWidth - paddingLeft - paddingRight,
                measuredHeight - paddingTop - paddingBottom) / 2
        mRealRadius = radiusDial - strokeWidthDial / 2
        if (measuredWidth - paddingLeft - paddingRight -
                (measuredHeight - paddingTop - paddingBottom) > 0) {
            offsetX = (measuredWidth - paddingLeft - paddingRight) / 2 - mRealRadius
        } else {
            offsetY = (measuredHeight - paddingTop - paddingBottom) / 2 - mRealRadius
        }
        mRect = RectF((-mRealRadius).toFloat(), (-mRealRadius).toFloat(), mRealRadius.toFloat(), mRealRadius.toFloat())
        fitX()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawArc(canvas)
        drawPointerLine(canvas)
        drawTitleDial(canvas)
        drawPointer(canvas)
    }

    private fun drawArc(canvas: Canvas) {
        canvas.translate(offsetX + radiusDial.toFloat(), offsetY + radiusDial.toFloat())
        arcPaint!!.color = colorDialLower
        canvas.drawArc(mRect!!, 270 - 0.5f * arcRoundRange, 0.2f * arcRoundRange, false, arcPaint!!)
        arcPaint!!.color = colorDialMiddle
        canvas.drawArc(mRect!!, 270 - 0.3f * arcRoundRange, 0.6f * arcRoundRange, false, arcPaint!!)
        arcPaint!!.color = colorDialHigh
        canvas.drawArc(mRect!!, 270 + 0.3f * arcRoundRange, 0.2f * arcRoundRange, false, arcPaint!!)
    }

    private fun drawPointerLine(canvas: Canvas) {
        //设置起始点的角度
        canvas.rotate(270 - 0.5f * arcRoundRange)
        for (i in 0..100) {     //一共需要绘制101个表针
            if (i <= 20) {
                pointerPaint!!.color = colorDialLower
            } else if (i <= 80) {
                pointerPaint!!.color = colorDialMiddle
            } else {
                pointerPaint!!.color = colorDialHigh
            }
            if (i % 10 == 0) {     //长表针
                pointerPaint!!.strokeWidth = 6f
                canvas.drawLine(radiusDial.toFloat(), 0f, radiusDial - strokeWidthDial - dp2px(longScaleLength).toFloat(), 0f, pointerPaint!!)
                if (showScale) {
                    drawPointerText(canvas, i)
                }
            } else {    //短表针
                pointerPaint!!.strokeWidth = 3f
                canvas.drawLine(radiusDial.toFloat(), 0f, radiusDial - strokeWidthDial - dp2px(shortScaleLength).toFloat(), 0f, pointerPaint!!)
            }
            canvas.rotate(arcRoundRange * 0.01f)
        }
    }

    private fun drawPointerText(canvas: Canvas, i: Int) {
        canvas.save()
        val currentCenterX = (radiusDial - strokeWidthDial - dp2px(longScaleLength + 4) - pointerPaint!!.measureText(i.toString()) / 2).toInt()
        canvas.translate(currentCenterX.toFloat(), 0f)
        canvas.rotate(90 + 0.5f * arcRoundRange - arcRoundRange * 0.01f * i) //坐标系总旋转角度为360度
        val textBaseLine = (0 + (fontMetrics!!.bottom - fontMetrics!!.top) / 2 - fontMetrics!!.bottom).toInt()
        val value = minValue + (maxValue - minValue) * 0.01 * i
        canvas.drawText(String.format("%.1f", value), 0f, textBaseLine.toFloat(), pointerPaint!!)
        canvas.restore()
    }

    private fun drawTitleDial(canvas: Canvas) {
        //上一步绘刻度，坐标系旋转到终点->第101个刻度的角度
        canvas.rotate(90 - 0.51f * arcRoundRange) //恢复坐标系为起始中心位置
        if (showTitleText && !TextUtils.isEmpty(titleDial)) {
            titlePaint!!.color = titleDialColor
            titlePaint!!.textSize = titleDialSize.toFloat()
            canvas.drawText(titleDial!!, 0f, -radiusDial / 3.toFloat(), titlePaint!!)
        }
        when {
            currentValue <= minValue + 0.2 * (maxValue - minValue) -> {
                titlePaint!!.color = colorDialLower
            }
            currentValue <= minValue + 0.8 * (maxValue - minValue) -> {
                titlePaint!!.color = colorDialMiddle
            }
            else -> {
                titlePaint!!.color = colorDialHigh
            }
        }
        if (showValueText) {
            titlePaint!!.textSize = valueTextSize.toFloat()
            canvas.drawText(currentValue.toString(), 0f, radiusDial * 2 / 3.toFloat(), titlePaint!!)
        }
    }

    private fun drawPointer(canvas: Canvas) {

        val currentDegree = when {
            currentValue >= maxValue -> (arcRoundRange + 270 - 0.5 * arcRoundRange).toInt()
            currentValue <= minValue -> (270 - 0.5 * arcRoundRange).toInt()
            else -> ((currentValue - minValue) / (maxValue - minValue) * arcRoundRange + 270 - 0.5 * arcRoundRange).toInt()
        }

        canvas.rotate(currentDegree.toFloat())
        pointerPath!!.moveTo(radiusDial - strokeWidthDial - dp2px(18).toFloat(), 0f)
        pointerPath!!.lineTo(0f, -dp2px(5).toFloat())
        pointerPath!!.lineTo(-dp2px(12).toFloat(), 0f)
        pointerPath!!.lineTo(0f, dp2px(5).toFloat())
        pointerPath!!.close()
        canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        canvas.drawPath(pointerPath!!, titlePaint!!)
    }

    fun setValue(value: Float) {
        currentValue = value
        if (value < minValue) {
            currentValue = minValue
        }
        if (value > maxValue) {
            currentValue = maxValue
        }
        if (animPlayTime <= 0) {
            invalidate()
        } else {
            val animator = ValueAnimator.ofFloat(0f, value)
            // TODO: 2020/7/22  范围修改后 动画效果计算存在问题
            animator.addUpdateListener { animation ->
                currentValue = (animation.animatedValue as Float * 100).roundToLong().toFloat() / 100
                invalidate()
            }
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.duration = animPlayTime.toLong()
            animator.start()
        }
    }

    protected fun dp2px(dpVal: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal.toFloat(), resources.displayMetrics).toInt()
    }

    protected fun sp2px(spVal: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal.toFloat(), resources.displayMetrics).toInt()
    }

    fun setMaxAndMin(max: Float, min: Float) {
        if (max > min) {
            this.maxValue = max
            this.minValue = min
        }
    }

    companion object {
        private val DEFAULT_COLOR_LOWER = Color.parseColor("#1d953f")
        private val DEFAULT_COLOR_MIDDLE = Color.parseColor("#228fbd")
        private const val DEFAULT_COLOR_HIGH = Color.RED
        private const val DEAFAULT_COLOR_TITLE = Color.BLACK
        private const val DEFAULT_TEXT_SIZE_DIAL = 11
        private const val DEFAULT_STROKE_WIDTH = 8
        private const val DEFAULT_RADIUS_DIAL = 100
        private const val DEAFAULT_TITLE_SIZE = 16
        private const val DEFAULT_VALUE_SIZE = 28
        private const val DEFAULT_ANIM_PLAY_TIME = 2000
    }

    init {
        initAttrs(context, attrs)
        initPaint()
    }
}