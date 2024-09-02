package com.rmutto.ananchai_android

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("computers")
    fun addComputer(@Body computer: Computer): Call<ApiResponse>

    @GET("computers/{id}")
    fun getComputerById(@Path("id") id: Int): Call<Computer>
}
