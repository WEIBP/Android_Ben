package com.util.library.view.mpchart.interfaces.dataprovider;

import com.util.library.view.mpchart.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
