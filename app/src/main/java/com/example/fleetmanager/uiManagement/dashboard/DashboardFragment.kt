package com.example.fleetmanager.uiManagement.dashboard

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.fleetmanager.R
import com.example.fleetmanager.api.Chart1
import com.example.fleetmanager.api.Chart2
import com.example.fleetmanager.api.Endpoints
import com.example.fleetmanager.api.ServiceBuilder
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var chart1: BarChart
    private lateinit var chart2: PieChart
    private lateinit var chart1Data: List<Chart1>
    private lateinit var chart2Data: List<Chart2>
    private lateinit var chart1ProgressView: ProgressBar
    private lateinit var chart2ProgressView: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        toolbar = root.findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.title_dashboard)

        chart1 = root.findViewById(R.id.chart_1)
        chart2 = root.findViewById(R.id.chart_2)

        chart1ProgressView = root.findViewById(R.id.chart1Progress)
        chart2ProgressView = root.findViewById(R.id.chart2Progress)

        getCharts()

        return root
    }

    private fun getCharts() {
        chart1ProgressView.visibility = View.VISIBLE
        chart2ProgressView.visibility = View.VISIBLE

        val sharedPref: SharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val company_key = sharedPref.getString(getString(R.string.company), "aaaa")
        val request = ServiceBuilder.buildService(Endpoints::class.java)

        // Chart 1
        val callChart1 = request.getchart1(company_key)

        callChart1.enqueue(object : Callback<List<Chart1>> {
            override fun onResponse(
                call: Call<List<Chart1>>,
                response: Response<List<Chart1>>
            ) {
                if (response.isSuccessful) {
                    chart1ProgressView.visibility = View.INVISIBLE
                    chart1.visibility = View.VISIBLE

                    Log.d("****DashboardFragment", response.body().toString())
                    chart1Data = response.body()!!

                    var xvalues = ArrayList<String>()
                    var barEntries = ArrayList<BarEntry>()

                    for (i in chart1Data.indices) {
                        val d = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH).parse(chart1Data[i].mes)
                        val cal = Calendar.getInstance()
                        cal.time = d
                        chart1Data[i].mes = SimpleDateFormat("MMMM").format(cal.time)

                        xvalues.add(chart1Data[i].mes)
                        barEntries.add(BarEntry(chart1Data[i].gastos.toFloat(), i))
                    }

                    val barDataset = BarDataSet(barEntries, getString(R.string.month))
                    barDataset.color = Color.rgb(3, 47, 100)
                    val data = BarData(xvalues, barDataset)

                    chart1.data = data
                    chart1.setBackgroundColor(resources.getColor(R.color.white))
                    chart1.animateXY(1000, 1000)
                    chart1.setDescription(getString(R.string.chart1))
                }
            }

            override fun onFailure(call: Call<List<Chart1>>, t: Throwable) {
                Log.d("****DashboardFragment", "${t.message}")
            }
        })

        // Chart 2
        val callChart2 = request.getchart2(company_key)
        callChart2.enqueue(object : Callback<List<Chart2>> {
            override fun onResponse(
                call: Call<List<Chart2>>,
                response: Response<List<Chart2>>
            ) {
                if (response.isSuccessful) {
                    chart2ProgressView.visibility = View.INVISIBLE
                    chart2.visibility = View.VISIBLE

                    Log.d("****DashboardFragment", response.body().toString())
                    chart2Data = response.body()!!

                    var xvalues = ArrayList<String>()
                    var pieChartEntries = ArrayList<Entry>()

                    for (i in chart2Data.indices) {
                        xvalues.add(chart2Data[i].license_plate)
                        pieChartEntries.add(Entry(chart2Data[i].costs.toFloat(), i))
                    }
                    val pieDataset = PieDataSet(pieChartEntries, getString(R.string.car))
                    pieDataset.color = Color.rgb(3, 47, 100)
                    pieDataset.sliceSpace = 2.5f
                    val data = PieData(xvalues, pieDataset)

                    chart2.data = data
                    chart2.setBackgroundColor(resources.getColor(R.color.white))
                    chart2.animateXY(1500, 1500)
                    chart2.setDescription(getString(R.string.chart2))

                    val legend: Legend = chart2.legend
                    legend.position = Legend.LegendPosition.LEFT_OF_CHART
                }
            }

            override fun onFailure(call: Call<List<Chart2>>, t: Throwable) {
                Log.d("****DashboardFragment", "${t.message}")
            }
        })
    }
}