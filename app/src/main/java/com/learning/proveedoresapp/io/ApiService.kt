package com.learning.proveedoresapp.io

import com.learning.proveedoresapp.io.response.LoginResponse
import com.learning.proveedoresapp.model.Empresa
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET(value="empresas")
    fun getEmpresas(): Call<ArrayList<Empresa>>

    companion object Factory {
        private const val BASE_URL = "http://192.168.1.6:89/api/"
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}