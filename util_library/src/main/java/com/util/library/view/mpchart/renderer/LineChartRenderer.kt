package com.util.library.view.mpchart.renderer

import android.graphics.*
import com.blankj.utilcode.util.LogUtils
import com.util.library.view.mpchart.animation.ChartAnimator
import com.util.library.view.mpchart.charts.BarLineChartBase
import com.util.library.view.mpchart.charts.LineChart
import com.util.library.view.mpchart.data.Entry
import com.util.library.view.mpchart.data.LineDataSet
import com.util.library.view.mpchart.highlight.Highlight
import com.util.library.view.mpchart.interfaces.dataprovider.LineDataProvider
import com.util.library.view.mpchart.interfaces.datasets.IDataSet
import com.util.library.view.mpchart.interfaces.datasets.ILineDataSet
import com.util.library.view.mpchart.utils.*
import java.lang.ref.WeakReference
import java.util.*

class LineChartRenderer(var lineDataProvider: LineDataProvider, animator: ChartAnimator?,
                        viewPortHandler: ViewPortHandler?) : LineRadarRenderer(animator, viewPortHandler) {

    /**
     * paint for the inner circle of the value indicators
     */
    protected var mCirclePaintInner: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    protected var textPaint: Paint

    /**
     * Bitmap object used for drawing the paths (otherwise they are too long if
     * rendered directly on the canvas)
     */
    var mDrawBitmap: WeakReference<Bitmap?>? = null

    /**
     * on this canvas, the paths are rendered, it is initialized with the
     * pathBitmap
     */
    protected var mBitmapCanvas: Canvas? = null

    /**
     * the bitmap configuration to be used
     */
    protected var mBitmapConfig = Bitmap.Config.ARGB_8888
    protected var cubicPath = Path()
    protected var cubicFillPath = Path()
    override fun initBuffers() {}
    override fun drawData(c: Canvas) {
        val width = mViewPortHandler.chartWidth.toInt()
        val height = mViewPortHandler.chartHeight.toInt()
        var drawBitmap = if (mDrawBitmap == null) null else mDrawBitmap!!.get()
        if (drawBitmap == null || drawBitmap.width != width
                || drawBitmap.height != height) {
            if (width > 0 && height > 0) {
                drawBitmap = Bitmap.createBitmap(width, height, mBitmapConfig)
                mDrawBitmap = WeakReference(drawBitmap)
                mBitmapCanvas = Canvas(drawBitmap)
            } else return
        }
        drawBitmap!!.eraseColor(Color.TRANSPARENT)
        val lineData = lineDataProvider.lineData
        for (set in lineData.dataSets) {
            if (set.isVisible) drawDataSet(c, set)
        }
        c.drawBitmap(drawBitmap, 0f, 0f, mRenderPaint)
    }

    fun drawDataSet(c: Canvas?, dataSet: ILineDataSet) {
        if (dataSet.entryCount < 1) return
        mRenderPaint.strokeWidth = dataSet.lineWidth
        mRenderPaint.pathEffect = dataSet.dashPathEffect
        when (dataSet.mode) {
            LineDataSet.Mode.LINEAR, LineDataSet.Mode.STEPPED -> drawLinear(c, dataSet)
            LineDataSet.Mode.CUBIC_BEZIER -> drawCubicBezier(dataSet)
            LineDataSet.Mode.HORIZONTAL_BEZIER -> drawHorizontalBezier(dataSet)
            else -> drawLinear(c, dataSet)
        }
        mRenderPaint.pathEffect = null
    }

    fun drawHorizontalBezier(dataSet: ILineDataSet) {
        val phaseY = mAnimator.phaseY
        val trans = lineDataProvider.getTransformer(dataSet.axisDependency)
        mXBounds[lineDataProvider] = dataSet
        cubicPath.reset()
        if (mXBounds.range >= 1) {
            var prev = dataSet.getEntryForIndex(mXBounds.min)
            var cur = prev

            // let the spline start
            cubicPath.moveTo(cur.x, cur.y * phaseY)
            for (j in mXBounds.min + 1..mXBounds.range + mXBounds.min) {
                prev = cur
                cur = dataSet.getEntryForIndex(j)
                val cpx = (prev.x
                        + (cur.x - prev.x) / 2.0f)
                cubicPath.cubicTo(
                        cpx, prev.y * phaseY,
                        cpx, cur.y * phaseY,
                        cur.x, cur.y * phaseY)
            }
        }

        // if filled is enabled, close the path
        if (dataSet.isDrawFilledEnabled) {
            cubicFillPath.reset()
            cubicFillPath.addPath(cubicPath)
            // create a new path, this is bad for performance
            drawCubicFill(mBitmapCanvas, dataSet, cubicFillPath, trans, mXBounds)
        }
        mRenderPaint.color = dataSet.color
        mRenderPaint.style = Paint.Style.STROKE
        trans.pathValueToPixel(cubicPath)
        mBitmapCanvas!!.drawPath(cubicPath, mRenderPaint)
        mRenderPaint.pathEffect = null
    }

    protected fun drawCubicBezier(dataSet: ILineDataSet) {
        val phaseY = mAnimator.phaseY
        val trans = lineDataProvider.getTransformer(dataSet.axisDependency)
        mXBounds[lineDataProvider] = dataSet
        val intensity = dataSet.cubicIntensity
        cubicPath.reset()
        if (mXBounds.range >= 1) {
            var prevDx = 0f
            var prevDy = 0f
            var curDx = 0f
            var curDy = 0f

            // Take an extra point from the left, and an extra from the right.
            // That's because we need 4 points for a cubic bezier (cubic=4), otherwise we get lines moving and doing weird stuff on the edges of the chart.
            // So in the starting `prev` and `cur`, go -2, -1
            // And in the `lastIndex`, add +1
            val firstIndex = mXBounds.min + 1
            val lastIndex = mXBounds.min + mXBounds.range
            var prevPrev: Entry?
            var prev = dataSet.getEntryForIndex(Math.max(firstIndex - 2, 0))
            var cur = dataSet.getEntryForIndex(Math.max(firstIndex - 1, 0))
            var next = cur
            var nextIndex = -1
            if (cur == null) return

            // let the spline start
            cubicPath.moveTo(cur.x, cur.y * phaseY)
            for (j in mXBounds.min + 1..mXBounds.range + mXBounds.min) {
                prevPrev = prev
                prev = cur
                cur = if (nextIndex == j) next else dataSet.getEntryForIndex(j)
                nextIndex = if (j + 1 < dataSet.entryCount) j + 1 else j
                next = dataSet.getEntryForIndex(nextIndex)
                prevDx = (cur!!.x - prevPrev!!.x) * intensity
                prevDy = (cur.y - prevPrev.y) * intensity
                curDx = (next.x - prev!!.x) * intensity
                curDy = (next.y - prev.y) * intensity
                cubicPath.cubicTo(prev.x + prevDx, (prev.y + prevDy) * phaseY,
                        cur.x - curDx,
                        (cur.y - curDy) * phaseY, cur.x, cur.y * phaseY)
            }
        }

        // if filled is enabled, close the path
        if (dataSet.isDrawFilledEnabled) {
            cubicFillPath.reset()
            cubicFillPath.addPath(cubicPath)
            drawCubicFill(mBitmapCanvas, dataSet, cubicFillPath, trans, mXBounds)
        }
        mRenderPaint.color = dataSet.color
        mRenderPaint.style = Paint.Style.STROKE
        trans.pathValueToPixel(cubicPath)
        mBitmapCanvas!!.drawPath(cubicPath, mRenderPaint)
        mRenderPaint.pathEffect = null
    }

    protected fun drawCubicFill(c: Canvas?, dataSet: ILineDataSet, spline: Path, trans: Transformer, bounds: XBounds) {
        val fillMin = dataSet.fillFormatter
                .getFillLinePosition(dataSet, lineDataProvider)
        spline.lineTo(dataSet.getEntryForIndex(bounds.min + bounds.range).x, fillMin)
        spline.lineTo(dataSet.getEntryForIndex(bounds.min).x, fillMin)
        spline.close()
        trans.pathValueToPixel(spline)
        val drawable = dataSet.fillDrawable
        if (drawable != null) {
            drawFilledPath(c, spline, drawable)
        } else {
            drawFilledPath(c, spline, dataSet.fillColor, dataSet.fillAlpha)
        }
    }

    private var mLineBuffer = FloatArray(4)

    /**
     * Draws a normal line.
     *
     * @param c
     * @param dataSet
     */
    fun drawLinear(c: Canvas?, dataSet: ILineDataSet) {
        if ((dataSet as LineDataSet).isHidden) return
        val entryCount = dataSet.getEntryCount()
        val isDrawSteppedEnabled = dataSet.getMode() == LineDataSet.Mode.STEPPED
        val pointsPerEntryPair = if (isDrawSteppedEnabled) 4 else 2
        val trans = lineDataProvider.getTransformer(dataSet.getAxisDependency())
        val phaseY = mAnimator.phaseY
        mRenderPaint.style = Paint.Style.STROKE
        var canvas: Canvas? = null

        // if the data-set is dashed, draw on bitmap-canvas
        canvas = if (dataSet.isDashedLineEnabled()) {
            mBitmapCanvas
        } else {
            c
        }
        mXBounds[lineDataProvider] = dataSet

        // if drawing filled is enabled
        if (dataSet.isDrawFilledEnabled() && entryCount > 0) {
            drawLinearFill(c, dataSet, trans, mXBounds)
        }

        // more than 1 color
        if (dataSet.getColors().size > 1) {

            val j = setLineBuffer(dataSet)

            if (j > 0) {
                trans.pointValuesToPixel(mLineBuffer)

                val count  =   if ( dataSet.getMode() == LineDataSet.Mode.STEPPED ) 8 else 4

                if (mLineBuffer.size % count == 0) {
                    for( i in mLineBuffer.indices step count) {
                        mRenderPaint.color = dataSet.getColor(i/count)
                        if (dataSet.isDrawLineEnable) {
                            canvas!!.drawLines(mLineBuffer,i,count, mRenderPaint)
                        }
                    }
                }

            }

//            if (mLineBuffer.size <= pointsPerEntryPair * 2) mLineBuffer = FloatArray(pointsPerEntryPair * 4)
//            for (j in mXBounds.min..mXBounds.range + mXBounds.min) {
//                if (!dataSet.ignoredEntryIndex.isEmpty()
//                        && dataSet.ignoredEntryIndex.contains(j)) continue
//                var e: Entry? = dataSet.getEntryForIndex(j) ?: continue
//
//                mLineBuffer[0] = e!!.x
//                mLineBuffer[1] = e.y * phaseY
//                if (j < mXBounds.max) {
//                    e = dataSet.getEntryForIndex(j + 1)
//                    if (e == null) break
//                    if (isDrawSteppedEnabled) {
//                        mLineBuffer[2] = e.x
//                        mLineBuffer[3] = mLineBuffer[1]
//                        mLineBuffer[4] = mLineBuffer[2]
//                        mLineBuffer[5] = mLineBuffer[3]
//                        mLineBuffer[6] = e.x
//                        mLineBuffer[7] = e.y * phaseY
//                    } else {
//                        mLineBuffer[2] = e.x
//                        mLineBuffer[3] = e.y * phaseY
//                    }
//                } else {
//                    mLineBuffer[2] = mLineBuffer[0]
//                    mLineBuffer[3] = mLineBuffer[1]
//                }
//                trans.pointValuesToPixel(mLineBuffer)
//                if (!mViewPortHandler.isInBoundsRight(mLineBuffer[0])) break
//
//                // make sure the lines don't do shitty things outside
//                // bounds
//                if (!mViewPortHandler.isInBoundsLeft(mLineBuffer[2])
//                        || !mViewPortHandler.isInBoundsTop(mLineBuffer[1]) && !mViewPortHandler
//                                .isInBoundsBottom(mLineBuffer[3])) continue
//
//                // get the color that is set for this line-segment
//                mRenderPaint.color = dataSet.getColor(j)
//                if (dataSet.isDrawLineEnable) {
//                    canvas!!.drawLines(mLineBuffer, 0, pointsPerEntryPair * 2, mRenderPaint)
//                }
//            }
        } else { // only one color per dataset
//            if (mLineBuffer.size < Math.max(entryCount * pointsPerEntryPair, pointsPerEntryPair) * 2) {
//                mLineBuffer = FloatArray(Math.max(entryCount * pointsPerEntryPair, pointsPerEntryPair) * 4)
//            }

            val j = setLineBuffer(dataSet)

            if (j > 0) {
                trans.pointValuesToPixel(mLineBuffer)
                mRenderPaint.color = dataSet.getColor()
                if (dataSet.isDrawLineEnable) {
                    canvas!!.drawLines(mLineBuffer, mRenderPaint)
                }
            }
//            var e1: Entry?
//            var e2: Entry?
//            e1 = dataSet.getEntryForIndex(mXBounds.min)
//            if (e1 != null) {
//                var j = 0
//                for (x in mXBounds.min..mXBounds.range + mXBounds.min) {
//                    e1 = dataSet.getEntryForIndex(if (x == 0) 0 else x - 1)
//                    e2 = dataSet.getEntryForIndex(x)
//
////                    if (e1 == null || e2 == null ) continue;
//                    if (e1 == null || e2 == null || (!dataSet.ignoredEntryIndex.isEmpty()
//                                    && dataSet.ignoredEntryIndex.contains(x))) continue
//                    mLineBuffer[j++] = e1.x
//                    mLineBuffer[j++] = e1.y * phaseY
//                    if (isDrawSteppedEnabled) {
//                        mLineBuffer[j++] = e2.x
//                        mLineBuffer[j++] = e1.y * phaseY
//                        mLineBuffer[j++] = e2.x
//                        mLineBuffer[j++] = e1.y * phaseY
//                    }
//                    mLineBuffer[j++] = e2.x
//                    mLineBuffer[j++] = e2.y * phaseY
//                }
//                if (j > 0) {
//                    trans.pointValuesToPixel(mLineBuffer)
//                    val size = Math.max((mXBounds.range + 1) * pointsPerEntryPair, pointsPerEntryPair) * 2
//                    mRenderPaint.color = dataSet.getColor()
//                    if (dataSet.isDrawLineEnable) {
//                        canvas!!.drawLines(mLineBuffer, 0, size, mRenderPaint)
//                    }
//                }
//            }
        }
        mRenderPaint.pathEffect = null
    }

    private fun setLineBuffer(dataSet: ILineDataSet): Int {

        val bufSize = if (dataSet.mode == LineDataSet.Mode.STEPPED) {
            (dataSet.data.size -1 ) * 8
        } else {
            (dataSet.data.size -1 ) * 4
        }

        mLineBuffer = FloatArray(bufSize)
        val phaseY = mAnimator.phaseY
        var lastEntry: Entry? = null


        val xMax = (lineDataProvider as LineChart).rendererXAxis.maxValue
        val xMin = (lineDataProvider as LineChart).rendererXAxis.minValue

        val yRange  = (lineDataProvider as LineChart).yRange


        var j = 0
        dataSet.data.forEach { nowEntry ->
            lastEntry?.also { lastE ->
                if (dataSet is LineDataSet) {
                    if (dataSet.ignoredEntryIndex.isEmpty() || !dataSet.ignoredEntryIndex.contains(nowEntry.x)) {
                        // 只有在线的两个点的x同时在显示区域外时 不绘制
                        if (!((nowEntry.x > xMax && lastE.x > xMax) || (nowEntry.x < xMin && lastE.x < xMin)
                                        || (nowEntry.y > yRange.endInclusive && lastE.y > yRange.endInclusive)
                                        || (nowEntry.y < yRange.start && lastE.y < yRange.start))) {
                            mLineBuffer[j++] = lastE.x
                            mLineBuffer[j++] = lastE.y * phaseY
                            if (dataSet.getMode() == LineDataSet.Mode.STEPPED) {
                                mLineBuffer[j++] = nowEntry.x
                                mLineBuffer[j++] = lastE.y * phaseY
                                mLineBuffer[j++] = nowEntry.x
                                mLineBuffer[j++] = lastE.y * phaseY
                            }
                            mLineBuffer[j++] = nowEntry.x
                            mLineBuffer[j++] = nowEntry.y * phaseY
                        }
                    }
                }
            }
            lastEntry = nowEntry
        }

        if (j < mLineBuffer.size - 1) {
            mLineBuffer = mLineBuffer.copyOfRange(0, j)
        }
        return j
    }

    var mGenerateFilledPathBuffer = Path()

    /**
     * Draws a filled linear path on the canvas.
     *
     * @param c
     * @param dataSet
     * @param trans
     * @param bounds
     */
    protected fun drawLinearFill(c: Canvas?, dataSet: ILineDataSet, trans: Transformer, bounds: XBounds) {
        val filled = mGenerateFilledPathBuffer
        val startingIndex = bounds.min
        val endingIndex = bounds.range + bounds.min
        val indexInterval = 128
        var currentStartIndex = 0
        var currentEndIndex = indexInterval
        var iterations = 0

        // Doing this iteratively in order to avoid OutOfMemory errors that can happen on large bounds sets.
        do {
            currentStartIndex = startingIndex + iterations * indexInterval
            currentEndIndex = currentStartIndex + indexInterval
            currentEndIndex = if (currentEndIndex > endingIndex) endingIndex else currentEndIndex
            if (currentStartIndex <= currentEndIndex) {
                generateFilledPath(dataSet, currentStartIndex, currentEndIndex, filled)
                trans.pathValueToPixel(filled)
                val drawable = dataSet.fillDrawable
                if (drawable != null) {
                    drawFilledPath(c, filled, drawable)
                } else {
                    drawFilledPath(c, filled, dataSet.fillColor, dataSet.fillAlpha)
                }
            }
            iterations++
        } while (currentStartIndex <= currentEndIndex)
    }

    /**
     * Generates a path that is used for filled drawing.
     *
     * @param dataSet    The dataset from which to read the entries.
     * @param startIndex The index from which to start reading the dataset
     * @param endIndex   The index from which to stop reading the dataset
     * @param outputPath The path object that will be assigned the chart data.
     * @return
     */
    private fun generateFilledPath(dataSet: ILineDataSet, startIndex: Int, endIndex: Int, outputPath: Path) {
        val fillMin = dataSet.fillFormatter.getFillLinePosition(dataSet, lineDataProvider)
        val phaseY = mAnimator.phaseY
        val isDrawSteppedEnabled = dataSet.mode == LineDataSet.Mode.STEPPED
        outputPath.reset()
        val entry = dataSet.getEntryForIndex(startIndex)
        outputPath.moveTo(entry.x, fillMin)
        outputPath.lineTo(entry.x, entry.y * phaseY)

        // create a new path
        var currentEntry: Entry? = null
        var previousEntry = entry
        for (x in startIndex + 1..endIndex) {
            currentEntry = dataSet.getEntryForIndex(x)
            if (isDrawSteppedEnabled) {
                outputPath.lineTo(currentEntry.x, previousEntry!!.y * phaseY)
            }
            outputPath.lineTo(currentEntry.x, currentEntry.y * phaseY)
            previousEntry = currentEntry
        }

        // close up
        if (currentEntry != null) {
            outputPath.lineTo(currentEntry.x, fillMin)
        }
        outputPath.close()
    }

    override fun drawValues(c: Canvas) {
        if (isDrawingValuesAllowed(lineDataProvider)) {
            val dataSets = lineDataProvider.lineData.dataSets
            for (i in dataSets.indices) {
                val dataSet = dataSets[i]
                if (!shouldDrawValues(dataSet) || dataSet.entryCount < 1) continue

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet)
                val trans = lineDataProvider.getTransformer(dataSet.axisDependency)

                // make sure the values do not interfear with the circles
                var valOffset = (dataSet.circleRadius * 1.75f).toInt()
                if (!dataSet.isDrawCirclesEnabled) valOffset = valOffset / 2
                mXBounds[lineDataProvider] = dataSet
                val positions = trans.generateTransformedValuesLine(dataSet, mAnimator.phaseX, mAnimator
                        .phaseY, mXBounds.min, mXBounds.max)
                val formatter = dataSet.valueFormatter
                val iconsOffset = MPPointF.getInstance(dataSet.iconsOffset)
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x)
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y)
                var j = 0
                while (j < positions.size) {
                    val x = positions[j]
                    val y = positions[j + 1]
                    if (!mViewPortHandler.isInBoundsRight(x)) break
                    if (!mViewPortHandler.isInBoundsLeft(x) || !mViewPortHandler.isInBoundsY(y)) {
                        j += 2
                        continue
                    }
                    val entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min)
                    if (dataSet.isDrawValuesEnabled) {
                        drawValue(c, formatter.getPointLabel(entry), x, y - valOffset, dataSet.getValueTextColor(j / 2))
                    }
                    if (entry.icon != null && dataSet.isDrawIconsEnabled) {
                        val icon = entry.icon
                        Utils.drawImage(
                                c,
                                icon,
                                (x + iconsOffset.x).toInt(),
                                (y + iconsOffset.y).toInt(),
                                icon.intrinsicWidth,
                                icon.intrinsicHeight)
                    }
                    j += 2
                }
                MPPointF.recycleInstance(iconsOffset)
            }
        }
    }

    override fun drawValue(c: Canvas, valueText: String, x: Float, y: Float, color: Int) {
        mValuePaint.color = color
        c.drawText(valueText, x, y, mValuePaint)
    }

    override fun drawExtras(c: Canvas) {
        drawCircles(c)
    }

    /**
     * cache for the circle bitmaps of all datasets
     */
    private val mImageCaches = HashMap<IDataSet<*>, DataSetImageCache>()
    fun removeDataSetImageCaches(dataSet: IDataSet<*>) {
        mImageCaches.remove(dataSet)
    }

    /**
     * buffer for drawing the circles
     */
    private val mCirclesBuffer = FloatArray(2)
    private fun drawCircles(c: Canvas) {
        mRenderPaint.style = Paint.Style.FILL
        val phaseY = mAnimator.phaseY
        mCirclesBuffer[0] = 0f
        mCirclesBuffer[1] = 0f
        val dataSets = lineDataProvider.lineData.dataSets
        for (i in dataSets.indices) {
            val dataSet = dataSets[i]
            if (!dataSet.isVisible || !dataSet.isDrawCirclesEnabled || dataSet.entryCount == 0) continue
            mCirclePaintInner.color = dataSet.circleHoleColor
            val trans = lineDataProvider.getTransformer(dataSet.axisDependency)
            mXBounds[lineDataProvider] = dataSet
            val circleRadius = dataSet.circleRadius
            val circleHoleRadius = dataSet.circleHoleRadius
            val drawCircleHole = dataSet.isDrawCircleHoleEnabled && circleHoleRadius < circleRadius && circleHoleRadius > 0f
            val drawTransparentCircleHole = drawCircleHole &&
                    dataSet.circleHoleColor == ColorTemplate.COLOR_NONE
            var imageCache: DataSetImageCache?
            if (mImageCaches.containsKey(dataSet)) {
                imageCache = mImageCaches[dataSet]
            } else {
                imageCache = DataSetImageCache()
                mImageCaches[dataSet] = imageCache
            }
            val changeRequired = imageCache!!.init(dataSet)

            // only fill the cache with new bitmaps if a change is required
            if (changeRequired) {
                imageCache.fill(dataSet, drawCircleHole, drawTransparentCircleHole)
            }


            val xRange = (lineDataProvider as LineChart).rendererXAxis.let {
                it.minValue..it.maxValue
            }

            val yRange   = (lineDataProvider as LineChart).yRange

            dataSet.data.forEachIndexed { index, e ->
                if (e.x in xRange && e.y in yRange) {
                    mCirclesBuffer[0] = e.x
                    mCirclesBuffer[1] = e.y * phaseY
                    trans.pointValuesToPixel(mCirclesBuffer)

                    val circleBitmap = imageCache.getBitmap(index)
                    if (circleBitmap != null) {
                        c.drawBitmap(circleBitmap, mCirclesBuffer[0] - circleRadius, mCirclesBuffer[1] - circleRadius, null)
                    }
                }

            }


//            for (j in mXBounds.min..boundsRangeCount) {
//                val e = dataSet.getEntryForIndex(j) ?: break
//                mCirclesBuffer[0] = e.x
//                mCirclesBuffer[1] = e.y * phaseY
//                trans.pointValuesToPixel(mCirclesBuffer)
//                if (!mViewPortHandler.isInBoundsRight(mCirclesBuffer[0])) break
//                if (!mViewPortHandler.isInBoundsLeft(mCirclesBuffer[0]) ||
//                        !mViewPortHandler.isInBoundsY(mCirclesBuffer[1])) continue
//                val circleBitmap = imageCache.getBitmap(j)
//                if (circleBitmap != null) {
//                    c.drawBitmap(circleBitmap, mCirclesBuffer[0] - circleRadius, mCirclesBuffer[1] - circleRadius, null)
//                }
//            }
        }
    }

    override fun drawHighlighted(c: Canvas, indices: Array<Highlight>) {
        drawHighlighted(c, indices, false)
    }

    fun drawHighlighted(c: Canvas?, indices: Array<Highlight>, isDrawValues: Boolean) {
        val lineData = lineDataProvider.lineData
        for (high in indices) {
            val set = lineData.getDataSetByIndex(high.dataSetIndex)
            if (set == null || !set.isHighlightEnabled) continue
            val e = set.getEntryForXValue(high.x, high.y)
            if (!isInBoundsX(e, set)) continue
            val pix = lineDataProvider.getTransformer(set.axisDependency).getPixelForValues(e.x, e.y * mAnimator
                    .phaseY)
            high.setDraw(pix.x.toFloat(), pix.y.toFloat())

            // draw the lines
            if (isDrawValues) {
                drawHighlightLines(c, pix.x.toFloat(), pix.y.toFloat(), set, textPaint, e)
            } else {
                drawHighlightLines(c, pix.x.toFloat(), pix.y.toFloat(), set)
            }
        }
    }
    /**
     * Returns the Bitmap.Config that is used by this renderer.
     *
     * @return
     */
    /**
     * Sets the Bitmap.Config to be used by this renderer.
     * Default: Bitmap.Config.ARGB_8888
     * Use Bitmap.Config.ARGB_4444 to consume less memory.
     *
     * @param config
     */
    var bitmapConfig: Bitmap.Config
        get() = mBitmapConfig
        set(config) {
            mBitmapConfig = config
            releaseBitmap()
        }

    /**
     * Releases the drawing bitmap. This should be called when
     */
    fun releaseBitmap() {
        if (mBitmapCanvas != null) {
            mBitmapCanvas!!.setBitmap(null)
            mBitmapCanvas = null
        }
        if (mDrawBitmap != null) {
            val drawBitmap = mDrawBitmap!!.get()
            drawBitmap?.recycle()
            mDrawBitmap!!.clear()
            mDrawBitmap = null
        }
    }

    private inner class DataSetImageCache {
        private val mCirclePathBuffer = Path()
        private var circleBitmaps: Array<Bitmap?>? = null

        /**
         * Sets up the cache, returns true if a change of cache was required.
         *
         * @param set
         * @return
         */
        fun init(set: ILineDataSet): Boolean {
            val size = set.circleColorCount
            var changeRequired = false
            if (circleBitmaps == null) {
                circleBitmaps = arrayOfNulls(size)
                changeRequired = true
            } else if (circleBitmaps!!.size != size) {
                circleBitmaps = arrayOfNulls(size)
                changeRequired = true
            }
            return changeRequired
        }

        /**
         * Fills the cache with bitmaps for the given dataset.
         *
         * @param set
         * @param drawCircleHole
         * @param drawTransparentCircleHole
         */
        fun fill(set: ILineDataSet, drawCircleHole: Boolean, drawTransparentCircleHole: Boolean) {
            val colorCount = set.circleColorCount
            val circleRadius = set.circleRadius
            val circleHoleRadius = set.circleHoleRadius
            for (i in 0 until colorCount) {
                val conf = Bitmap.Config.ARGB_4444
                val circleBitmap = Bitmap.createBitmap((circleRadius * 2.1).toInt(), (circleRadius * 2.1).toInt(), conf)
                val canvas = Canvas(circleBitmap)
                circleBitmaps!![i] = circleBitmap
                mRenderPaint.color = set.getCircleColor(i)
                if (drawTransparentCircleHole) {
                    // Begin path for circle with hole
                    mCirclePathBuffer.reset()
                    mCirclePathBuffer.addCircle(
                            circleRadius,
                            circleRadius,
                            circleRadius,
                            Path.Direction.CW)

                    // Cut hole in path
                    mCirclePathBuffer.addCircle(
                            circleRadius,
                            circleRadius,
                            circleHoleRadius,
                            Path.Direction.CCW)

                    // Fill in-between
                    canvas.drawPath(mCirclePathBuffer, mRenderPaint)
                } else {
                    canvas.drawCircle(
                            circleRadius,
                            circleRadius,
                            circleRadius,
                            mRenderPaint)
                    if (drawCircleHole) {
                        canvas.drawCircle(
                                circleRadius,
                                circleRadius,
                                circleHoleRadius,
                                mCirclePaintInner)
                    }
                }
            }
        }

        /**
         * Returns the cached Bitmap at the given index.
         *
         * @param index
         * @return
         */
        fun getBitmap(index: Int): Bitmap? {
            return circleBitmaps!![index % circleBitmaps!!.size]
        }
    }

    init {
        mCirclePaintInner.style = Paint.Style.FILL
        mCirclePaintInner.color = Color.WHITE
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        textPaint.color = Color.GRAY
        textPaint.textSize = 22f
        textPaint.isAntiAlias = true
    }
}