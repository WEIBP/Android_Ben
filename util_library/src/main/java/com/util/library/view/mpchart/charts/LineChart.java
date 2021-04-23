
package com.util.library.view.mpchart.charts;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.LogUtils;
import com.util.library.R;
import com.util.library.view.mpchart.components.LimitLine;
import com.util.library.view.mpchart.data.LineData;
import com.util.library.view.mpchart.interfaces.dataprovider.LineDataProvider;
import com.util.library.view.mpchart.interfaces.datasets.IDataSet;
import com.util.library.view.mpchart.listener.ChartTouchListener;
import com.util.library.view.mpchart.listener.OnChartGestureListener;
import com.util.library.view.mpchart.renderer.LineChartRenderer;

import org.jetbrains.annotations.NotNull;

import kotlin.ranges.ClosedFloatingPointRange;

/**
 * Chart that draws lines, surfaces, circles, ...
 *
 * @author Philipp Jahoda
 */
public class LineChart extends BarLineChartBase<LineData> implements LineDataProvider {

    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    @Override
    protected void init() {
        super.init();
        mRenderer = new LineChartRenderer(this, mAnimator, mViewPortHandler);

    }





    public void removeDataSetImageCaches(IDataSet dataSet) {
        if (mRenderer != null && mRenderer instanceof LineChartRenderer) {
            ((LineChartRenderer) mRenderer).removeDataSetImageCaches(dataSet);
        }
    }

    @Override
    public LineData getLineData() {
        return mData;
    }

    @Override
    protected void onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if (mRenderer != null && mRenderer instanceof LineChartRenderer) {
            ((LineChartRenderer) mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }



}
