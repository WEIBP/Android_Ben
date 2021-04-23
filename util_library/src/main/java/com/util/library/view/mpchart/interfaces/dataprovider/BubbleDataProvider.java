package com.util.library.view.mpchart.interfaces.dataprovider;

import com.util.library.view.mpchart.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
