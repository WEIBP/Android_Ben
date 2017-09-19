package com.ben.ben;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

public class TestActivity extends Activity {


        protected String[] mParties = new String[] {
                "治安", "交通", "消防", "备情", "Party E", "Party F", "Party G", "Party H",
                "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
                "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
                "Party Y", "Party Z"
        };

        protected Typeface mTfRegular;
        protected Typeface mTfLight;

        PieChart mChart;

        BarChart mBarChart;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_test);
                initView();
        }

        private void initView() {
                mChart = (PieChart) findViewById(R.id.chart1);
                mBarChart = (BarChart) findViewById(R.id.chart2);
                setchart1();
                setchart2();
        }


        private void setchart2() {

                mBarChart.setDrawBarShadow(false);
                mBarChart.setDrawValueAboveBar(true);

                mBarChart.getDescription().setEnabled(false);

                // if more than 60 entries are displayed in the chart, no values will be
                // drawn
                mBarChart.setMaxVisibleValueCount(60);

                // scaling can now only be done on x- and y-axis separately
                mBarChart.setPinchZoom(false);

                mBarChart.setDrawGridBackground(false);
                // mBarChart.setDrawYLabels(false);


//        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mBarChart);

                XAxis xAxis = mBarChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTypeface(mTfLight);
                xAxis.setDrawGridLines(false);
                xAxis.setGranularity(1f); // only intervals of 1 day
                xAxis.setLabelCount(7);

                xAxis.setTextColor(Color.WHITE);
                xAxis.setTextSize(13f);
//        xAxis.setValueFormatter(xAxisFormatter);

//        IAxisValueFormatter custom = new MyAxisValueFormatter();

                YAxis leftAxis = mBarChart.getAxisLeft();
                leftAxis.setTypeface(mTfLight);
                leftAxis.setLabelCount(8, false);
//        leftAxis.setValueFormatter(custom);
                leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                leftAxis.setSpaceTop(15f);
                leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                leftAxis.setTextColor(Color.WHITE);
                leftAxis.setTextSize(13f);
                YAxis rightAxis = mBarChart.getAxisRight();
                rightAxis.setDrawGridLines(false);
                rightAxis.setTypeface(mTfLight);
                rightAxis.setLabelCount(8, false);
//        rightAxis.setValueFormatter(custom);
                rightAxis.setSpaceTop(15f);
                rightAxis.setTextColor(Color.WHITE);
                rightAxis.setTextSize(13f);
                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                Legend l = mBarChart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setDrawInside(false);
                l.setForm(Legend.LegendForm.SQUARE);
                l.setFormSize(9f);
                l.setTextColor(Color.WHITE);
                l.setTextSize(13f);
                l.setXEntrySpace(4f);
                // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
                // "def", "ghj", "ikl", "mno" });
                // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
                // "def", "ghj", "ikl", "mno" });

//        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
//        mv.setChartView(mBarChart); // For bounds control
//        mBarChart.setMarker(mv); // Set the marker to the chart

                setData2(12, 50);
        }

        private void setchart1() {

                mChart.setUsePercentValues(true);
                mChart.getDescription().setEnabled(false);
                mChart.setExtraOffsets(5, 10, 5, 5);

                mChart.setDragDecelerationFrictionCoef(0.95f);

                mChart.setCenterTextTypeface(mTfLight);

                mChart.setDrawHoleEnabled(true);
                mChart.setHoleColor(Color.WHITE);

                mChart.setTransparentCircleColor(Color.WHITE);
                mChart.setTransparentCircleAlpha(110);

                mChart.setHoleRadius(58f);
                mChart.setDrawHoleEnabled(false);
                mChart.setTransparentCircleRadius(61f);

                mChart.setDrawCenterText(true);

                mChart.setRotationAngle(0);
                // enable rotation of the chart by touch
                mChart.setRotationEnabled(true);
                mChart.setHighlightPerTapEnabled(true);

                // mChart.setUnit(" €");
                // mChart.setDrawUnitsInChart(true);

                // add a selection listener

                setData(4, 100);

                mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                // mChart.spin(2000, 0, 360);


                Legend l = mChart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.VERTICAL);
                l.setDrawInside(false);
                l.setTextColor(Color.WHITE);
                l.setTextSize(13f);
                l.setXEntrySpace(7f);
                l.setYEntrySpace(0f);
                l.setYOffset(0f);

                // entry label styling
                mChart.setEntryLabelColor(Color.WHITE);
                mChart.setEntryLabelTypeface(mTfRegular);
                mChart.setEntryLabelTextSize(15f);
        }

        private void setData(int count, float range) {

                float mult = range;

                ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

                // NOTE: The order of the entries when being added to the entries array determines their position around the center of
                // the chart.
                for (int i = 0; i < count ; i++) {
                        entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5),
                                mParties[i % mParties.length],
                                getResources().getDrawable(R.mipmap.baozhang)));
                }

                PieDataSet dataSet = new PieDataSet(entries, "公安");

                dataSet.setDrawIcons(false);

                dataSet.setSliceSpace(3f);
                dataSet.setIconsOffset(new MPPointF(0, 40));
                dataSet.setSelectionShift(5f);

                // add a lot of colors

                ArrayList<Integer> colors = new ArrayList<Integer>();

                for (int c : ColorTemplate.VORDIPLOM_COLORS)
                        colors.add(c);

                for (int c : ColorTemplate.JOYFUL_COLORS)
                        colors.add(c);

                for (int c : ColorTemplate.COLORFUL_COLORS)
                        colors.add(c);

                for (int c : ColorTemplate.LIBERTY_COLORS)
                        colors.add(c);

                for (int c : ColorTemplate.PASTEL_COLORS)
                        colors.add(c);

                colors.add(ColorTemplate.getHoloBlue());

                dataSet.setColors(colors);
                //dataSet.setSelectionShift(0f);

                PieData data = new PieData(dataSet);
                data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(15f);
                data.setValueTextColor(Color.WHITE);
                data.setValueTypeface(mTfLight);
                mChart.setData(data);

                // undo all highlights
                mChart.highlightValues(null);

                mChart.invalidate();
        }

        private void setData2(int count, float range) {

                float groupSpace = 0.08f;
                float barSpace = 0.03f; // x4 DataSet
                float barWidth = 0.2f; // x4 DataSet
                // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

                int groupCount = 3;
                int startYear = 1980;
                int endYear = startYear + groupCount;


                ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
                ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
                ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
                ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();

                float randomMultiplier = 30 * 100f;

                for (int i = startYear; i < endYear; i++) {
                        yVals1.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
                        yVals2.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
                        yVals3.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
                        yVals4.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
                }

                BarDataSet set1, set2, set3, set4;

                if (mBarChart.getData() != null && mBarChart.getData().getDataSetCount() > 0) {

                        set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
                        set2 = (BarDataSet) mBarChart.getData().getDataSetByIndex(1);
                        set3 = (BarDataSet) mBarChart.getData().getDataSetByIndex(2);
                        set4 = (BarDataSet) mBarChart.getData().getDataSetByIndex(3);
                        set1.setValues(yVals1);
                        set2.setValues(yVals2);
                        set3.setValues(yVals3);
                        set4.setValues(yVals4);
                        mBarChart.getData().notifyDataChanged();
                        mBarChart.notifyDataSetChanged();

                } else {
                        // create 4 DataSets
                        set1 = new BarDataSet(yVals1, "核心");
                        set1.setColor(Color.rgb(104, 241, 175));
                        set2 = new BarDataSet(yVals2, "警戒");
                        set2.setColor(Color.rgb(164, 228, 251));
                        set3 = new BarDataSet(yVals3, "控制");
                        set3.setColor(Color.rgb(242, 247, 158));
                        set4 = new BarDataSet(yVals4, "防核生化");
                        set4.setColor(Color.rgb(255, 102, 0));

                        BarData data = new BarData(set1, set2, set3, set4);

                        data.setValueFormatter(new LargeValueFormatter());
                        data.setValueTypeface(mTfLight);
                        data.setValueTextColor(Color.WHITE);
                        data.setValueTextSize(13f);
                        mBarChart.setData(data);
                }

                // specify the width each bar should have
                mBarChart.getBarData().setBarWidth(barWidth);

                // restrict the x-axis range
                mBarChart.getXAxis().setAxisMinimum(startYear);

                // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
                mBarChart.getXAxis().setAxisMaximum(startYear + mBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
                mBarChart.groupBars(startYear, groupSpace, barSpace);
                mBarChart.invalidate();
        }
}
