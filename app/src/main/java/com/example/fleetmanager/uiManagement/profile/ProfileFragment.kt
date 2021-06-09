package com.example.fleetmanager.uiManagement.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fleetmanager.Login
import com.example.fleetmanager.R
import com.example.fleetmanager.api.Endpoints
import com.example.fleetmanager.api.OutputManagement
import com.example.fleetmanager.api.ServiceBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    // Toolbar
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    // Progress Bar
    private lateinit var profileProgressView: ProgressBar
    // Fields
    private lateinit var companyNamePHTV: TextView
    private lateinit var companyNameTV: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        // Toolbar
        toolbar = root.findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.title_profile)
        toolbar.inflateMenu(R.menu.profile_menu)
        toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        // Progress Bar
        profileProgressView = root.findViewById(R.id.profilePB)
        // Fields
        companyNamePHTV = root.findViewById(R.id.company)
        companyNameTV = root.findViewById(R.id.company_name)

        val sharedPref: SharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val company_key = sharedPref.getString(getString(R.string.company), "aaaa")
        getUserInfo(company_key)
        return root
    }

    // Logout Button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_nav -> {
                // Removing user data from shared preferences
                val sharedPref: SharedPreferences = requireActivity().getSharedPreferences(
                    getString(R.string.preference_file_key),
                    Context.MODE_PRIVATE
                )
                with(sharedPref.edit()) {
                    putBoolean(getString(R.string.logged), false)
                    putInt(getString(R.string.uid), 0)
                    putString(getString(R.string.isEmployee), null)
                    putString(getString(R.string.company), null)
                    commit()
                }

                Firebase.auth.signOut()

                Log.d("ProfilePage", "Logout")
                val intent = Intent(activity, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getUserInfo(company_key: String?) {
        profileProgressView.visibility = View.VISIBLE

        val request = ServiceBuilder.buildService(Endpoints::class.java)
        val call = request.getManagementInfo(company_key)
        call.enqueue(object : Callback<OutputManagement> {
            override fun onResponse(
                call: Call<OutputManagement>,
                response: Response<OutputManagement>,
            ) {
                if (response.isSuccessful) {
                    profileProgressView.visibility = View.INVISIBLE
                    companyNamePHTV.visibility = View.VISIBLE
                    companyNameTV.visibility = View.VISIBLE

                    val res = response.body()!!
                    Log.d("****ProfilePage", res.toString())

                    companyNameTV.text = res.company_name
                }
            }

            override fun onFailure(call: Call<OutputManagement>, t: Throwable) {
                Log.d("****ProfilePage", t.toString())
                Log.d("****ProfilePage", call.toString())
            }
        })
    }
}