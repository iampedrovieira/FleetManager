package com.example.fleetmanager.chat.model.recyclerview.item

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.System.getString
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import com.example.fleetmanager.R
import com.example.fleetmanager.chat.model.TextMessage
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_text_message.view.*
import java.text.SimpleDateFormat

class TextMessageItem(val message: TextMessage, val context: Context):Item() {
    override fun bind(viewHolder:ViewHolder, position: Int) {
        viewHolder.itemView.textView_message_text.text = message.text
        setTimeText(viewHolder)
        setMessageRootGravity(viewHolder)
    }
    private fun setTimeText(viewHolder: ViewHolder){
        val dateFormat = SimpleDateFormat("dd/MM/yy HH:mm")
        viewHolder.itemView.textView_message_time.text= dateFormat.format(message.time)
    }

    @SuppressLint("ResourceAsColor")
    private fun setMessageRootGravity(viewHolder: ViewHolder){
        val sharedPref: SharedPreferences = context.getSharedPreferences(
            R.string.preference_file_key.toString(),
            Context.MODE_PRIVATE
        )
        val sender_key = FirebaseAuth.getInstance().currentUser!!.uid
        if(message.senderId == sender_key){

            viewHolder.itemView.message_root.apply {
                setBackgroundResource(R.color.primaryBlue)

                //viewHolder.itemView.textView_message_text.setTextColor(R.color.browser_actions_bg_grey)
                //viewHolder.itemView.textView_message_time.setTextColor(R.color.browser_actions_bg_grey)
                val lParams = FrameLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT,Gravity.END)
                this.layoutParams = lParams
            }
        }else{
            viewHolder.itemView.message_root.apply {
                setBackgroundResource(R.color.browser_actions_bg_grey)
                viewHolder.itemView.textView_message_text.setTextColor(R.color.black)
                viewHolder.itemView.textView_message_time.setTextColor(R.color.black)
                val lParams = FrameLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT,Gravity.START)
                this.layoutParams = lParams
            }
        }

    }

    override fun getLayout() = R.layout.item_text_message


}