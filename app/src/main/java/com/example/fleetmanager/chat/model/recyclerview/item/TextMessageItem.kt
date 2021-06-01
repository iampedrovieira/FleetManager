package com.example.fleetmanager.chat.model.recyclerview.item

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.example.fleetmanager.R
import com.example.fleetmanager.chat.model.TextMessage
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
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
            setBackgroundResource(R.drawable.chat_send)

            val lParams = FrameLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT,Gravity.END)

            this.layoutParams = lParams
            }
            viewHolder.itemView.textView_message_text.setTextColor(Color.WHITE)
            viewHolder.itemView.textView_message_time.setTextColor(Color.WHITE)
            viewHolder.itemView.textView_message_text.setPadding(50,5,15,5)

            //viewHolder.itemView.textView_message_time.layoutParams= FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.RIGHT)
            //viewHolder.itemView.textView_message_time.setPadding(5,50,15,50)
        }else{
            viewHolder.itemView.message_root.apply {
                setBackgroundResource(R.drawable.chat_receive)
                val lParams = FrameLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT,Gravity.START)
                this.layoutParams = lParams
            }
            viewHolder.itemView.textView_message_text.setTextColor(Color.BLACK)
            viewHolder.itemView.textView_message_time.setTextColor(Color.BLACK)
            viewHolder.itemView.textView_message_text.setPadding(50,5,15,5)

            //viewHolder.itemView.textView_message_time.layoutParams= FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.)
            //viewHolder.itemView.textView_message_time.setPadding(50,50,15,50)
        }




    }

    override fun getLayout() = R.layout.item_text_message


}