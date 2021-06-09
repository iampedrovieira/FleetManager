package com.example.fleetmanager.adapters

// import com.example.fleetmanager.ui.employees.EmployeesFragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fleetmanager.ChatActivity
import com.example.fleetmanager.R
import com.example.fleetmanager.api.OutputEmployee
import com.example.fleetmanager.uiManagement.employees.EmployeesFragment
import com.example.fleetmanager.util.AppConstants


class EmployeesAdapter(val context: Context, val fragment: EmployeesFragment): RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var employees_list = emptyList<OutputEmployee>()

    inner class EmployeesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val employee_nameViewHolder: TextView = itemView.findViewById(R.id.employee_name_txt)
        val employee_idViewHolder: TextView = itemView.findViewById(R.id.employee_id_txt)
        val profile_imgViewHolder: ImageView = itemView.findViewById(R.id.employee_profile_img)
        val chat_imgViewHolder: ImageView = itemView.findViewById(R.id.chat_img)
        val employee_status: TextView = itemView.findViewById(R.id.employee_status)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): EmployeesAdapter.EmployeesViewHolder {
        // Criar nova view que define a User Interface do item da lista
        val view = inflater.inflate(R.layout.recycler_employee_line, parent, false)
        return EmployeesViewHolder(view)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onBindViewHolder(holder: EmployeesAdapter.EmployeesViewHolder, position: Int) {
        val currentEmployee = employees_list[position]
        val bm : Bitmap

        if(currentEmployee.picture != null){
            val base64String: String = currentEmployee.picture
            val img : ByteArray? = Base64.decode(base64String, Base64.DEFAULT)
            bm = BitmapFactory.decodeByteArray(img, 0, img!!.size)
            holder.profile_imgViewHolder.setImageBitmap(bm)
        }

        Log.d("aa", employees_list.size.toString())
        holder.employee_nameViewHolder.text = holder.itemView.context.getString(R.string.employee_name,
            currentEmployee.employee_name)
        holder.employee_idViewHolder.text = holder.itemView.context.getString(R.string.employee_id,
            currentEmployee.employee_key)
        holder.chat_imgViewHolder.setImageResource(R.drawable.ic_chat)
        holder.itemView.setOnClickListener{
            val i = Intent(context, ChatActivity::class.java)
            i.putExtra(AppConstants.USER_NAME, currentEmployee.employee_name)
            i.putExtra(AppConstants.USER_ID, currentEmployee.employee_key)
            context.startActivity(i)

            Log.v("adsadasd", currentEmployee.employee_name)
        }
        if(currentEmployee.on_service.equals(true)){
            holder.employee_status.text = holder.itemView.context.getString(R.string.employee_status,
                currentEmployee.on_service)
        }

    }

    fun setEmployees(employees: List<OutputEmployee>){
        Log.d("camioes", employees.toString())
        this.employees_list = employees
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return employees_list.size
    }
}