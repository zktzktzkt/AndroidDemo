package demo.zkttestdemo.effect.chart;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.Utils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;

public class BarChartManager {

    private BarChart mBarChart;

    // 左边的Y轴
    private YAxis leftAxis;
    // 右边的Y轴
    private YAxis rightAxis;
    //X轴
    private XAxis xAxis;

    public BarChartManager(BarChart barChart) {
        this.mBarChart = barChart;
        leftAxis = mBarChart.getAxisLeft();
        rightAxis = mBarChart.getAxisRight();
        xAxis = mBarChart.getXAxis();
    }


    private void initLineChart() {
        mBarChart.setDrawValueAboveBar(false);
        mBarChart.getDescription().setEnabled(false);
        //背景颜色
        //mBarChart.setBackground(SupportApp.drawable(R.drawable.shape_out_in));
        //网格
        mBarChart.setDrawGridBackground(false);
        //背景阴影
        mBarChart.setDrawBarShadow(false);
        //
        mBarChart.setHighlightFullBarEnabled(false);
        //手势缩放
        mBarChart.setHighlightPerDragEnabled(true);
        //点击某一柱后高亮
        mBarChart.setHighlightPerTapEnabled(true);
        //显示边界
        mBarChart.setDrawBorders(false);
        mBarChart.setPadding(1, 1, 1, 1);
        //设置动画效果
        mBarChart.animateY(1000, Easing.Linear);
        mBarChart.animateX(1000, Easing.Linear);
        mBarChart.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

        /*折线图例 标签 设置*/
        Legend legend = mBarChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(11f);
        legend.setTextColor(Color.BLACK);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(32);
        legend.setTextSize(12);

        // 设置X轴
        xAxis.setTextSize(10);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setGranularityEnabled(true);
        //xAxis.setGranularity(0.9f);
        xAxis.setCenterAxisLabels(true);

        // 左边的Y轴
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextSize(12);
        leftAxis.setTextColor(Color.BLACK);

        // 不显示右边的Y轴
        rightAxis.setEnabled(false);

        mBarChart.notifyDataSetChanged();
    }


    /**
     * 展示柱状图(一条)
     */
    public void showBarChart(List<Float> xAxisValues, List<Float> yAxisValues, String label, int color) {

        initLineChart();

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < yAxisValues.size(); i++) {
            entries.add(new BarEntry(xAxisValues.get(i), yAxisValues.get(i)));
        }

        // 每一个BarDataSet代表一类柱状图
        BarDataSet barDataSet = new BarDataSet(entries, label);
        barDataSet.setColor(color);
        barDataSet.setValueTextSize(9f);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarData data = new BarData(barDataSet);
        data.setDrawValues(false);
        data.setBarWidth(0.5f);
        data.setValueTextColor(ContextCompat.getColor(Utils.getApp(), R.color.white));

        //设置X轴的刻度数

        xAxis.setLabelCount(xAxisValues.size() - 1, false);

