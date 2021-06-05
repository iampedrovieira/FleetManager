package com.example.fleetmanager.chat.model

data class User(val registrationToken: MutableList<String>){
    constructor(): this(mutableListOf())
}
