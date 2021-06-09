package com.example.fleetmanager.uiEmployee.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.util.Base64
import android.view.MenuItem
import android.widget.ProgressBar
import com.example.fleetmanager.Login
import com.example.fleetmanager.MapsTracksActivityAll
import com.example.fleetmanager.R
import com.example.fleetmanager.api.Endpoints
import com.example.fleetmanager.api.OutputEmployee
import com.example.fleetmanager.api.OutputManagement
import com.example.fleetmanager.api.ServiceBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileEmployeeFragment : Fragment() {
    // Toolbar
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    // Progress Bar
    private lateinit var profileProgressView: ProgressBar

    // Fields
    private lateinit var nameTV: TextView
    private lateinit var phoneNumberTV: TextView
    private lateinit var onServiceTV: TextView
    private lateinit var addressPHTV: TextView
    private lateinit var addressTV: TextView
    private lateinit var companyNamePHTV: TextView
    private lateinit var companyNameTV: TextView
    private lateinit var photo: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile_employee, container, false)

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
        nameTV = root.findViewById(R.id.employee_name)
        phoneNumberTV = root.findViewById(R.id.employee_phone_number)
        onServiceTV = root.findViewById(R.id.onService)
        addressPHTV = root.findViewById(R.id.address)
        addressTV = root.findViewById(R.id.address_name)
        companyNamePHTV = root.findViewById(R.id.company)
        companyNameTV = root.findViewById(R.id.company_name)
        photo = root.findViewById(R.id.employee_profile_image)

        val sharedPref: SharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val user_key = sharedPref.getString(getString(R.string.uid), "aaaa")
        getUserInfo(user_key)

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

    private fun getUserInfo(user_key: String?) {
        profileProgressView.visibility = View.VISIBLE
        val request = ServiceBuilder.buildService(Endpoints::class.java)
        val call = request.getEmployeeInfo(user_key)

        call.enqueue(object : Callback<OutputEmployee> {
            override fun onResponse(
                call: Call<OutputEmployee>,
                response: Response<OutputEmployee>,
            ) {
                if (response.isSuccessful) {
                    profileProgressView.visibility = View.INVISIBLE
                    nameTV.visibility = View.VISIBLE
                    phoneNumberTV.visibility = View.VISIBLE
                    photo.visibility = View.VISIBLE
                    addressPHTV.visibility = View.VISIBLE
                    addressTV.visibility = View.VISIBLE
                    companyNamePHTV.visibility = View.VISIBLE
                    companyNameTV.visibility = View.VISIBLE

                    val res = response.body()!!
                    Log.d("****ProfilePage", response.body().toString())

                    nameTV.text = res.employee_name
                    phoneNumberTV.text = res.phone_number
                    val decodedBitmap: Bitmap? = res.picture.toBitmap()
                    photo.setImageBitmap(decodedBitmap)
                    addressTV.text = res.employee_address
                    getCompanyName(res.company_key).toString()
                }
            }

            override fun onFailure(call: Call<OutputEmployee>, t: Throwable) {
                Log.d("****ProfilePage", t.message.toString())
            }
        })
    }

    // extension function to decode base64 string to bitmap
    fun String.toBitmap(): Bitmap? {
        Base64.decode(this, Base64.DEFAULT).apply {
            return BitmapFactory.decodeByteArray(this, 0, size)
        }
    }

    private fun getCompanyName(company_key: String?) {
        val request = ServiceBuilder.buildService(Endpoints::class.java)
        val call = request.getManagementInfo(company_key)

        call.enqueue(object : Callback<OutputManagement> {
            override fun onResponse(
                call: Call<OutputManagement>,
                response: Response<OutputManagement>,
            ) {
                if (response.isSuccessful) {
                    val res = response.body()!!
                    Log.d("****ProfilePage", response.body().toString())
                    companyNameTV.text = res.company_name
                }
            }

            override fun onFailure(call: Call<OutputManagement>, t: Throwable) {
                Log.d("****ProfilePage", t.message.toString())
            }
        })
    }
}