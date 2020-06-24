package demo.zkttestdemo.effect.chart

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import demo.zkttestdemo.R
import demo.zkttestdemo.databinding.ActivityChartBinding
import java.util.*
import kotlin.collections.ArrayList


class ChartActivity : AppCompatActivity() {
    private lateinit var b: ActivityChartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val v = LayoutInflater.from(this).inflate(R.layout.activity_chart, null)
        b = ActivityChartBinding.bind(v)
        setContentView(v)

        initMPChart()
    }

    fun initMPChart() {
        val dataSets: ArrayList<ILineDataSet> = ArrayList() //线条数据集合

        for (i in 0 until 2) {

            //设置一条线上的所有绘制点
            var list: ArrayList<Entry> = arrayListOf()
            for (j in 0..10) {
                list.add(Entry(j.toFloat(), j.toFloat() + Random().nextInt(100)))  //其中两个数字对应的分别是  X轴  Y轴
            }
            //对点和线进行配置
            val lineDataSet = LineDataSet(list, "我是${i}")  //list是你这条线的数据  "语文" 是你对这条线的描述（也就是图例上的文字）
            lineDataSet.apply {
                // 设置折线的式样   这个是圆滑的曲线（有好几种自己试试）  默认是直线
                //mode = LineDataSet.Mode.CUBIC_BEZIER
                color = Color.GREEN  //线颜色
                lineWidth = 2F       //线粗细
                setDrawValues(false) //数值
                setDrawCircles(true) //圆点
                circleHoleRadius = 0F  //空心圆的圆心半径
                setDrawCircleHole(false) //是否画折线点上的空心圆  false表示直接画成实心圆
                setCircleColor(Color.BLACK) //圆点颜色  可以实现超过某个值定义成某个颜色的功能
                circleRadius = 1F      //圆点半径
                valueTextColor = ColorTemplate.getHoloBlue() //设置节点文字颜色
                isHighlightEnabled = true //设置是否显示十字线
                setDrawFilled(true) //曲线下方渐变的阴影
                fillDrawable = resources.getDrawable(R.drawable.line_gradient_bg_shape)
                // 定义折线上的数据显示    可以实现加单位    以及显示整数（默认是显示小数）
                /*valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(v: Float, entry: Entry, dataSetIndex: Int,
                                                   viewPortHandler: ViewPortHandler): String {
                        return "0"
                    }
                }*/
            }

            dataSets.add(lineDataSet)
        }

        //折线图点的标记
        val mv = MyMarkerView(this)
        b.chart.marker = mv

        //对于右下角一串字母的操作
        b.chart.description.apply {
            isEnabled = false                  //是否显示右下角描述
            text = "这是修改那串英文的方法"   //修改右下角字母的显示
            textSize = 20F                    //字体大小
            textColor = Color.RED             //字体颜色
        }

        //图例
        b.chart.legend.apply {
            isEnabled = true    //是否显示图例
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        }

        //X轴
        b.chart.xAxis.apply {
            setDrawGridLines(false)  //是否绘制X轴上的网格线（背景里面的竖线）
            setDrawAxisLine(true)
            granularity = 1f  //设置是否一个格子显示一条数据，如果不设置这个属性，就会导致X轴数据重复并且错乱的问题
            axisLineColor = Color.RED   //X轴颜色
            axisLineWidth = 2F           //X轴粗细
            position = XAxis.XAxisPosition.BOTTOM //X轴位置(默认为上面)
            axisMinimum = 1F   //X轴最小数值
            axisMaximum = 12f   //X轴最大数值
            //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
            setLabelCount(11, false)
            valueFormatter = object : ValueFormatter() {
                //X轴自定义坐标
                override fun getAxisLabel(v: Float, axis: AxisBase?): String {
                    return "${v.toInt()}月"
                }
            }
        }

        //Y轴
        b.chart.axisLeft.apply {
            setDrawGridLines(true)  //是否绘制Y轴上的网格线（背景里面的横线）
            setDrawAxisLine(false)
            granularity = 1f  //设置是否一个格子显示一条数据，如果不设置这个属性，就会导致X轴数据重复并且错乱的问题
            axisLineColor = Color.BLUE  //Y轴颜色
            axisLineWidth = 2F          //Y轴粗细
            axisMinimum = 0F   //Y轴最小数值
            axisMaximum = 120F   //Y轴最大数值
            //Y轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
            setLabelCount(4, false) //如果是false 绘制的数量=(最大值-最小值)/count
            valueFormatter = object : ValueFormatter() {
                //Y轴自定义坐标
                override fun getAxisLabel(v: Float, axis: AxisBase?): String {
                    return v.toInt().toString()
                }
            }
        }

        b.chart.apply {
            setBackgroundColor(Color.WHITE)   //背景颜色
            setNoDataText("暂无数据") //无数据时显示的文字
            axisRight.isEnabled = false //是否隐藏右边的Y轴（不设置的话有两条Y轴 同理可以隐藏左边的Y轴）
            setScaleEnabled(false) //设置为不可缩放
            isDragEnabled = true //设置为可左右滑动
            isHighlightPerDragEnabled = true //能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        }

        val lineData = LineData(dataSets)
        b.chart.data = lineData

        //数据更新
        b.chart.notifyDataSetChanged()
        b.chart.invalidate()

    }


    class MyMarkerView(context: Context) : MarkerView(context, R.layout.layout_markerview) {

        private val tvContent: TextView

        init {
            tvContent = findViewById<View>(R.id.tvContent) as TextView
        }

        //显示的内容
        override fun refreshContent(e: Entry, highlight: Highlight) {
            tvContent.text = "x=${e.x} y=${e.y}"
            super.refreshContent(e, highlight)
        }

        //标记相对于折线图的偏移量
        override fun getOffset(): MPPointF {
            return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
        }
    }
}

