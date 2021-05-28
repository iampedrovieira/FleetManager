package com.example.fleetmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class RevisionSelectionArea : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revision_selection_area)

    }

    fun addReminder(view: View) {
        val i = Intent(this@RevisionSelectionArea, AddReminder::class.java)
        startActivity(i)
    }

    fun addNote(view: View) {
        val i = Intent(this@RevisionSelectionArea, AddRevision::class.java)
        startActivity(i)
    }
}