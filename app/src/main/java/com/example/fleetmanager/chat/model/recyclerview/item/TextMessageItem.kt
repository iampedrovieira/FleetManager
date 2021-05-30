package com.example.fleetmanager.chat.model.recyclerview.item

import android.content.Context
import android.content.SharedPreferences
import android.view.Gravity
import android.widget.FrameLayout
import com.example.fleetmanager.R
import com.example.fleetmanager.chat.model.TextMessage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_text_message.view.*
import java.text.SimpleDateFormat

class TextMessageItem(val message: TextMessage, val context: Context):Item() {
    override fun bind(viewHolder:ViewHolder, position: Int) {
        viewHolder.itemView.textView_message_text.text = message.text
        setTimeText(viewHolder)
    }
    private fun setTimeText(viewHolder: ViewHolder){
        val dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT)
        viewHolder.itemView.textView_message_time.text= dateFormat.format(message.time)
    }

    private fun setMessageRootGravity(viewHolder: ViewHolder){
        val sharedPref: SharedPreferences = context.getSharedPreferences(
            R.string.preference_file_key.toString(),
            Context.MODE_PRIVATE
        )
        val sender_key = sharedPref.getString(R.string.employee_id.toString(), "")
        if(message.senderId == sender_key){
            viewHolder.itemView.message_root.apply {
                setBackgroundResource(R.color.primaryBlue)
                val lParams = FrameLayout.LayoutParams(0,0,Gravity.END)
                this.layoutParams = lParams
            }
        }else{
            viewHolder.itemView.message_root.apply {
                setBackgroundResource(R.color.white)
                val lParams = FrameLayout.LayoutParams(0,0,Gravity.START)
                this.layoutParams = lParams
            }
        }

    }

    override fun getLayout() = R.layout.item_text_message


}