package com.example.fleetmanager

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

private lateinit var payment_date: TextInputEditText
private lateinit var payment_date_layout: TextInputLayout
private var datePicker: Boolean = false

class AddRevision : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var toolbar : androidx.appcompat.widget.Toolbar


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