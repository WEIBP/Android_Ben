package com.util.library.view.mpchart.interfaces.dataprovider;

import com.util.library.view.mpchart.components.YAxis;
import com.util.library.view.mpchart.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
