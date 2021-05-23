package com.example.fleetmanager.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Endpoints {
    @FormUrlEncoded
    @POST("users/login")
    fun login(@Field("firebaseKey") firebaseKey: String): Call<OutputLogin>

    @FormUrlEncoded
    @POST("company/vehicle")
    fun getVehicles(@Field("companyKey") company_key: String?): Call<List<OutputVehicle>>



    /*@POST("users/login")
    fun login(@Body firebaseKey: String): Call<OutputLogin>*/
}