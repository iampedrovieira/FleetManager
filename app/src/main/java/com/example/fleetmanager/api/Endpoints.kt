package com.example.fleetmanager.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Endpoints {
    @FormUrlEncoded
    @POST("users/login")
    fun login(@Field("firebaseKey") firebaseKey: String): Call<OutputLogin>

    // Todos os veículos
    @FormUrlEncoded
    @POST("company/vehicle")
    fun getVehicles(@Field("companyKey") company_key: String?): Call<List<OutputVehicle>>

    // Todos os empregados
    @FormUrlEncoded
    @POST("company/employees")
    fun getEmployees(@Field("companyKey") company_key: String?): Call<List<OutputEmployee>>

    // Charts
    @FormUrlEncoded
    @POST("company/chart1")
    fun getchart1(@Field("companyKey") company_key: String?): Call<List<Chart1>>

    @FormUrlEncoded
    @POST("company/chart2")
    fun getchart2(@Field("companyKey") company_key: String?): Call<List<Chart2>>

    @FormUrlEncoded
    @POST("company/chart3")
    fun getchart3(@Field("companyKey") company_key: String?): Call<List<Chart3>>

    @FormUrlEncoded
    @POST("company/chart4")
    fun getchart4(@Field("companyKey") company_key: String?): Call<List<Chart4>>

    @FormUrlEncoded
    @POST("company/chart5")
    fun getchart5(@Field("companyKey") company_key: String?): Call<List<Chart5>>
}