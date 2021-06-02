package com.example.fleetmanager.chat.model

data class ChatChannel(val userIds:MutableList<String>){
    constructor():this(mutableListOf())
}