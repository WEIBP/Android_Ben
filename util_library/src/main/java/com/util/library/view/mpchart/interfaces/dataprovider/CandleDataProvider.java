package com.util.library.view.mpchart.interfaces.dataprovider;

import com.util.library.view.mpchart.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
