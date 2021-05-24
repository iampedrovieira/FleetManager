package com.example.fleetmanager.uiManagement.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fleetmanager.R
import com.github.aachartmodel.aainfographics.aachartcreator.*

class DashboardFragment : Fragment() {

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var chart1: AAChartView
    private lateinit var chart2: AAChartView
    private lateinit var chart3: AAChartView
    private lateinit var chart4: AAChartView
    private lateinit var chart5: AAChartView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        toolbar = root.findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.title_dashboard)

        chart1 = root.findViewById(R.id.aa_chart_1)
        chart2 = root.findViewById(R.id.aa_chart_2)
        chart3 = root.findViewById(R.id.aa_chart_3)
        chart4 = root.findViewById(R.id.aa_chart_4)
        chart5 = root.findViewById(R.id.aa_chart_5)

        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("title")
            .backgroundColor(R.attr.backgroundColor)
            .axesTextColor(R.color.white.toString())
            .dataLabelsEnabled(true)
            .animationType(AAChartAnimationType.EaseInQuad)
            .series(arrayOf(
                AASeriesElement()
                    .name("Tokyo")
                    .data(arrayOf(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6)),
                AASeriesElement()
                    .name("NewYork")
                    .data(arrayOf(0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5)),
                AASeriesElement()
                    .name("London")
                    .data(arrayOf(0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0)),
                AASeriesElement()
                    .name("Berlin")
                    .data(arrayOf(3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8))
            )
            )

        chart1.aa_drawChartWithChartModel(aaChartModel)
        chart2.aa_drawChartWithChartModel(aaChartModel)
        chart3.aa_drawChartWithChartModel(aaChartModel)
        chart4.aa_drawChartWithChartModel(aaChartModel)
        chart5.aa_drawChartWithChartModel(aaChartModel)

        return root
    }
}