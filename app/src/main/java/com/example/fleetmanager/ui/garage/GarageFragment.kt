package com.example.fleetmanager.ui.garage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fleetmanager.R

class GarageFragment : Fragment() {

    private lateinit var garageViewModel: GarageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        garageViewModel =
            ViewModelProvider(this).get(GarageViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_garage, container, false)
        val textView: TextView = root.findViewById(R.id.text_garage)
        garageViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}