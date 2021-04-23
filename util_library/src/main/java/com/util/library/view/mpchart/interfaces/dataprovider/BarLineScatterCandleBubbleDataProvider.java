package com.util.library.view.mpchart.interfaces.dataprovider;

import com.util.library.view.mpchart.components.YAxis.AxisDependency;
import com.util.library.view.mpchart.data.BarLineScatterCandleBubbleData;
import com.util.library.view.mpchart.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
