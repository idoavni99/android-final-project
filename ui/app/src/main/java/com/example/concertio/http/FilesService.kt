package com.example.concertio.http

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FilesService {
    @GET("/file/:id")
    suspend fun getFile(@Path("id") id: String): Response<ResponseBody>

    @POST("/upload")
    suspend fun uploadFile(file: ByteArray): Response<ResponseBody>

    @PUT("file/:id")
    suspend fun updateFile(@Path("id") id: String, file: ByteArray): Response<ResponseBody>
}