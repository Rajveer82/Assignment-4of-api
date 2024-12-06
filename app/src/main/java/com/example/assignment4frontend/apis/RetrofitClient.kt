package com.example.assignment4frontend.apis

import com.example.assignment4frontend.apis.AuthApi



import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:1234/"

    private fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: Retrofit by lazy {
        buildRetrofit()
    }

    val authApi: AuthApi by lazy {
        instance.create(AuthApi::class.java)
    }
}
