package com.example.fleetmanager

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fleetmanager.chat.model.MessageType
import com.example.fleetmanager.chat.model.TextMessage
import com.example.fleetmanager.chat.model.User
import com.example.fleetmanager.util.AppConstants
import com.example.fleetmanager.util.FirestoreUtil
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import org.w3c.dom.Text
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messageListenerRegistration: ListenerRegistration
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar
    private var shoudInitRecyclerView = true
    private lateinit var editText: TextView
    private lateinit var messagesSection:Section
    private lateinit var currentUser: User
    private lateinit var otherUserid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        editText = findViewById(R.id.editText_message)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = intent.getStringExtra(AppConstants.USER_NAME)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        FirestoreUtil.getCurrentUser { currentUser = it }
        otherUserid = intent.getStringExtra(AppConstants.USER_ID)!!
        val sharedPref: SharedPreferences = getSharedPreferences(
            R.string.preference_file_key.toString(),
            Context.MODE_PRIVATE
        )
        val sender_key = FirebaseAuth.getInstance().currentUser!!.uid
            FirestoreUtil.getOrCreateChatChannel(otherUserid!!){channelId ->
                messageListenerRegistration = FirestoreUtil.addChatMessagesListener(channelId,this,this::updateRecyclerView)
                fab_send_image.setOnClickListener{
                    val messageToSend = TextMessage(editText.text.toString(),Calendar.getInstance().time,sender_key!!,otherUserid!!,"",MessageType.TEXT)
                    editText.setText("")
                    FirestoreUtil.sendMessage(messageToSend,channelId)
                }
            }
    }

    private fun updateRecyclerView(messages:List<Item>){
        fun init(){
            recycler_view_messages.apply {
                layoutManager = LinearLayoutManager(this@ChatActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messagesSection = Section(messages)
                    add(messagesSection)
                }
            }
            shoudInitRecyclerView=false
        }
        fun updateItems() = messagesSection.update(messages)

        if(shoudInitRecyclerView){
            init()
        }else{
            updateItems()
        }
        recycler_view_messages.scrollToPosition(recycler_view_messages.adapter!!.itemCount-1)
    }
}