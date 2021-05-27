package com.example.fleetmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.fleetmanager.util.AppConstants
import com.example.fleetmanager.util.FirestoreUtil
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item

class ChatActivity : AppCompatActivity() {

    private lateinit var messageListenerRegistration: ListenerRegistration
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = intent.getStringExtra(AppConstants.USER_NAME)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val otherUserid = intent.getStringExtra(AppConstants.USER_ID)
        if (otherUserid != null) {
            FirestoreUtil.getOrCreateChatChannel(otherUserid){channelId ->
                messageListenerRegistration = FirestoreUtil.addChatMessagesListener(channelId,this,this::onMessagesChanged)
            }
        }
        Log.v("aaaaaaaaaaaaaaaaaaa",intent.getStringExtra(AppConstants.USER_NAME).toString())
    }

    private fun onMessagesChanged(messages:List<Item>){
        Log.v("aaaaaaaaaaaaaaaaaaa","ons dasru undasd")
    }
}