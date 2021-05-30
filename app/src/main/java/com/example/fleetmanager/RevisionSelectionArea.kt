package com.example.fleetmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class RevisionSelectionArea : AppCompatActivity() {
    private lateinit var reminder_btn: Button
    private lateinit var note_btn: Button
    private lateinit var dashboard_btn_clicked: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revision_selection_area)

        reminder_btn = findViewById(R.id.addReminder)
        note_btn = findViewById(R.id.addNote)

        //Contains the string value of the button clicked on the dashboard.
        dashboard_btn_clicked = intent.getStringExtra("Dashboard_Clicked")!!


        when(dashboard_btn_clicked){
            "revision" -> {
                note_btn.setText(R.string.add_revision_note)
            }
            "insurance" ->{
                note_btn.setText(R.string.add_insurance_note)
            }
            "inspection"->{
                note_btn.setText(R.string.add_inspection_note)
            }
        }

    }

    fun addReminder(view: View) {
        val i = Intent(this@RevisionSelectionArea, AddReminder::class.java)
        startActivity(i)
    }

    fun addNote(view: View) {
        val i = Intent(this@RevisionSelectionArea, AddRevision::class.java)
        i.putExtra("Type_note", dashboard_btn_clicked)
        startActivity(i)
    }
}