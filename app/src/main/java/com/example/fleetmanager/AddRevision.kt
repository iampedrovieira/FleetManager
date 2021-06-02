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



class AddRevision : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var payment_date: EditText
    private lateinit var payment_date_layout: TextInputLayout
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar
    private lateinit var type_of_note: String
    private lateinit var isApprovedLayout: View
    private lateinit var isApproved: Switch


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_revision)

        // toolbar
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.add_revision_note)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        payment_date = findViewById(R.id.paymentDateText)
        payment_date_layout = findViewById(R.id.paymentDateTextField)

        type_of_note = intent.getStringExtra("Type_note")!!
        //LinearLayout where the switch is inserted
        isApprovedLayout = findViewById(R.id.approved_layout)


        if(type_of_note.equals("inspection")){
            isApprovedLayout.visibility = View.VISIBLE
        }

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
        payment_date.setText(date, TextView.BufferType.EDITABLE)
    }
}