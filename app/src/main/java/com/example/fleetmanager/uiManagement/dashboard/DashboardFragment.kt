package com.example.fleetmanager.uiManagement.dashboard

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.fleetmanager.R
import com.example.fleetmanager.api.*
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
    private lateinit var chart3: BarChart
    private lateinit var chart4: Button
    private lateinit var chart5: Button
    private lateinit var chart1Data: List<Chart1>
    private lateinit var chart2Data: List<Chart2>
    private lateinit var chart3Data: List<Chart3>
    private lateinit var chart4Data: List<Chart4>
    private lateinit var chart5Data: List<Chart5>
    private lateinit var chart1ProgressView: ProgressBar
    private lateinit var chart2ProgressView: ProgressBar
    private lateinit var chart3ProgressView: ProgressBar
    private lateinit var chart4ProgressView: ProgressBar


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
        chart3 = root.findViewById(R.id.chart_3)
        chart4 = root.findViewById(R.id.chart_4)
        chart5 = root.findViewById(R.id.chart_5)

        chart1ProgressView = root.findViewById(R.id.chart1Progress)
        chart2ProgressView = root.findViewById(R.id.chart2Progress)
        chart3ProgressView = root.findViewById(R.id.chart3Progress)
        chart4ProgressView = root.findViewById(R.id.chart4Progress)

        getCharts()

        return root
    }

    private fun getCharts() {
        chart1ProgressView.visibility = View.VISIBLE
        chart2ProgressView.visibility = View.VISIBLE
        chart3ProgressView.visibility = View.VISIBLE
        chart4ProgressView.visibility = View.VISIBLE

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
                        val d =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH).parse(
                                chart1Data[i].mes
                            )
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
                    chart1.setDescription(null)
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
                    pieDataset.valueTextColor = Color.WHITE
                    pieDataset.sliceSpace = 2f
                    val data = PieData(xvalues, pieDataset)

                    chart2.data = data
                    chart2.setBackgroundColor(resources.getColor(R.color.white))
                    chart2.animateXY(1500, 1500)
                    chart2.setDescription(null)

                    val legend: Legend = chart2.legend
                    legend.position = Legend.LegendPosition.LEFT_OF_CHART
                }
            }

            override fun onFailure(call: Call<List<Chart2>>, t: Throwable) {
                Log.d("****DashboardFragment", "${t.message}")
            }
        })

        // Chart 3
        val callChart3 = request.getchart3(company_key)
        callChart3.enqueue(object : Callback<List<Chart3>> {
            override fun onResponse(
                call: Call<List<Chart3>>,
                response: Response<List<Chart3>>
            ) {
                if (response.isSuccessful) {
                    chart3ProgressView.visibility = View.INVISIBLE
                    chart3.visibility = View.VISIBLE

                    Log.d("****DashboardFragment", response.body().toString())
                    chart3Data = response.body()!!

                    var xvalues = ArrayList<String>()
                    var barEntries = ArrayList<BarEntry>()

                    for (i in chart3Data.indices) {
                        val d =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH).parse(
                                chart3Data[i].mes
                            )
                        val cal = Calendar.getInstance()
                        cal.time = d
                        chart3Data[i].mes = SimpleDateFormat("MMMM").format(cal.time)

                        xvalues.add(chart3Data[i].mes)
                        barEntries.add(BarEntry(chart3Data[i].trips.toFloat(), i))
                    }

                    val barDataset = BarDataSet(barEntries, getString(R.string.month))
                    barDataset.color = Color.rgb(3, 47, 100)
                    val data = BarData(xvalues, barDataset)

                    chart3.data = data
                    chart3.setBackgroundColor(resources.getColor(R.color.white))
                    chart3.animateXY(1000, 1000)
                    chart3.setDescription(null)
                }
            }

            override fun onFailure(call: Call<List<Chart3>>, t: Throwable) {
                Log.d("****DashboardFragment", "${t.message}")
            }
        })

        // Chart 4
        val callChart4 = request.getchart4(company_key)
        callChart4.enqueue(object : Callback<List<Chart4>> {
            override fun onResponse(
                call: Call<List<Chart4>>,
                response: Response<List<Chart4>>
            ) {
                if (response.isSuccessful) {
                    chart4ProgressView.visibility = View.INVISIBLE
                    chart4.visibility = View.VISIBLE

                    Log.d("****DashboardFragment", response.body().toString())
                    chart4Data = response.body()!!

                    chart4.text = chart4.text as String + chart4Data[0].empregados.toString()
                }
            }

            override fun onFailure(call: Call<List<Chart4>>, t: Throwable) {
                Log.d("****DashboardFragment", "${t.message}")
            }
        })

        // Chart 5
        val callChart5 = request.getchart5(company_key)
        callChart5.enqueue(object : Callback<List<Chart5>> {
            override fun onResponse(
                call: Call<List<Chart5>>,
                response: Response<List<Chart5>>
            ) {
                if (response.isSuccessful) {
                    chart5.visibility = View.VISIBLE

                    Log.d("****DashboardFragment", response.body().toString())
                    chart5Data = response.body()!!

                    chart5.text = chart5.text as String + chart5Data[0].emservico.toString()
                }
            }

            override fun onFailure(call: Call<List<Chart5>>, t: Throwable) {
                Log.d("****DashboardFragment", "${t.message}")
            }
        })
    }
}