package com.example.fleetmanager

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddReminder : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar
    private lateinit var add_btn : Button
    private lateinit var location_input: EditText
    private lateinit var email_input: EditText
    private lateinit var date_input: EditText
    private lateinit var other_input: EditText
    private lateinit var title_input: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        // toolbar
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.add_reminder)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        location_input = findViewById(R.id.locationText)
        email_input = findViewById(R.id.emailText)
        other_input = findViewById(R.id.additionalInfoText)
        title_input = findViewById(R.id.titleText)
        date_input = findViewById(R.id.dateText)

        //Add Button
        add_btn = findViewById(R.id.create_reminder)
        add_btn.setOnClickListener {
            /**No input da data fazer split ao resultado e criar o Long no format seguinte.
             * NOTA: O parêmetro MONTH do set tem que ser o mês que nós queremos - 1*/
            val startMillis: Long = Calendar.getInstance().run{

                val currentDate = Calendar.getInstance()
                val day = currentDate.get(Calendar.DAY_OF_MONTH)
                val month = currentDate.get(Calendar.MONTH)
                val year = currentDate.get(Calendar.YEAR)
                set(year, month, day)
                timeInMillis
            }

            val endMillis: Long = Calendar.getInstance().run{
                val date: String = date_input.text.toString()
                var dateParts : Array<String> = date.split("/").toTypedArray()
                val day = dateParts[0].toInt()
                val month = dateParts[1].toInt() - 1
                val year = dateParts[2].toInt()
                set(year, month, day)
                timeInMillis
            }

            //information for calendar
            val calendarInfo = Intent(Intent.ACTION_INSERT)
            calendarInfo.setData(CalendarContract.Events.CONTENT_URI)
            calendarInfo.putExtra(CalendarContract.Events.TITLE, title_input.text.toString())
            calendarInfo.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            calendarInfo.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
            calendarInfo.putExtra(CalendarContract.Events.DESCRIPTION, other_input.text.toString())
            calendarInfo.putExtra(CalendarContract.Events.EVENT_LOCATION,
                location_input.text.toString())
            calendarInfo.putExtra(Intent.EXTRA_EMAIL, email_input.text.toString())

            startActivity(calendarInfo)

        }

        date_input.setOnClickListener { openDatePicker(date_input) }
    }

    fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    fun openDatePicker(view: View) {
        showDatePickerDialog()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "$dayOfMonth/${month + 1}/$year"
        date_input.setText(date, TextView.BufferType.EDITABLE)
    }
}