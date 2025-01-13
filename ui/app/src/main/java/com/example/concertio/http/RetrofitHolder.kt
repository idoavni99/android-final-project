package com.example.concertio.http

import android.content.Context
import android.net.Uri
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

object RetrofitHolder {
    private val filesServiceApi = Retrofit.Builder().baseUrl("http://localhost:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val filesService = filesServiceApi.create(FilesService::class.java)
}