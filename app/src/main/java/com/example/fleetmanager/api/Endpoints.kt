package com.example.fleetmanager.api

import com.example.fleetmanager.entities.TruksDetailsPlate
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


    @FormUrlEncoded
    @POST("external/vehicle-by-plate")
    fun getVehicleDetails(@Field("plate") plate: String?): Call<TruksDetailsPlate>

    /*@POST("users/login")
    fun login(@Body firebaseKey: String): Call<OutputLogin>*/
}