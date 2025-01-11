package com.example.concertio.http

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHolder {
    private val retrofitInstance = Retrofit.Builder().baseUrl("http://localhost:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val filesService = retrofitInstance.create(FilesService::class.java)
}