        mBarChart.setData(data);
        mBarChart.invalidate();
    }


    /**
     * 展示柱状图(多条)
     */
    public void showBarChart(List<Float> xAxisValues, List<List<Float>> yAxisValues,
                             List<String> labels, List<Integer> colours, List<String> values) {

        initLineChart();

        BarData data = new BarData();

        for (int i = 0; i < yAxisValues.size(); i++) {

            ArrayList<BarEntry> entries = new ArrayList<>();

            for (int j = 0; j < yAxisValues.get(i).size(); j++) {
                entries.add(new BarEntry(xAxisValues.get(j), yAxisValues.get(i).get(j)));
            }

            BarDataSet barDataSet = new BarDataSet(entries, labels.get(i));
            barDataSet.setColor(colours.get(i));
            barDataSet.setValueTextColor(colours.get(i));
            barDataSet.setValueTextSize(10f);
            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            barDataSet.setDrawValues(false);

            data.addDataSet(barDataSet);
        }

        int amount = yAxisValues.size();

        //float groupSpace = 0.5f; //柱状图组之间的间距
        //float barSpace   = (float) ((1 - 0.12) / amount / 20); // x4 DataSet
        //float barWidth   = (float) ((1 - 0.12) / amount / 10 * 4); // x4 DataSet

        float barSpace   = (float) ((1 - 0.12) / amount / 10);
        float barWidth   = (float) (barSpace * 3);
        float groupSpace = 1 - (barWidth * 2 + barSpace * 2); //柱状图组之间的间距

        xAxis.setLabelCount(xAxisValues.size() - 1, false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new XAxisValueFormatter(values));

        data.setBarWidth(barWidth);
        data.groupBars(0, groupSpace, barSpace);

        mBarChart.notifyDataSetChanged();
        mBarChart.setData(data);
    }


    /**
     * 设置Y轴值
     */
    public void setYAxis(float max, float min, int labelCount) {
        if (max < min) {
            return;
        }
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setLabelCount(labelCount, false);

        rightAxis.setAxisMaximum(max);
        rightAxis.setAxisMinimum(min);
        rightAxis.setLabelCount(labelCount, false);

        mBarChart.invalidate();
    }


    /**
     * 设置X轴的值
     */
    public void setXAxis(float max, float min, int labelCount) {
        xAxis.setAxisMaximum(max);
        xAxis.setAxisMinimum(min);
        xAxis.setLabelCount(labelCount, false);

        mBarChart.invalidate();
    }


    /**
     * 设置高限制线
     */
    public void setHightLimitLine(float high, String name, int color) {
        if (name == null) {
            name = "高限制线";
        }
        LimitLine hightLimit = new LimitLine(high, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        hightLimit.setLineColor(color);
        hightLimit.setTextColor(color);

        leftAxis.addLimitLine(hightLimit);

        mBarChart.invalidate();
    }


    /**
     * 设置低限制线
     */
    public void setLowLimitLine(int low, String name) {
        if (name == null) {
            name = "低限制线";
        }

        LimitLine hightLimit = new LimitLine(low, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);

        leftAxis.addLimitLine(hightLimit);

        mBarChart.invalidate();
    }


    /**
     * 设置描述信息
     */
    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        mBarChart.setDescription(description);
        mBarChart.invalidate();
    }


    /**
     * 展示柱状图(多条)
     */
    public void showBarChartPlan(List<Float> xAxisValues, List<List<Float>> yAxisValues,
                                 List<String> labels, List<Integer> colours, List<String> values) {

        initLineChart();

        BarData data = new BarData();

        for (int i = 0; i < yAxisValues.size(); i++) {

            ArrayList<BarEntry> entries = new ArrayList<>();

            for (int j = 0; j < yAxisValues.get(i).size(); j++) {
                entries.add(new BarEntry(xAxisValues.get(j), yAxisValues.get(i).get(j)));
            }

            BarDataSet barDataSet = new BarDataSet(entries, labels.get(i));
            barDataSet.setColor(colours.get(i));
            barDataSet.setValueTextColor(colours.get(i));
            barDataSet.setValueTextSize(10f);
            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            barDataSet.setDrawValues(false);

            data.addDataSet(barDataSet);
        }

        int amount = yAxisValues.size();

        //float groupSpace = 0.5f; //柱状图组之间的间距
        //float barSpace = (float) ((1 - 0.12) / amount / 20); // x4 DataSet
        //float barWidth = (float) ((1 - 0.12) / amount / 10 * 4); // x4 DataSet

        float barSpace   = (float) ((1 - 0.12) / amount / 10);
        float barWidth   = (float) (barSpace * 3);
        float groupSpace = 1 - (barWidth * 2 + barSpace * 2); //柱状图组之间的间距

        xAxis.setLabelCount(xAxisValues.size() - 1, false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new XAxisValueFormatter(values));
        xAxis.setXOffset(barSpace);

        data.setBarWidth(barWidth);
        data.groupBars(0, groupSpace, barSpace);

        mBarChart.setDrawValueAboveBar(true);
        mBarChart.notifyDataSetChanged();
        mBarChart.setData(data);
    }


    /**
     * 展示柱状图(多条)
     */
    public void showBarChart(List<Float> xAxisValues, List<List<Float>> yAxisValues,
                             List<String> labels, List<Integer> colours) {

        initLineChart();

        BarData data = new BarData();

        for (int i = 0; i < yAxisValues.size(); i++) {

            ArrayList<BarEntry> entries = new ArrayList<>();

            for (int j = 0; j < yAxisValues.get(i).size(); j++) {
                entries.add(new BarEntry(xAxisValues.get(j), yAxisValues.get(i).get(j)));
            }

            BarDataSet barDataSet = new BarDataSet(entries, labels.get(i));
            barDataSet.setColor(colours.get(i));
            barDataSet.setValueTextColor(colours.get(i));
            barDataSet.setValueTextSize(10f);
            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            barDataSet.setDrawValues(false);

            data.addDataSet(barDataSet);
        }


        List<String> values = new ArrayList<>();
        values.add("qw");
        values.add("qwe");
        values.add("qweqwe");
        values.add("asd");
        values.add("asd");
        values.add("asdas");
        values.add("xczc");
        values.add("dffgdf");

        // group"的宽度
        xAxis.setLabelCount(xAxisValues.size(), false);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(xAxisValues.size());//不加的话柱子会显示不全
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //X轴位置(默认为上面)
        xAxis.setValueFormatter(new XAxisValueFormatter(values));

        //获取有多少组数据
        int amount = yAxisValues.size();

        float barSpace   = (float) ((1 - 0.12) / amount / 10);
        float barWidth   = (float) (barSpace * 3);
        float groupSpace = 1 - (barWidth * 2 + barSpace * 2); //柱状图组之间的间距

        data.setBarWidth(barWidth);
        data.groupBars(0, groupSpace, barSpace);

        mBarChart.notifyDataSetChanged();
        mBarChart.setData(data);
        mBarChart.invalidate();
    }

    static class XAxisValueFormatter extends ValueFormatter {
        private final List<String> values;

        public XAxisValueFormatter(List<String> _values) {
            this.values = _values;
        }


        @Override
        public String getFormattedValue(float value) {
            Log.e("XAxisValueFormatter", "getFormattedValue: value->" + value);
            if (value < 0) {
                return values.get(0);
            } else if (value > values.size() - 1) {
                return values.get(values.size() - 1);
            } else {
                return values.get((int) value);
            }
        }

       /* @Override
        public String getAxisLabel(float value, AxisBase axis) {
            //Log.e("XAxisValueFormatter", "getAxisLabel: value->" + value);
            //return values.get((int) value);
            return String.valueOf((int)value);
        }*/
    }
}