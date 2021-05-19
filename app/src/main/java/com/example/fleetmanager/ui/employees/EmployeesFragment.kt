package com.example.fleetmanager.ui.employees

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fleetmanager.R

class EmployeesFragment : Fragment() {

    private lateinit var employeesViewModel: EmployeesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        employeesViewModel =
            ViewModelProvider(this).get(EmployeesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_employees, container, false)
        val textView: TextView = root.findViewById(R.id.text_profile)
        employeesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}