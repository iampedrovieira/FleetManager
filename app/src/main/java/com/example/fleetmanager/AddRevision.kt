package com.example.fleetmanager

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

private lateinit var payment_date: TextInputEditText
private lateinit var payment_date_layout: TextInputLayout
private var datePicker: Boolean = false

class AddRevision : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var toolbar : androidx.appcompat.widget.Toolbar
    private lateinit var company_input: EditText
    private lateinit var add_btn : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_revision)

        // toolbar
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.title_activity_add_revision)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        payment_date = findViewById(R.id.paymentDateText)
        payment_date_layout = findViewById(R.id.paymentDateTextField)

        payment_date.setOnClickListener { openDatePicker(payment_date) }

        //Add Button
        add_btn = findViewById(R.id.create_reminder)
        add_btn.setOnClickListener {
            val startMillis: Long = Calendar.getInstance().run{
                set(2021, 7, 10, 20, 30)
                timeInMillis
            }

            val endMillis: Long = Calendar.getInstance().run{
                set(2021, 7, 10, 21, 30)
                timeInMillis
            }

            //information for calendar
            val calendarInfo = Intent(Intent.ACTION_INSERT)
            calendarInfo.setData(CalendarContract.Events.CONTENT_URI)
            calendarInfo.putExtra(CalendarContract.Events.TITLE, "Exemplo")
            calendarInfo.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            calendarInfo.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
            calendarInfo.putExtra(CalendarContract.Events.DESCRIPTION, "Descrição de exemplo")
            calendarInfo.putExtra(CalendarContract.Events.EVENT_LOCATION, "A casa do nelson")
            calendarInfo.putExtra(Intent.EXTRA_EMAIL, "nelsoncampinho@ipvc.pt")

            startActivity(calendarInfo)
        }
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
        if(datePicker)
            payment_date_layout.editText?.setText(date)
    }
}