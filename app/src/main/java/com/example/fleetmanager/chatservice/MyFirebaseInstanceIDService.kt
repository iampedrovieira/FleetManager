package com.example.fleetmanager.chatservice

import com.example.fleetmanager.util.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import java.lang.NullPointerException

class MyFirebaseInstanceIDService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener{task ->
            run {
                val newRegistrationToken = task.result

                if (FirebaseAuth.getInstance().currentUser != null)
                    addTokenToFirestore(newRegistrationToken)
            }
        }


    }

    companion object{
        fun addTokenToFirestore(newRegistrationToken: String?){
            if(newRegistrationToken == null) throw NullPointerException("FCM token is null")

            FirestoreUtil.getFCMRegistrationTokens { tokens ->
                if(tokens.contains(newRegistrationToken))
                    return@getFCMRegistrationTokens

                tokens.add(newRegistrationToken)
                FirestoreUtil.setFCMRegistrationTokens(tokens)
            }
        }
    }
}