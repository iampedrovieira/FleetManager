package com.example.fleetmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Button
import android.widget.EditText
import java.util.*

class AddReminder : AppCompatActivity() {
    private lateinit var add_btn : Button
    private lateinit var location_input: EditText
    private lateinit var email_input: EditText
    private lateinit var date_input: EditText
    private lateinit var other_input: EditText
    private lateinit var title_input: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        location_input = findViewById(R.id.locationText)
        email_input = findViewById(R.id.emailText)
        other_input = findViewById(R.id.additionalInfoText)
        title_input = findViewById(R.id.titleText)

        //Add Button
        add_btn = findViewById(R.id.create_reminder)
        add_btn.setOnClickListener {
            /**No input da data fazer split ao resultado e criar o Long no format seguinte.
             * NOTA: O parêmetro MONTH do set tem que ser o mês que nós queremos - 1*/
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
            calendarInfo.putExtra(CalendarContract.Events.TITLE, title_input.text.toString())
            calendarInfo.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            calendarInfo.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
            calendarInfo.putExtra(CalendarContract.Events.DESCRIPTION, other_input.text.toString())
            calendarInfo.putExtra(CalendarContract.Events.EVENT_LOCATION, location_input.text.toString())
            calendarInfo.putExtra(Intent.EXTRA_EMAIL, email_input.text.toString())

            startActivity(calendarInfo)
        }
    }
}